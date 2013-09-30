sudo apt-get install unzip -y
sudo mkdir /mnt/gdelt-staging 
sudo chown ubuntu /mnt/gdelt-staging 
cd /mnt/gdelt-staging 

hdfs dfs -mkdir gdelt_historical_tsv 
for i in `cat /home/ubuntu/backfiles.txt`; do
  curl -O http://gdelt.utdallas.edu/data/backfiles/$i.zip
  unzip $i.zip && hdfs dfs -put $i.csv gdelt_historical_tsv &  
done 

hdfs dfs -mkdir gdelt_dailyupdates_tsv 
for i in `cat /home/ubuntu/dailyupdates.txt`; do 
  curl -O http://gdelt.utdallas.edu/data/dailyupdates/$i.zip 
  unzip $i.zip && hdfs dfs -put $i gdelt_dailyupdates_tsv &  
done

echo "Be Patient, will take a while to load" 
#impala-shell -i localhost -f /vagrant/media/sql/create_gdelt.sql 






