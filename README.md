# FunnelCloud 

FunnelCloud is a simple tool for ingesting bulk data off the web. 

More documentation here: https://github.com/tlpinney/funnelcloud/wiki

## Prequisites 

CDH4.4

## Development Prequisiites

protobuf 


## Installation from Package  

    cd /tmp
    curl -O https://github.com/tlpinney/funnelcloud/releases/download/0.1.2/funnelcloud-0.1.2-bin.tar.gz
    cd /usr/local 
    sudo tar xzvf /tmp/funnelcloud-0.1.2-bin.tar.gz
    export PATH=$PATH:/usr/local/funnelcloud-0.1.2/bin 

    fcl -h 


## Installation from Git 


    git clone https://github.com/tlpinney/funnelcloud.git
    cd funnelcloud 
    # generate protobuf java classes
    protoc --java_out=src/main/java osmformat.proto 
    protoc --java_out=src/main/java fileformat.proto 
     



