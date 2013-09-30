
DROP TABLE IF EXISTS reversedns_raw;

CREATE EXTERNAL TABLE reversedns_raw
(
  ip STRING, 
  rdns STRING 
)

ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
LOCATION '/user/ubuntu/reversedns_tsv';

SET PARQUET_COMPRESSION_CODEC=snappy;

DROP TABLE IF EXISTS reversedns;

CREATE TABLE reversedns LIKE reversedns_raw STORED AS PARQUETFILE;

INSERT OVERWRITE TABLE reveresedns SELECT * FROM reversedns_raw;





