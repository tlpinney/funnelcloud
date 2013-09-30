#!/bin/bash


if [ ! -d /vagrant/media ]; then
  echo "Creating vagrant directory and moving media"
  mkdir /vagrant 
  mv media /vagrant
fi 
  

echo "127.0.1.1 master" >> /etc/hosts

apt-get update -y
apt-get install git curl vim -y

cp /vagrant/media/cloudera.list /etc/apt/sources.list.d
cp /vagrant/media/cloudera-impala.list /etc/apt/sources.list.d 

curl -s http://archive.cloudera.com/cdh4/ubuntu/precise/amd64/cdh/archive.key | apt-key add -

if [ ! -d /usr/local/jdk1.6.0_45 ]; then 
  cd /usr/local
  sh /vagrant/media/jdk-6u45-linux-x64.bin
fi

cp /vagrant/media/bashrc /root/.bashrc
sudo -u vagrant cp /vagrant/media/bashrc /home/vagrant/.bashrc

cp /vagrant/media/environment /etc/environment 
export JAVA_HOME=/usr/local/jdk1.6.0_45
export PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/opt/vagrant_ruby/bin:/usr/local/jdk1.6.0_45/bin

apt-get update -y
apt-get install hadoop-hdfs-namenode hadoop-hdfs-datanode hadoop-mapreduce -y
mkdir -p /var/lib/hadoop-hdfs/cache/hdfs/dfs/name

# copy hadoop conf files 
cp /vagrant/media/hadoop/core-site.xml /etc/hadoop/conf
cp /vagrant/media/hadoop/hdfs-site.xml /etc/hadoop/conf
cp /vagrant/media/hadoop/mapred-site.xml /etc/hadoop/conf 

mkdir -p /mnt/data/1/mapred/local /mnt/data/2/mapred/local /mnt/data/3/mapred/local /mnt/data/4/mapred/local
chown -R mapred:hadoop /mnt/data/1/mapred/local /mnt/data/2/mapred/local /mnt/data/3/mapred/local /mnt/data/4/mapred/local


chown hdfs /var/lib/hadoop-hdfs/cache/hdfs/dfs/name
/etc/init.d/hadoop-hdfs-namenode restart
/etc/init.d/hadoop-hdfs-datanode restart
mkdir -p /mnt/var/lib/hadoop-hdfs/cache/hdfs/dfs/name
chown hdfs.hdfs /mnt/var/lib/hadoop-hdfs/cache/hdfs/dfs/name
mkdir -p /mnt/dfs 
chown hdfs.hdfs /mnt/dfs
echo "Formatting namenode"
/etc/init.d/hadoop-hdfs-namenode init
/etc/init.d/hadoop-hdfs-namenode restart
/etc/init.d/hadoop-hdfs-datanode restart
mkdir -p /mnt/staging
chown hdfs /mnt/staging

# create hdfs tmp directory
sudo -u hdfs hadoop fs -mkdir /tmp
sudo -u hdfs hadoop fs -chmod -R 1777 /tmp

# create mapreduce var directories 
sudo -u hdfs hadoop fs -mkdir -p /var/lib/hadoop-hdfs/cache/mapred/mapred/staging
sudo -u hdfs hadoop fs -chmod 1777 /var/lib/hadoop-hdfs/cache/mapred/mapred/staging
sudo -u hdfs hadoop fs -chown -R mapred /var/lib/hadoop-hdfs/cache/mapred

# create mapreduce system dir in hdfs 
sudo -u hdfs hadoop fs -mkdir /tmp/mapred/system
sudo -u hdfs hadoop fs -chown mapred:hadoop /tmp/mapred/system

# start hadoop mapreduce tracker 
# sudo /etc/init.d/hadoop-0.20-mapreduce-jobtracker start

# create a home directory for the mapreduce user 
sudo -u hdfs hadoop fs -mkdir  /user/ubuntu
sudo -u hdfs hadoop fs -chown ubuntu /user/ubuntu

# set up impala and hive 
apt-get install impala impala-shell impala-server impala-state-store hive hive-server -y
cp /vagrant/media/impala/hive-site.xml /etc/impala 


service impala-state-store start
service impala-server start




exit 0
