
CREATE EXTERNAL TABLE geonames_raw
(
   geonameid INT,
   name STRING,
   asciiname STRING,
   alternatenames STRING,
   latitude FLOAT,
   longitude FLOAT,
   feature_class STRING,
   country_code STRING,
   cc2 STRING, 
   admin1_code STRING,
   admin2_code STRING,
   admin3_code STRING,
   admin4_code STRING,
   population BIGINT,
   elevation INT,
   dem STRING,
   timezone STRING,
   modification_date TIMESTAMP
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
LOCATION '/user/ubuntu/geonames_tsv';

set PARQUET_COMPRESSION_CODEC=snappy;
create table geonames LIKE geonames_raw STORED AS PARQUETFILE;

insert overwrite table geonames  select * from geonames_raw;

