
DROP TABLE IF EXISTS gdelt_historical_raw;

CREATE EXTERNAL TABLE gdelt_historical_raw
(
  globaleventid BIGINT, 
  sqldate STRING, 
  MonthYear STRING, 
  Year STRING, 
  FractionDate DOUBLE, 
  Actor1Code STRING, 
  Actor1Name STRING, 
  Actor1CountryCode STRING, 
  Actor1KnownGroupCode STRING, 
  Actor1EthnicCode STRING, 
  Actor1Religion1Code STRING, 
  Actor1Religion2Code STRING, 
  Actor1Type1Code STRING, 
  Actor1Type2Code STRING, 
  Actor1Type3Code STRING, 
  Actor2Code STRING, 
  Actor2Name STRING, 
  Actor2CountryCode STRING, 
  Actor2KnownGroupCode STRING, 
  Actor2EthnicCode STRING, 
  Actor2Religion1Code STRING, 
  Actor2Religion2Code STRING, 
  Actor2Type1Code STRING, 
  Actor2Type2Code STRING, 
  Actor2Type3Code STRING, 
  IsRootEvent INT, 
  EventCode STRING, 
  EventBaseCode STRING, 
  EventRootCode STRING, 
  QuadClass INT, 
  GoldsteinScale DOUBLE, 
  NumMentions INT, 
  NumSources INT, 
  NumArticles INT, 
  AvgTone DOUBLE, 
  Actor1Geo_Type INT, 
  Actor1Geo_FullName STRING, 
  Actor1Geo_CountryCode STRING, 
  Actor1Geo_ADM1Code STRING, 
  Actor1Geo_Lat FLOAT, 
  Actor1Geo_Long FLOAT, 
  Actor1Geo_FeatureID INT, 
  Actor2Geo_Type INT, 
  Actor2Geo_FullName STRING, 
  Actor2Geo_CountryCode STRING, 
  Actor2Geo_ADM1Code STRING, 
  Actor2Geo_Lat FLOAT, 
  Actor2Geo_Long FLOAT, 
  Actor2Geo_FeatureID INT, 
  ActionGeo_Type INT, 
  ActionGeo_FullName STRING, 
  ActionGeo_CountryCode STRING, 
  ActionGeo_ADM1Code STRING, 
  ActionGeo_Lat FLOAT, 
  ActionGeo_Long FLOAT, 
  ActionGeo_FeatureID INT, 
  dateadded INT
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
LOCATION '/user/ubuntu/gdelt_historical_tsv';

SET PARQUET_COMPRESSION_CODEC=snappy;

DROP TABLE IF EXISTS gdelt_historical;

CREATE TABLE gdelt_historical LIKE gdelt_historical_raw STORED AS PARQUETFILE;

INSERT OVERWRITE TABLE gdelt_historical SELECT * FROM gdelt_historical_raw;



DROP TABLE IF EXISTS gdelt_dailyupdates_raw;

CREATE EXTERNAL TABLE gdelt_dailyupdates_raw
(
  globaleventid BIGINT, 
  sqldate STRING, 
  MonthYear STRING, 
  Year STRING, 
  FractionDate DOUBLE, 
  Actor1Code STRING, 
  Actor1Name STRING, 
  Actor1CountryCode STRING, 
  Actor1KnownGroupCode STRING, 
  Actor1EthnicCode STRING, 
  Actor1Religion1Code STRING, 
  Actor1Religion2Code STRING, 
  Actor1Type1Code STRING, 
  Actor1Type2Code STRING, 
  Actor1Type3Code STRING, 
  Actor2Code STRING, 
  Actor2Name STRING, 
  Actor2CountryCode STRING, 
  Actor2KnownGroupCode STRING, 
  Actor2EthnicCode STRING, 
  Actor2Religion1Code STRING, 
  Actor2Religion2Code STRING, 
  Actor2Type1Code STRING, 
  Actor2Type2Code STRING, 
  Actor2Type3Code STRING, 
  IsRootEvent INT, 
  EventCode STRING, 
  EventBaseCode STRING, 
  EventRootCode STRING, 
  QuadClass INT, 
  GoldsteinScale DOUBLE, 
  NumMentions INT, 
  NumSources INT, 
  NumArticles INT, 
  AvgTone DOUBLE, 
  Actor1Geo_Type INT, 
  Actor1Geo_FullName STRING, 
  Actor1Geo_CountryCode STRING, 
  Actor1Geo_ADM1Code STRING, 
  Actor1Geo_Lat FLOAT, 
  Actor1Geo_Long FLOAT, 
  Actor1Geo_FeatureID INT, 
  Actor2Geo_Type INT, 
  Actor2Geo_FullName STRING, 
  Actor2Geo_CountryCode STRING, 
  Actor2Geo_ADM1Code STRING, 
  Actor2Geo_Lat FLOAT, 
  Actor2Geo_Long FLOAT, 
  Actor2Geo_FeatureID INT, 
  ActionGeo_Type INT, 
  ActionGeo_FullName STRING, 
  ActionGeo_CountryCode STRING, 
  ActionGeo_ADM1Code STRING, 
  ActionGeo_Lat FLOAT, 
  ActionGeo_Long FLOAT, 
  ActionGeo_FeatureID INT, 
  dateadded INT,
  sourceurl STRING 
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
LOCATION '/user/ubuntu/gdelt_dailyupdates_tsv';

SET PARQUET_COMPRESSION_CODEC=snappy;

DROP TABLE IF EXISTS gdelt_dailyupdates;

CREATE TABLE gdelt_dailyupdates LIKE gdelt_dailyupdates_raw STORED AS PARQUETFILE;

INSERT OVERWRITE TABLE gdelt_dailyupdates SELECT * FROM gdelt_dailyupdates_raw;



