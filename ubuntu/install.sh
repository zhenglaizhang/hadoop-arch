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

