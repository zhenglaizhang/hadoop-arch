
## Apache Kafka publisher-subscriber messaging system

* **Topic**: A topic is **a category or feed name** to which messages are published by the message producers. In Kafka, **topics are partitioned** and each partition is represented by the **ordered immutable sequence of messages**. A Kafka cluster maintains the partitioned log for each topic. Each message in the partition is assigned **a unique sequential ID called the offset**.
* **Broker**: _A Kafka cluster consists of one or more servers where each one may have one or more server processes running and is called the broker_. Topics are created within the context of broker processes.
* **Zookeeper**: ZooKeeper serves as the coordination interface between the Kafka broker and consumers.
    * “ZooKeeper allows distributed processes to coordinate with each other through a shared hierarchical name space of data registers (we call these registers znodes), much like a file system.” 
    * Different from znode can have data associated with it and znodes are limited to the amount of data that they can have. ZooKeeper was designed to store coordination data: status information, configuration, location information, and so on
* **Producers**: Producers publish data to the topics by choosing the appropriate partition within the topic. For load balancing, the allocation of messages to the topic partition can be done in **a round-robin fashion or using a custom defined function**.
* **Consumer**: Consumers are the applications or processes that subscribe to topics and process the feed of published messages. 

![](.kafka-intro_images/we_saw_world.png)

![](.kafka-intro_images/customer_see_world.png)


## The World with Kafka

![](.kafka-intro_images/world_with_kafka.png)

* Kafka provides a fast, distributed, highly scalable, highly available, publish-subscribe messaging system
* In turns this solves part of a much harder problem
* Communication and integration between components of large software systems


### What's unique about Kafka?

* large number of consumers
* Ad-hoc consumers
* Batch consumers (daily/hourly jobs are ok)
* Automatic recovery from **broker** failures


### What do we do with Kafka?

* Messaging - communicating between apps
* Website Activity Tracking (clicks, searches...)
* Metrics collection - instead of writing to logs
* Audit
* Source and target stream processing

### What Kafka Doesn't Do?

* Its not end-user solution. You need to write code to use it
* Not a drop-in JMS replacement
* It doesn't have many ready-made producers and consumers
* No data transformations
* No encryption, authorization or authentication (yet)

### The Basics

* Messages are organized into **topics**
* **Producers** push messages
* **Consumers** pull messages
* Kafka runs in a cluster, Nodes are called **brokers**

`topic -> partition -> unique offset in the partition`

![](.kafka-intro_images/5f608116.png)

### Each partition as log

![](.kafka-intro_images/partition_as_log.png)

* Retention based on TTL 

### Each Broker has many partitions

* Producers load balance between partitions (leaders)

![](.kafka-intro_images/broker_partitions.png)

* Orange one is the leader, all reads and writes always goes to the leader, blues one are replications in case of leader failures
* Leader replicates to folloers

![](.kafka-intro_images/leader_follower.png)


### Consumers

![](.kafka-intro_images/consumers.png)

* Consumers in one consumer group get different set of data from brokers
* Lots of partitions allows add more consumers
* Data is replicated to different consumer groups (ML + OLAP target should belongs 2 different consumer groups)

