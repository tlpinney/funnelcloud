# FunnelCloud 

    git clone http://github.com/tlpinney/funnelcloud 
    cd funnelcloud
    mvn package 
    hadoop fs -mkdir /osm-download 
    hadoop jar target/funnelcloud-0.1.0.jar http://download.geofabrik.de/north-america/us/district-of-columbia-latest.osm.pbf /osm-download/district-of-columbia-latest.osm.pbf
