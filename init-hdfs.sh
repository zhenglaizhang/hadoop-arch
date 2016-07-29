hdfs namenode -format
hdfs dfs -ls /
hdfs dfs -mkdir -p /user/Zhenglai
hdfs dfs -mkdir -p /etc
hdfs dfs -mkdir -p /tmp
hdfs dfs -mkdir -p /data
hdfs dfs -mkdir -p /app
hdfs dfs -mkdir -p /metadata
hdfs dfs -mkdir -p /data/ncdc

hdfs dfs -put 1901 /data/ncdc
hdfs dfs -put 1902 /data/ncdc

hdfs dfs -cat /data/ncdc/1901


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
