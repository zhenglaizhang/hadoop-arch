#!/usr/bin/env bash

mkdir /tmp/spooldir
flume-ng agent \
--conf-file src/main/resources/flume/spool-to-logger.properties \
# agent name must also be passed in with --name (since a Flume properties file can define several agents
--name agent1 \
--conf  $FLUME_HOME/conf \
-Dflume.root.logger=INFO,console


# The spooling directory source ingests the file by splitting it into lines and creating a Flume event for each line

echo "Hello Flume" > /tmp/spooldir/.file1.txt
mv /tmp/spooldir/.file1.txt /tmp/spooldir/file1.txt





flume-ng agent --conf-file spool-to-hdfs-tiered.properties --name agent1 ...
flume-ng agent --conf-file spool-to-hdfs-tiered.properties --name agent2 ...
