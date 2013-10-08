
DROP TABLE IF EXISTS current_node_tags_tsv;

CREATE EXTERNAL TABLE current_node_tags_tsv (
    id BIGINT, 
    k STRING, 
    v STRING 
) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
LOCATION '/user/vagrant/current_node_tags_tsv';

INSERT OVERWRITE TABLE current_node_tags SELECT * FROM current_node_tags_tsv;
 
 
-- DROP TABLE IF EXISTS changeset_tags_tsv;
-- CREATE EXTERNAL TABLE changeset_tags_tsv (
--    id BIGINT, 
--    k STRING, 
--    v STRING'
--) 
--ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
--LOCATION '/user/vagrant/changeset_tags_tsv';

--INSERT OVERWRITE TABLE changeset_tags SELECT * FROM changeset_tags_tsv;


--DROP TABLE IF EXISTS changesets_tsv;

--CREATE EXTERNAL TABLE changesets_tsv (
--    id BIGINT, 
--    user_id BIGINT,
--    created_at TIMESTAMP, 
--    min_lat INTEGER,
--    max_lat INTEGER,
--    min_lon INTEGER,
--    max_lon INTEGER,
--    closed_at TIMESTAMP, 
--    num_changes INTEGER 
--)
--ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
--LOCATION '/user/vagrant/changeset_tsv';

--INSERT OVERWRITE TABLE changeset SELECT * FROM changeset_tsv;


 
DROP TABLE IF EXISTS current_nodes_tsv;
  
CREATE EXTERNAL TABLE current_nodes_tsv (
    id BIGINT, 
    latitude DOUBLE, 
    longitude DOUBLE, 
    changeset_id BIGINT, 
    visible BOOLEAN,
    tstamp TIMESTAMP, 
    tile BIGINT,  
    version BIGINT 
) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
LOCATION '/user/vagrant/current_nodes_tsv';

INSERT OVERWRITE TABLE current_nodes SELECT * FROM current_nodes_tsv;


DROP TABLE IF EXISTS current_way_nodes_tsv;

CREATE  EXTERNAL TABLE current_way_nodes_tsv (
    id BIGINT, 
    node_id BIGINT, 
    sequence_id BIGINT 
) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
LOCATION '/user/vagrant/current_way_nodes_tsv';

INSERT OVERWRITE TABLE current_way_nodes SELECT * FROM current_way_nodes_tsv;



DROP TABLE IF EXISTS current_way_tags_tsv;

CREATE  EXTERNAL TABLE current_way_tags_tsv (
    id BIGINT, 
    k STRING, 
    v STRING
) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
LOCATION '/user/vagrant/current_way_tags_tsv';

INSERT OVERWRITE TABLE current_way_tags SELECT * FROM current_way_tags_tsv;


DROP TABLE IF EXISTS current_ways_tsv;

CREATE  EXTERNAL TABLE current_ways_tsv (
    id BIGINT, 
    changeset_id BIGINT,
    tstamp TIMESTAMP, 
    visible BOOLEAN, 
    version BIGINT 
) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
LOCATION '/user/vagrant/current_ways_tsv';

INSERT OVERWRITE TABLE current_ways SELECT * FROM current_ways_tsv;


DROP TABLE IF EXISTS current_relation_members_tsv;

CREATE  EXTERNAL TABLE current_relation_members_tsv (
    id BIGINT, 
    member_type TINYINT, 
    member_id BIGINT, 
    member_role STRING, 
    sequence_id INTEGER 
) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
LOCATION '/user/vagrant/current_relation_members_tsv';

INSERT OVERWRITE TABLE current_relation_members SELECT * FROM current_relation_members_tsv;



DROP TABLE IF EXISTS current_relation_tags_tsv;

CREATE  EXTERNAL TABLE current_relation_tags_tsv (
    id BIGINT, 
    k STRING, 
    v STRING 
) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
LOCATION '/user/vagrant/current_relation_members_tsv';

INSERT OVERWRITE TABLE current_relation_tags SELECT * FROM current_relation_tags_tsv;

 
DROP TABLE IF EXISTS current_relations_tsv;
 
CREATE EXTERNAL TABLE current_relations_tsv (
    id BIGINT, 
    changeset_id BIGINT,
    tstamp TIMESTAMP, 
    visible BOOLEAN,
    version BIGINT
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
LOCATION '/user/vagrant/current_relations_tsv';

INSERT OVERWRITE TABLE current_relations SELECT * FROM current_relations_tsv;





 