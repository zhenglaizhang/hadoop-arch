#!/usr/bin/env bash


export HADOOP_CONF_DIR=/home/zhenglai/hadoop/etc/hadoop
./bin/spark-submit --class org.apache.spark.examples.SparkPi --master yarn --deploy-mode cluster examples/jars/spark-examples_2.11-2.0.0.jar 1000