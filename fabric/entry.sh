#!/bin/bash

# Check if install-fabric.sh exists in the current directory
if [ ! -f "install-fabric.sh" ]; then
    echo "Downloading install-fabric.sh..."
    curl -sSLO https://raw.githubusercontent.com/hyperledger/fabric/main/scripts/install-fabric.sh
    chmod +x install-fabric.sh
fi

# Execute the script with necessary arguments
./install-fabric.sh "binary"
