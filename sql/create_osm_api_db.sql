set PARQUET_COMPRESSION_CODEC=snappy;


DROP TABLE IF EXISTS changeset_tags;


CREATE TABLE changeset_tags (
    id BIGINT, -- references changesets(id) NOT NULL
    k STRING, -- defaults to ''
    v STRING -- defaults to ''
) STORED AS PARQUETFILE;

DROP TABLE IF EXISTS changesets;

CREATE TABLE changesets (
    id BIGINT, -- NOT NULL autoincrement primary key
    user_id BIGINT, -- NOT NULLreferences users(id)
    created_at TIMESTAMP, -- WITHOUT TIME zone NOT NULL,
    min_lat INTEGER,
    max_lat INTEGER,
    min_lon INTEGER,
    max_lon INTEGER,
    closed_at TIMESTAMP, -- WITHOUT TIME zone NOT NULL,
    num_changes INTEGER -- DEFAULT 0 NOT NULL
) STORED AS PARQUETFILE;


DROP TABLE IF EXISTS current_node_tags;

CREATE TABLE current_node_tags (
    id BIGINT, -- NOT NULL,  primary key part 1/2; references current_nodes(id)
    k STRING, -- DEFAULT ''::CHARACTER VARYING NOT NULL, primary key part 2/2
    v STRING -- DEFAULT ''::CHARACTER VARYING NOT NULL
) STORED AS PARQUETFILE;
 
 
 DROP TABLE IF EXISTS current_nodes;
  
CREATE TABLE current_nodes (
    id BIGINT, -- NOT NULL, autoincrement primary key
    latitude DOUBLE, -- NOT NULL, should be INTEGER
    longitude DOUBLE, -- NOT NULL, should be INTEGER
    changeset_id BIGINT, -- NOT NULL, references changesets(id)
    visible BOOLEAN, -- NOT NULL,
    tstamp TIMESTAMP, -- WITHOUT TIME zone NOT NULL, "timestamp"
    tile BIGINT,  -- NOT NULL,
    version BIGINT -- NOT NULL
) STORED AS PARQUETFILE;
 


DROP TABLE IF EXISTS current_way_nodes;

CREATE TABLE current_way_nodes (
    id BIGINT, -- NOT NULL primary key part 1/2; references current_ways(id)
    node_id BIGINT, -- NOT NULL, references current_nodes(id)
    sequence_id BIGINT -- NOT NULL primary key part 2/2
) STORED AS PARQUETFILE;

DROP TABLE IF EXISTS current_way_tags;


CREATE TABLE current_way_tags (
    id BIGINT, -- NOT NULL primary key part 1/2; references current_ways(id)
    k STRING, -- DEFAULT ''::CHARACTER VARYING NOT NULL,  primary key part 2/2
    v STRING --  DEFAULT ''::CHARACTER VARYING NOT NULL
) STORED AS PARQUETFILE;

DROP TABLE IF EXISTS current_ways;

CREATE TABLE current_ways (
    id BIGINT, -- NOT NULL,  autoincrement primary key
    changeset_id BIGINT, -- NOT NULL,  references changesets(id)
    tstamp TIMESTAMP, -- WITHOUT TIME zone NOT NULL, "timestamp"
    visible BOOLEAN, -- NOT NULL,
    version BIGINT -- NOT NULL
) STORED AS PARQUETFILE;

DROP TABLE IF EXISTS current_relation_members;

CREATE TABLE current_relation_members (
    id BIGINT, --  NOT NULL, primary key part 1/5; references current_relations(id)
    member_type TINYINT, -- nwr_enum NOT NULL, -- primary key part 2/5
    member_id BIGINT, -- NOT NULL, -- primary key part 3/5
    member_role STRING, -- NOT NULL, -- primary key part 4/5
    sequence_id INTEGER -- DEFAULT 0 NOT NULL -- primary key part 5/5
) STORED AS PARQUETFILE;

DROP TABLE IF EXISTS current_relation_tags;

CREATE TABLE current_relation_tags (
    id BIGINT, --  NOT NULL primary key part 1/2; references current_relations(id)
    k STRING, -- DEFAULT ''::CHARACTER VARYING NOT NULL, --primary key part 2/2
    v STRING --DEFAULT ''::CHARACTER VARYING NOT NULL
) STORED AS PARQUETFILE;
 
 DROP TABLE IF EXISTS current_relations;
 
 CREATE TABLE current_relations (
    id BIGINT, -- NOT NULL, autoincrement primary key
    changeset_id BIGINT, -- NOT NULL, references changesets(id)
    tstamp TIMESTAMP, -- WITHOUT TIME zone NOT NULL, "timestamp"
    visible BOOLEAN, -- NOT NULL,
    version BIGINT -- NOT NULL
) STORED AS PARQUETFILE;
 
 
 
 
 
 





