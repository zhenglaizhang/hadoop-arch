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

hdfs dfs -cat /data/ncdc/1901