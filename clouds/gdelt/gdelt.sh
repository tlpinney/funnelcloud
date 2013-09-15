sudo apt-get install unzip -y
sudo mkdir /mnt/gdelt-staging 
sudo chown ubuntu /mnt/gdelt-staging 
cd /mnt/gdelt-staging 

hdfs dfs -mkdir gdelt_historical_tsv 
for i in `cat /home/ubuntu/backfiles.txt`; do
  curl -O http://gdelt.utdallas.edu/data/backfiles/$i.zip
  unzip $i.zip
  tail -n +2 $i.csv > $i.tmp 
  mv $i.tmp $i.csv 
  hdfs dfs -put $i.csv gdelt_historical_tsv 
done 

hdfs dfs -mkdir gdelt_dailyupdates_tsv 
for i in `cat /home/ubuntu/dailyupdates.txt`; do 
  curl -O http://gdelt.utdallas.edu/data/dailyupdates/$i.zip 
  unzip $i.zip 
  tail -n +2 $i > $i.tmp 
  mv $i.tmp $i 
  hdfs dfs -put $i gdelt_dailyupdates_tsv 
done


#hdfs dfs -mkdir geonames_tsv
#hdfs dfs -put allCountries.txt geonames_tsv 
#impala-shell -i localhost -f /vagrant/media/sql/create_geonames.sql 






