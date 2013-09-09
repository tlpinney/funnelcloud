# FunnelCloud 

    git clone http://github.com/tlpinney/funnelcloud 
    cd funnelcloud
    mvn clean compile
    mvn package 
    hadoop fs -mkdir /osm-download 
    hadoop jar target/funnelcloud-0.1.0.jar http://download.geofabrik.de/north-america/us/district-of-columbia-latest.osm.pbf /osm-download/district-of-columbia-latest.osm.pbf




## OpenStreetMap 

Generate protocol buffer files 

    protoc --java_out=src/main/java osmformat.proto
    protoc --java_out=src/main/java fileformat.proto
    
    
## Issues 

    vagrant@precise64:~/funnelcloud$ hadoop jar target/funnelcloud-0.1.0.jar http://download.geofabrik.de/north-america/us/district-of-columbia-latest.osm.pbf /osm-download/district-of-columbia-latest.osm.pbf
    13/09/09 14:34:42 WARN hdfs.DFSClient: DataStreamer Exception
    org.apache.hadoop.ipc.RemoteException(java.io.IOException): File /osm-download/district-of-columbia-latest.osm.pbf could only be replicated to 0 nodes instead of minReplication (=1).  There are 0 datanode(s) running and no node(s) are excluded in this operation.

Make sure hdfs is not in safe mode

	hdfs dfsadmin -safemode leave
