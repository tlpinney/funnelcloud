sudo apt-get install unzip -y
sudo mkdir /mnt/geonames-staging 
sudo chown ubuntu /mnt/geonames-staging 
cd /mnt/geonames-staging 
curl -O http://download.geonames.org/export/dump/allCountries.zip
unzip allCountries.zip
hdfs dfs -mkdir geonames_tsv
hdfs dfs -put allCountries.txt geonames_tsv 
impala-shell -i localhost -f /vagrant/media/sql/create_geonames.sql 






