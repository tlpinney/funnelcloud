# OpenStreetMap FunnelCloud 

Create a fast import of GDELT into a Impala/Parquet format to do analytics.
Blow away instance when done. 


## Setup 

## Requirements 

Download Java.

http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html#jdk-6u45-oth-JPR

Place jdk-6u45-linux-x64.bin in the media directory  

Download ec2-api-tools and configure Amazon Account 

curl -O http://s3.amazonaws.com/ec2-downloads/ec2-api-tools-1.6.9.0.zip

Set up environment variables, fill in values of XXXXXXX

    export AWS_ACCESS_KEY=XXXXXXX
    export AWS_SECRET_KEY=XXXXXXX
    export JAVA_HOME=XXXXXXXX
    export EC2_URL=https://ec2.us-east-1.amazonaws.com
    export EC2_HOME=/XXXXXXXXX/ec2-api-tools-1.6.9.0
    export PATH=$PATH:EC2_HOME/bin


Instantiate the node to run Impala, data is relatively small and it works well with
one node.


    ec2-request-spot-instances ami-d0f89fb9 -t m2.4xlarge -p '0.20' -n 1 -r 'one-time' -k XXXXXXXXX
    
Add in the name of the key your using, leaving off the .pem extension.

This will request a node at 20 cents per hour bid price. You may need to adjust
the spot instance price.


Include full path to key file for FCL_KEYPATH 

    # see if the instance came up and get its ip address
    ec2-describe-instances 
   
    export FCL_HOST=XXXXXX
    export FCL_KEYPATH=XXXXXX   
 
    # copy seed script and media to server
    # todo, figure out how to cache the key to known_hosts from the amazon API

    scp -i $FCL_KEYPATH -r media seed.sh osm.sh ubuntu@$FCL_HOST:

    # ssh to the server 
    ssh -i $FCL_KEYPATH ubuntu@$FCL_HOST

    # run the seed script
    sudo sh seed.sh 

    # exit and reconnect to set up the environment variables 
    exit 
    ssh -i $FCL_KEYPATH ubuntu@$FCL_HOST

    # run the geonames script to download and ingest geonames 
    # this may take a long time to download so use screen or tmux
    screen 
    sh osm.sh 
     

When this completes you should have a fully functional basic analytics system for GDELT data.
 
## Analytics 


Examples of very simple analytics.


## Blow away instance when done 

    # find the instance you went to terminate 
    ec2-describe-instances 
    # terminate instance 
    ec2-terminate-instances i-XXXXXXXXX










