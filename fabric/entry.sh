#!/bin/bash
ROOTDIR=$(cd "$(dirname "$0")" && pwd)


# Check if install-fabric.sh exists in the current directory
if [ ! -f "install-fabric.sh" ]; then
    echo "Downloading install-fabric.sh..."
    curl -sSLO https://raw.githubusercontent.com/hyperledger/fabric/main/scripts/install-fabric.sh
    chmod +x install-fabric.sh
fi

directory="bin"

# Check if the directory exists
if [ ! -d "$directory" ]; then
    # If it doesn't exist, create it
    mkdir "$directory"
fi

# Move into the directory
cd "$directory" || exit

# Install binaryies 
$ROOTDIR/install-fabric.sh "binary" "docker"

# Move back to the previous directory
cd - || exit


