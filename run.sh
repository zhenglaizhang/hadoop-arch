#!/usr/bin/env bash
export HADOOP_CLASSPATH=target/hadoop-arch-1.0-SNAPSHOT.jar
hadoop net.zhenglai.MaxTemperature /data/ncdc/1901 /data/ncdc/output


hadoop net.zhenglai.lib.URLCat hdfs://localhost:9000/user/Zhenglai/quangle.txt

# or
export HADOOP_CLASSPATH=target/classes/


# package job jar
mvn package -DskipTests


# run with jar directive, we might need to unset HADOOP_CLASSPATH
hadoop jar target/hadoop-arch.jar net.zhenglai.maxtemp.MaxTemperatureDriver /data/ncdc/1901.gz /tmp/max-output
hdfs dfs -cat /tmp/max-output/part-r-00000


hadoop fs -getmerge /tmp/max-output /tmp/max-output-local
sort /tmp/max-output-local | tail

hadoop fs -cat /tmp/max-output/*


# turn on hadoop jvm internal debug logging
HADOOP_ROOT_LOGGER=DEBUG,console hadoop fs -text /foo/bar