sudo apt-get install pbzip2 -y
sudo mkdir /mnt/reversedns-staging 
sudo chown ubuntu /mnt/reversedns-staging 
cd /mnt/reversedns-staging 

hdfs dfs -mkdir reversedns_tsv 

curl -O https://scans.io/data/rapid7/sonar.rdns/20130921/20130921_01.csv.bz2 
pbzip -d 20130921_01.csv.bz2 && hdfs dfs -put 20130921_01.csv reversedns_tsv &  
curl -O https://scans.io/data/rapid7/sonar.rdns/20130921/20130921_02.csv.bz2
pbzip -d 20130921_02.csv.bz2 && hdfs dfs -put 20130921_02.csv reversedns_tsv & 
curl -O https://scans.io/data/rapid7/sonar.rdns/20130921/20130921_03.csv.bz2
pbzip -d 20130921_03.csv.bz2 && hdfs dfs -put 20130921_03.csv reversedns_tsv & 





