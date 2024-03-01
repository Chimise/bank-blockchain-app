#!/bin/bash
ROOTDIR=$(cd "$(dirname "$0")" && pwd)
export PATH=${ROOTDIR}/../bin:${PWD}/../bin:$PATH
export FABRIC_CFG_PATH=${PWD}/configtx
export VERBOSE=false

. scripts/utils.sh
. scripts/envVar.sh

# If CONTAINER_CLI enviroment variable is set use it as is else set it to docker
: ${CONTAINER_CLI:="docker"}
: ${CHANNEL_NAME:="mainchannel"}
: ${DELAY:="3"}
: ${MAX_RETRY:="5"}
: ${VERBOSE:="false"}

if [[ $# -ge 1 ]]; then
    CHANNEL_NAME=$1
fi

if command -v ${CONTAINER_CLI}-compose >/dev/null 2>&1; then
    : ${CONTAINER_CLI_COMPOSE:="${CONTAINER_CLI}-compose"}
else
    : ${CONTAINER_CLI_COMPOSE:="${CONTAINER_CLI} compose"}
fi

infoln "Using ${CONTAINER_CLI} and ${CONTAINER_CLI_COMPOSE}"

function checkPrereqs() {

    if ! $CONTAINER_CLI info >/dev/null 2>&1; then
        fatalln "$CONTAINER_CLI network is required to be running to create a channel"
    fi

    # check if all containers are present
    CONTAINERS=($($CONTAINER_CLI ps | grep hyperledger/ | awk '{print $2}'))
    len=$(echo ${#CONTAINERS[@]})

    # if [[ $len -lt 4 ]]; then
    #     echo "Containers not running properly"
    #     exit 0
    # fi

    if [ ! -d "organizations/peerOrganizations" ]; then
        echo "No certificates found, execute network.sh and try again"
        exit 0
    fi
}

#  now run the script that creates a channel. This script uses configtxgen once
# to create the channel creation transaction and the anchor peer updates.
# scripts/createChannel.sh $CHANNEL_NAME $CLI_DELAY $MAX_RETRY $VERBOSE $bft_true

createChannelGenesisBlock() {

    if [ ! -d "channel-artifacts" ]; then
        mkdir channel-artifacts
    fi

    setGlobals 0
    which configtxgen
    if [ "$?" -ne 0 ]; then
        fatalln "configtxgen tool not found."
        exit 0
    fi

    set -x
    configtxgen -profile ChannelUsingRaft -outputBlock ./channel-artifacts/${CHANNEL_NAME}.block -channelID $CHANNEL_NAME
    res=$?
    { set +x; } 2>/dev/null

    verifyResult $res "Failed to generate channel configuration transaction..."
}

createChannel() {
    # Poll in case the raft leader is not set yet
    local rc=1
    local COUNTER=1
    infoln "Adding orderers"
    while [ $rc -ne 0 -a $COUNTER -lt $MAX_RETRY ]; do
        sleep $DELAY
        set -x
        createChannelWithOrdererInfo ${CHANNEL_NAME} >/dev/null 2>&1
        res=$?
        { set +x; } 2>/dev/null
        let rc=$res
        COUNTER=$(expr $COUNTER + 1)
    done
    cat log.txt
    verifyResult $res "Channel creation failed"
}

createChannelWithOrdererInfo() {
    local channel_name=$1

    export ORDERER_ADMIN_TLS_SIGN_CERT=${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/tls/server.crt /dev/null 2>&1
    export ORDERER_ADMIN_TLS_PRIVATE_KEY=${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/tls/server.key /dev/null 2>&1

    osnadmin channel join --channelID ${channel_name} --config-block ./channel-artifacts/${channel_name}.block -o localhost:7053 --ca-file "$ORDERER_CA" --client-cert "$ORDERER_ADMIN_TLS_SIGN_CERT" --client-key "$ORDERER_ADMIN_TLS_PRIVATE_KEY" >>log.txt 2>&1
}

joinPeerToChannel() {
    local PEER=$1
    FABRIC_CFG_PATH=$PWD/../config/
    setGlobals $PEER
    local rc=1
    local COUNTER=1
    ## Sometimes Join takes time, hence retry
    while [ $rc -ne 0 -a $COUNTER -lt $MAX_RETRY ]; do
        sleep $DELAY
        set -x
        peer channel join -b $BLOCKFILE >&log.txt
        res=$?
        { set +x; } 2>/dev/null
        let rc=$res
        COUNTER=$(expr $COUNTER + 1)
    done
    cat log.txt
    verifyResult $res "After $MAX_RETRY attempts, peer0.org${ORG} has failed to join channel '$CHANNEL_NAME' "
}

setAnchorPeer() {
  PEER=$1
  ${CONTAINER_CLI} exec cli ./scripts/setAnchorPeer.sh $PEER $CHANNEL_NAME 
}


## Create channel genesis block
FABRIC_CFG_PATH=$PWD/../config/
BLOCKFILE="./channel-artifacts/${CHANNEL_NAME}.block"

#Check whether prerequities are available
checkPrereqs

#Generate a genesis block
infoln "Generating channel genesis block '${CHANNEL_NAME}.block'"
FABRIC_CFG_PATH=${PWD}/configtx
createChannelGenesisBlock

## Create channel
infoln "Creating channel ${CHANNEL_NAME}"
createChannel
successln "Channel '$CHANNEL_NAME' created"

infoln "Joining peer0 to the channel..."
joinPeerToChannel 0

infoln "Joining peer1 to the channel..."
joinPeerToChannel 1

infoln "Joining peer2 to the channel..."
joinPeerToChannel 2

infoln "Setting peer0 as anchor peer..."
setAnchorPeer 0

