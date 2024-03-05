#!/bin/bash
ROOTDIR=$(cd "$(dirname "$0")" && pwd)


# Check if install-fabric.sh exists in the current directory
if [ ! -f "install-fabric.sh" ]; then
    echo "Downloading install-fabric.sh..."
    curl -sSLO https://raw.githubusercontent.com/hyperledger/fabric/main/scripts/install-fabric.sh
    chmod +x install-fabric.sh
fi


# Install binaries

./install-fabric.sh "binary" "docker"

# # Move back to the previous directory
# cd - || exit


