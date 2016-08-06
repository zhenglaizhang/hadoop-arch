


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