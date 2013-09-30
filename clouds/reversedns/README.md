# Reversedns FunnelCloud 

Create a fast import of reversedns into a Impala/Parquet format to do analytics.
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

    scp -i $FCL_KEYPATH -r media reversedns.sh create_table.sql ubuntu@$FCL_HOST:

    # ssh to the server 
    ssh -i $FCL_KEYPATH ubuntu@$FCL_HOST

    # run the seed script
    sudo sh seed.sh 

    # exit and reconnect to set up the environment variables 
    exit 
    ssh -i $FCL_KEYPATH ubuntu@$FCL_HOST

    # run the script 
    sh reversedns.sh 
     

When this completes you should have a fully functional basic analytics system for reversedns data.
 
## Analytics 

Using some of the examples from here: https://community.rapid7.com/community/infosec/sonar/blog/2013/09/26/welcome-to-project-sonar


Get the first 5 records

    select * FROM reversedns LIMIT 5;

    Query: select * FROM reversedns LIMIT 5
    Query finished, fetching results ...
    +---------+-------------------------------------------------+
    | ip      | rdns                                            |
    +---------+-------------------------------------------------+
    | 1.0.4.0 | ip1.ip0.ip4.ip0.reverse.bigredgroup.net.au      |
    | 1.0.4.1 | loop0.brg_mel01_7206.bigredgroup.net.au         |
    | 1.0.4.2 | loop0.brg_mel02_7206.reverse.bigredgroup.net.au |
    | 1.0.4.3 | ip1.ip0.ip4.ip3.reverse.bigredgroup.net.au      |
    | 1.0.4.4 | ns1.bigredgroup.net.au                          |
    +---------+-------------------------------------------------+
    Returned 5 row(s) in 0.33s


Get records associated with rapid7.com

    SELECT * FROM reversedns WHERE rdns LIKE '%.rapid7.com';
    ...
    +-----------------+----------------------------+
    Returned 400 row(s) in 63.23s



Get records associated with 54.200.72.*

    SELECT * FROM reversedns WHERE ip LIKE '54.200.73.%'; 

      54.200.73.252 | ec2-54-200-73-252.us-west-2.compute.amazonaws.com |
    | 54.200.73.253 | ec2-54-200-73-253.us-west-2.compute.amazonaws.com |
    | 54.200.73.254 | ec2-54-200-73-254.us-west-2.compute.amazonaws.com |
    | 54.200.73.255 | ec2-54-200-73-255.us-west-2.compute.amazonaws.com |
    +---------------+---------------------------------------------------+
    Returned 252 row(s) in 31.37s



Get a count of all the records 

    Query: select COUNT(*) FROM reversedns
    Query finished, fetching results ...
    +------------+
    | count(*)   |
    +------------+
    | 1134579034 |
    +------------+
    Returned 1 row(s) in 18.19s

 
Get a count of everything that is a .com

    Query: select COUNT(*) FROM reversedns WHERE rdns LIKE '%.com'
    Query finished, fetching results ...
    +-----------+
    | count(*)  |
    +-----------+
    | 211575204 |
    +-----------+
    Returned 1 row(s) in 20.26s


Get an ip addresses associated with an original record 

    Query: select * FROM reversedns WHERE rdns = 'www.google.com'
    Query finished, fetching results ...
    +-----------------+----------------+
    | ip              | rdns           |
    +-----------------+----------------+
    | 65.59.189.222   | www.google.com |
    | 66.179.100.13   | www.google.com |
    | 87.118.110.142  | www.google.com |
    | 88.198.126.254  | www.google.com |
    | 103.29.168.50   | www.google.com |
    | 108.58.78.90    | www.google.com |
    | 192.241.171.188 | www.google.com |
    +-----------------+----------------+
    Returned 7 row(s) in 31.65s











## Blow away instance when done 

    # find the instance you went to terminate 
    ec2-describe-instances 
    # terminate instance 
    ec2-terminate-instances i-XXXXXXXXX










