# GeoNames FunnelCloud 

Create a fast import of GeoNames into a Impala/Parquet format to do analytics.
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


Instantiate node to run Impala, data is relatively small and it works well with
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

    scp -i $FCL_KEYPATH -r media seed.sh geonames.sh ubuntu@$FCL_HOST:

    # ssh to the server 
    ssh -i $FCL_KEYPATH ubuntu@$FCL_HOST

    # run the seed script
    sudo sh seed.sh 

    # exit and reconnect to set up the environment variables 
    exit 
    ssh -i $FCL_KEYPATH ubuntu@$FCL_HOST

    # run the geonames script to download and ingest geonames 
    sh geonames.sh 
     

When this completes you should have a fully functional basic analytics system for GeoNames data.
 
## Analytics 


Examples of very simple analytics.

    impala-shell -i localhost

## Get row count 

    [localhost:21000] > select count(*) from geonames;
    Query: select count(*) from geonames
    Query finished, fetching results ...
    +----------+
    | count(*) |
    +----------+
    | 8526080  |
    +----------+
    Returned 1 row(s) in 0.23s


## Get top 10 most common names in the name column 

    [localhost:21000] > select name, count(name) from geonames group by name order by count(name) desc limit 10;
    Query: select name, count(name) from geonames group by name order by count(name) desc limit 10
    Query finished, fetching results ...
    +-------------------------------------------------+-------------+
    | name                                            | count(name) |
    +-------------------------------------------------+-------------+
    | First Baptist Church                            | 2634        |
    | The Church of Jesus Christ of Latter Day Saints | 2175        |
    | Church of Christ                                | 1656        |
    | San Antonio                                     | 1593        |
    | Mill Creek                                      | 1528        |
    | Spring Creek                                    | 1482        |
    | San José                                        | 1404        |
    | First Presbyterian Church                       | 1337        |
    | Dry Creek                                       | 1261        |
    | Santa Rosa                                      | 1231        |
    +-------------------------------------------------+-------------+
    Returned 10 row(s) in 7.60s

##  Get the top 10 most populous places 

    [localhost:21000] > select geonameid, name, population from geonames order by population desc limit 10;
    Query: select geonameid, name, population from geonames order by population desc limit 10
    Query finished, fetching results ...
    +-----------+-------------------------------+------------+
    | geonameid | name                          | population |
    +-----------+-------------------------------+------------+
    | 6295630   | Earth                         | 6814400000 |
    | 6255147   | Asia                          | 3812366000 |
    | 1814991   | People’s Republic of China    | 1330044000 |
    | 1269750   | Republic of India             | 1173108018 |
    | 6255150   | South America                 | 357000000  |
    | 6252001   | United States                 | 310232863  |
    | 1643084   | Republic of Indonesia         | 242968342  |
    | 3469034   | Federative Republic of Brazil | 201103330  |
    | 1168579   | Islamic Republic of Pakistan  | 184404791  |
    | 1253626   | State of Uttar Pradesh        | 166052900  |
    +-----------+-------------------------------+------------+
    Returned 10 row(s) in 1.14s



## Get all places named Tokyo 

    [localhost:21000] > select geonameid, name,asciiname,population  population from geonames where asciiname = 'Tokyo';
    Query: select geonameid, name,asciiname,population  population from geonames where asciiname = 'Tokyo'
    Query finished, fetching results ...
    +-----------+-------+-----------+------------+
    | geonameid | name  | asciiname | population |
    +-----------+-------+-----------+------------+
    | 6300404   | Tokyo | Tokyo     | 0          |
    | 2085290   | Tokyo | Tokyo     | 0          |
    | 7045155   | Tokyo | Tokyo     | 0          |
    | 8610075   | Tokyo | Tokyo     | 0          |
    | 8610595   | Tokyo | Tokyo     | 0          |
    | 1850146   | Tōkyō | Tokyo     | 0          |
    | 1850147   | Tokyo | Tokyo     | 8336599    |
    | 7945384   | Tokyo | Tokyo     | 0          |
    +-----------+-------+-----------+------------+
    Returned 8 row(s) in 1.87s





   Please do a pull request to add more. 



## Blow away instance when done 

    # find the instance you went to terminate 
    ec2-describe-instances 
    # terminate instance 
    ec2-terminate-instances i-XXXXXXXXX










