#!/usr/bin/env bash


export CLASSPATH=.:target/classes/:$ZOOKEEPER_HOME/*:$ZOOKEEPER_HOME/lib/*:$ZOOCFGDIR:~/.m2/repository/org/apache/kafka/kafka_2.11/0.10.0.0/kafka_2.11-0.10.0.0.jar:~/.m2/repository/org/apache/kafka/kafka-clients/0.10.0.0/kafka-clients-0.10.0.0.jar:~/.m2/repository/com/yammer/metrics/metrics-core/2.2.0/metrics-core-2.2.0.jar

scala -cp $CLASSPATH net.zhenglai.kafka.SimpleCounter vm01:9092,vm02:9092,vm03:9092 simplecounter new async 5 100