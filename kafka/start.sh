#!/usr/bin/env bash


# Run the kafka-server-start.sh script using nohup to start the Kafka server (also called Kafka broker) as a background process that is independent of your shell session.
nohup /home/zhenglai/install/kafka_2.11-0.10.0.0/bin/kafka-server-start.sh /home/zhenglai/install/kafka_2.11-0.10.0.0/config/server.properties > /tmp/kafka.log 2>&1 &



# mac host

# create topic with default settings
echo "Hello, World" | kafka-console-producer --broker-list vm01:9092,vm02:9092,vm03:9092 --topic hellotopic > /dev/null

# --from-beginning tells to read all messages from begging,
# otherwise only the new message will be read
kafka-console-consumer --zookeeper vm01:2181,vm02:2181,vm03:2181 --topic hellotopic --from-beginning

# KafkaT is a handy little tool from Airbnb
gem install kafkat


kafka-topics --zookeeper vm01:2181 --create  --replication-factor 3 --partitions 5 --topic customtopic
kafka-topics --zookeeper vm01:2181 --describe --topic customtopic
kafka-topics --zookeeper vm01:2181 --list
