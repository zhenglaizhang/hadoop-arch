
Hadoop MapReduce provides a distributed computation framework for highthroughput data access. The Hadoop Distributed File System (HDFS) gives HBase a storage layer providing availability and reliability.

## A case for MapReduce

### Online operations 

* online operations. You expect every Get and Put to return results in milliseconds 
* The twits table’s rowkey is designed to maximize physical data locality and minimize the time spent scanning records.

### Offline operations

* monthly site traffic summary report
* Offline operations have performance concerns as well. Instead of focusing on individual request latency, these concerns often focus on the **entire computation in aggregate**.

### Latency vs. throughput

* OLTP vs OLAP
* Online vs Offline
* Making the shopping list results in higher throughput
* Online systems focus on minimizing the time it takes to access one piece of data—the round trip
    * Response latency measured on the 95th percentile is generally the most important metric for online performance
    * if someone was in the 95th precentile in height they'd be taller than 95% of people and shorter than 5%. you get the idea..
* Offline systems are optimized for access in the aggregate, processing as much as we can all at once in order to maximize throughput.
    * report their performance in number of units processed per second. Those units might be requests, records, or megabytes. 
    * it’s about overall processing time of the task, not the time of an
      individual unit. 
* Serial execution has limited throughput
* Improved throughput with parallel execution


#### MapReduce: maximum throughput with distributed parallelism

* There are no split calculations, no Futures to track, and no thread pool to clean up after
* HBase provides **`TableMapper`** and **`TableReducer`** classes to help with that.



### Splitting and distributing big tables

* Tables in HBase can scale to billions of rows and millions of columns.
* These smaller chunks are called regions. Servers that host regions are called RegionServers.
* RegionServers are typically collocated with HDFS DataNodes (figure 3.7) on the same physical hardware, although that’s not a requirement. 
* The only requirement is that RegionServers should be able to access HDFS
* The master process does the distribution of regions among RegionServers, and each RegionServer typically hosts multiple regions

![](.05_hbase_distributed_images/regionserver_datanode_collocate.png)

* By physically collocating DataNodes and RegionServers, you can use the data locality property; that is, RegionServers can theoretically read and write to the local DataNode as the primary DataNode
* Any RegionServer can host any region.
* Given that the underlying data is stored in HDFS, which is available to all clients as a single namespace, all RegionServers have access to the same persisted files in the file system and can therefore host any region


### How do I find my region

* Region assignment happens when regions split (as they grow in size), when RegionServers die, or when new RegionServers are added to the deployment
* **`-ROOT-`** never splits into more than one region. **`.META.`** behaves like all other tables and can split into as many regions as required
* Think of this like a distributed B+Tree of height 3
* The entry point for an HBase system is provided by another system called ZooKeeper

![](.05_hbase_distributed_images/root_meta_usertable.png)


### Sample -ROOT-, .META. and User table layout

![](.05_hbase_distributed_images/sample_root_meta_usert.png)


#### How client interacts with HBase

![](.05_hbase_distributed_images/client_interacts.png)