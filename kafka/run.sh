#!/usr/bin/env bash


export CLASSPATH=.:target/classes/:$ZOOKEEPER_HOME/*:$ZOOKEEPER_HOME/lib/*:$ZOOCFGDIR:~/.m2/repository/org/apache/kafka/kafka_2.11/0.10.0.0/kafka_2.11-0.10.0.0.jar:~/.m2/repository/org/apache/kafka/kafka-clients/0.10.0.0/kafka-clients-0.10.0.0.jar:~/.m2/repository/com/yammer/metrics/metrics-core/2.2.0/metrics-core-2.2.0.jar

scala -cp $CLASSPATH net.zhenglai.kafka.SimpleCounter vm01:9092,vm02:9092,vm03:9092 simplecounter new async 5 100


# another tmux panel
kafka-console-consumer --zookeeper vm01:2181,vm02:2181,vm03:2181 --topic simplecounter


# 5230ms
scala -cp $CLASSPATH net.zhenglai.kafka.SimpleCounter vm01:9092,vm02:9092,vm03:9092 simplecounter old sync 500 10

# 5040ms
scala -cp $CLASSPATH net.zhenglai.kafka.SimpleCounter vm01:9092,vm02:9092,vm03:9092 simplecounter old async 500 10



kafka-topics --zookeeper vm01:2181 --describe
export CLASSPATH=.:target/classes/:$ZOOKEEPER_HOME/*:$ZOOKEEPER_HOME/lib/*:$ZOOCFGDIR:~/.m2/repository/org/apache/kafka/kafka_2.11/0.10.0.0/kafka_2.11-0.10.0.0.jar:~/.m2/repository/org/apache/kafka/kafka-clients/0.10.0.0/kafka-clients-0.10.0.0.jar:~/.m2/repository/com/yammer/metrics/metrics-core/2.2.0/metrics-core-2.2.0.jar:~/.m2/repository/com/101tec/zkclient/0.8/zkclient-0.8.jar:~/.m2/repository/commons-collections/commons-collections/3.2.2/commons-collections-3.2.2.jar
scala -cp $CLASSPATH net.zhenglai.kafka.MovingAvgZkConsumer vm01:2181,vm02:2181,vm03:2181 1 simplecounter 3 120000

kill %2


kafka-run-class kafka.tools.ConsumerOffsetChecker --zookeeper vm01:2181,vm02:2181,vm03:2181 --broker-info --group 1
# output as below
[2016-08-08 02:27:59,687] WARN WARNING: ConsumerOffsetChecker is deprecated and will be dropped in releases following 0.9.0. Use ConsumerGroupCommand instead. (kafka.tools.ConsumerOffsetChecker$)
Group           Topic                          Pid Offset          logSize         Lag             Owner
1               simplecounter                  0   193             193             0               none
BROKER INFO
0 -> vm01.shared:9092
