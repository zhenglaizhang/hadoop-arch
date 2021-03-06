#!/usr/bin/env bash
hdfs namenode -format
hdfs dfs -ls /
hdfs dfs -mkdir -p /user/zhenglai
hdfs dfs -mkdir -p /etc
hdfs dfs -mkdir -p /tmp
hdfs dfs -mkdir -p /data
hdfs dfs -mkdir -p /app
hdfs dfs -mkdir -p /metadata
hdfs dfs -mkdir -p /data/ncdc

hdfs dfs -put 1901 /data/ncdc
hdfs dfs -put 1902 /data/ncdc
hdfs dfs -put 1901.gz /data/ncdc/

hdfs dfs -cat /data/ncdc/1901


echo "to be or not to be" > words
hdfs dfs -copyFromLocal words /user/zhenglai/words


# verify hadoop startup
jps

# testing
# $ hadoop jar /usr/local/Cellar/hadoop/2.3.0/libexec/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.3.0.jar pi 2 5


# preparing more data
hadoop fs -copyFromLocal input/docs/quangle.txt hdfs://localhost:9000/data/quangle.txt
hadoop fs -copyFromLocal input/docs/1400-8.txt /user/zhenglai/1400-8.txt
# hadoop fs -copyFromLocal input/docs/1400-8.txt 1400-8.txt
hadoop fs -copyFromLocal input/docs/quangle.txt quangle.txt

hadoop fs -copyToLocal /data/quangle.txt /tmp/quangle.txt
md5 /tmp/quangle.txt

hadoop fs -mkdir books
hadoop fs -ls .

# The second column is the replication factor of the file


# checksum
hadoop fs -checksum /data/ncdc/1901
# 16/07/29 22:12:24 WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using
# builtin-java classes where applicable
# /data/ncdc/1901 MD5-of-0MD5-of-512CRC32C        000002000000000000000000a35b0f71f7c5fbaebfc63e694f4fc516


# -text understand plain text, gzipeed, sequence file, and avro data file
hdfs dfs -text numbers.seq | head


export HADOOP_HOME=/usr/local/Cellar/hadoop/2.7.2/libexec

hadoop jar \
$HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-*.jar \
sort -r 1 \
-inFormat org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat \
-outFormat org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat \
-outKey org.apache.hadoop.io.IntWritable \
-outValue org.apache.hadoop.io.Text \
numbers.seq sorted

hadoop fs -text sorted/part-r-00000 | head




hdfs dfsadmin -safemode enter
hdfs dfsadmin -safemode get
hdfs dfsadmin -safemode wait    # wait to exit safe mode, helpful in script
hdfs dfsadmin -safemode leave
hdfs dfsadmin -safemode get


# enable auditing
export HDFS_AUDIT_LOGGER="INFO,RFAAUDIT"
# A log line is written to the audit log (hdfs-audit.log) for every HDFS event.


hdfs fsck /
