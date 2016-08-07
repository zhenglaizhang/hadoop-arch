#!/usr/bin/env bash

# scala install
wget www.scala-lang.org/files/archive/scala-2.11.7.deb
sudo dpkg -i scala-2.11.7.deb


# sbt installation
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
sudo apt-get update
sudo apt-get install sbt


# java install
sudo apt-get install python-software-properties
sudo apt-get install software-properties-common
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer


# git install
sudo apt-get install git


sudo dpkg -i DEB_PACKAGE
sudo dpkg -r DEB_PACKAGE


# cleanup package cache
sudo apt-get autoclean
sudo apt-get clean
sudo apt-get autoremove

sudo localepurge


# install parallels desktop
# mount the tools image
sudo su
apt-get install gcc make
mkdir -p /media/cdrom
mount /dev/cdrom /media/cdrom
cd /media/cdrom
./install




sudo -s
editor /etc/hostname
editor /etc/hosts
sudo service hostname restart



# install helpers
sudo apt-get install zsh
wget https://github.com/robbyrussell/oh-my-zsh/raw/master/tools/install.sh -O - | zsh
chsh -s `which zsh`
sudo shutdown -r 0

mkdir -p ~/.oh-my-zsh/custom/themes && wget -O ~/.oh-my-zsh/custom/themes/xxf.zsh-theme https://raw.githubusercontent.com/xfanwu/oh-my-zsh-custom-xxf/master/themes/xxf.zsh-theme

# configure hosts
# remove all 127.0.0.1 entries from hosts file

# start zk
export ZOOCFGDIR=/home/zhenglai/install/zookeeper-3.4.8/conf
/home/zhenglai/install/zookeeper-3.4.8/bin/zkServer.sh start


# start kafka
nohup /home/zhenglai/install/kafka_2.11-0.10.0.0/bin/kafka-server-start.sh /home/zhenglai/install/kafka_2.11-0.10.0.0/config/server.properties > /tmp/kafka.log 2>&1 &


# mysql
sudo -s
apt-get install mysql-server
apt-get install nginx

useradd starfish
usermod -aG sudo starfish

su -s starfish



#/etc/mysql/my.cnf (Mysql 5.5)
#/etc/mysql/conf.d/mysql.cnf (Mysql 5.6+)
# Find bind-address=127.0.0.1 in config file change bind-address=0.0.0.0 (you can set bind address to one of your interface ips or like me use 0.0.0.0)
service mysql restart
mysql -uroot -p
CREATE USER 'starfish'@'localhost' IDENTIFIED BY '~@$Starfish666';
GRANT ALL PRIVILEGES ON * . * TO 'newuser'@'localhost';
FLUSH PRIVILEGES;

CREATE USER 'starfish'@'localhost' IDENTIFIED BY '~@$Starfish666';
CREATE USER 'starfish'@'%' IDENTIFIED BY '~@$Starfish666';
GRANT ALL ON *.* TO 'starfish'@'localhost';
GRANT ALL ON *.* TO 'starfish'@'%';
FLUSH PRIVILEGES;


