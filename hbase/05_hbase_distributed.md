
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
    * it’s about overall processing time of the task, not the time of an individual unit.
