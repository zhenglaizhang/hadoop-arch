# Design 

It's a 

* a file system
* storing very large files
* with streaming data access patterns
    * deliver high throughput of data, at the expense of latency
    * write-once, read-many-times
    * time to read the whole data set is more important than the latency in reading the first record
* run on clusters of commodity hardware
    * though the chance of node failure across the cluster is high
    * fault tolerance

It's **NOT GOOD** for

* low-latency data access
    * HBase is good here
* lots of small files
    * namenode stores the filesystem metadata in memory
    * each file, directory, and block takes ~150bytes in namenode
    * too many map tasks are created for each small files
* multiple writers, arbitrary file modications
    * files in HDFS may be written to by a single writer. Writes are always made at the end of the file, in append-only fashion 