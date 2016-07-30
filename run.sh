#!/usr/bin/env bash
export HADOOP_CLASSPATH=target/hadoop-arch-1.0-SNAPSHOT.jar
hadoop net.zhenglai.MaxTemperature /data/ncdc/1901 /data/ncdc/output


hadoop net.zhenglai.lib.URLCat hdfs://localhost:9000/user/Zhenglai/quangle.txt

# or
export HADOOP_CLASSPATH=target/classes/