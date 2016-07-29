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
    
    
### Blocks

* Filesystem blocks are typically a few kilobytes in size, whereas disk blocks are normally 512 bytes. This is generally transparent to the
  filesystem user who is simply reading or writing a file of
* Unlike a filesystem for a single disk, a file in HDFS that is smaller than a single block does not occupy a full block’s worth of underlying
  storage
* HDFS blocks are large compared to disk blocks, and the reason is to minimize the cost of seeks.
* some applications may choose to set a high replication factor for the blocks in a popular file to spread the read load on the cluster

```bash
hdfs fsck / -files -blocks
```

### Namenodes and Datanodes

* master-worker patterns
* a namenode (master) and a number of datanodes (workers)

#### Namenode

* maintain the filesystem namespace
    * filesystem tree
    * metadata for all the files and directories in the tree
* the namespace image and the edit log
* it does not store block locations persistently, because this information is reconstructed from datanodes when the system starts.
* without the namenode, the filesystem cannot be used, it is important to make the namenode resilient to failure.
* secondary namenode, which despite its name does not act as a namenode. Its main role is to periodically merge the namespace image with the edit log to prevent the edit log from becoming too large.
* The usual course of action in this case is to copy the namenode’s metadata files that are on NFS to the secondary and run it as the new
  primary. (Note that it is possible to run a hot standby namenode instead of a secondary
* directories are treated as metadata and stored by the namenode, not the datanodes

#### Datanode
* They store and retrieve blocks when
  they are told to (by clients or the namenode), and they report back to the namenode
  periodically with lists of blocks that they are storing.
  
#### Off-heap Block Caching

* by default, a block is cached in only one datanode’s memory's off-heap block cache, although the number is configurable on a per-file basis. Job schedulers (for MapReduce, Spark, and other frameworks)
can take advantage of cached blocks by running tasks on the datanode where a block is cached, for increased read performance. 
* A small lookup table used in a join is a good candidate for caching.
* Users or applications instruct the namenode which files to cache (and for how long) by **adding a cache directive to a cache pool.**


#### HDFS Federation

* HDFS federation allows a cluster to scale by adding namenodes, each of which manages a portion of the filesystem namespace
* Under federation, each namenode manages a namespace volume, which is made up of the metadata for the namespace, 
* and a block pool containing all the blocks for the files in the namespace.


#### HDFS High Availability

* replicating namenode metadata on multiple filesystems (local & NFS ...)
* using the secondary namenode to create checkpoints
* The namenode is still a single point of failure (SPOF) since the namenode is the sole repository of the metadata and the file-to-block mapping

The new namenode is not able to serve requests until it has 

* loaded its namespace image into memory, 
* replayed its edit log, and
* enough block reports from the datanodes to leave safe mode.

A large cluster's namenode's start from cold can be 30 minutes

The long recovery time is a problem for routine maintenance

Hadoop 2 remedied this situation by adding support for HDFS high availability (HA). There are a pair of namenodes in an active-standby configuration.
  
* The namenodes must use highly available shared storage to share the edit log
* Datanodes must send block reports to both namenodes
* Clients must be configured to handle namenode failover
* The secondary namenode’s role is subsumed by the standby, which takes periodic checkpoints of the active namenode’s namespace.

* HDFS HA does use ZooKeeper for electing the active namenode
* the system needs to be conservative in deciding that the active namenode has failed, so the failover time might be longer, around 1min or so

##### failover controller

* the default implementation uses ZooKeeper to ensure that only one namenode is active.
* Each namenode runs a lightweight failover controller process whose job it is to monitor its namenode for failures (using a simple heartbeating mechanism) and trigger a failover should a namenode fail.

**graceful failover**: Failover may also be initiated manually by an administrator, for example, in the case of routine maintenance.

**ungraceful failover**:  it is impossible to be sure that the failed namenode has stopped running.

* a slow network or a network partition can trigger a failover transition

**fencing**:  ensure that the previously active namenode is prevented from doing any damage and causing corruption

* The QJM only allows one namenode to write to the edit log at one time

Client failover: The HDFS URI uses a logical hostname that is mapped to a pair of namenode addresses (in the configuration file), and the client library tries each namenode address until the operation succeeds.


Compare output of following to see hdfs implementations

```bash
hadoop fs -ls hdfs:///
hadoop fs -ls file:///
```
It's possible (and sometimes very convenient) to run MapReduce programs that access any of these filesystems, but for large data set, the distributed HDFS is much better


### Read flow

* `DistributedFileSystem` and `FSDataInputStream`
* the client contacts datanodes directly to retrieve data and is guided by the namenode to the best datanode for each block
* the data traffic is spread across all the datanodes in the cluster
* the namenode merely has to service block location requests (which it stores in memory, making them very efficient) and does not

### Write flow
* `DistributedFileSystem` and `FSDataOutputStream`
* `FSDataOutputStream` wraps a `DFSOutputStream`, which handles communication with the datanodes and namenode
* data queue is consumed by `DataStreamer`
* The list of datanodes forms a pipeline of size of replication factor
* ack queue
* As long as `dfs.namenode.replication.min` replicas (which defaults to 1) are written, the write will succeed
* Namenode finally waits for blocks to be minimally replicated before returning successfully

this strategy gives a good balance among reliability (blocks are stored on two racks), write bandwidth (writes only have to traverse a single network switch), read performance (there’s a choice of two racks to read from), and block distribution across the cluster (clients only write a single block on the local rack).


### Replica placement

* There’s a tradeoff between reliability(redundancy) and write bandwidth and read bandwidth here.
* Hadoop’s default strategy is to place the first replica on the same node as the client (for clients running outside the cluster, a node is chosen at random, although the system tries not to pick nodes that are too full or too busy
* The second replica is placed on a different rack from the first (off-rack), chosen at random. 
* The third replica is placed on the same rack as the second, but on a different node chosen at random. 
* Further replicas are placed on random nodes in the cluster, although the system tries to avoid placing too many replicas on the same rack.
* Once the replica locations have been chosen, a pipeline is built

### Network topolicy

* Hadoop takes a simple approach in which the network is represented as a tree and the distance between two nodes is the sum of their distances to their closest common ancestor.
* By default, though, it assumes that the network is flat—a singlelevel hierarchy 

### Coherency model

* A coherency model for a filesystem describes the data visibility of reads and writes for a file. HDFS trades off some POSIX requirements for performance, so some operations
  may behave
* After creating a file, it is visible in the filesystem namespace 
* However, any content written to the file is not guaranteed to be visible, even if the stream is flushed
* Once more than a block’s worth of data has been written, the first block will be visible to new readers
* it is always the current block being written that is not visible to other readers.
* HDFS provides a way to force all buffers to be flushed to the datanodes via the hflush() method on FSDataOutputStream.
* Note that hflush() does not guarantee that the datanodes have written the data to disk, only that it’s in the datanodes’ memory
* For this stronger guarantee, use hsync() instead.
* Closing a file in HDFS performs an implicit hflush(), too:


* With no calls to hflush() or hsync(), you should be prepared to lose up to a block of data in the event of client or system failure.
* For many applications, this is unacceptable, so you should call hflush() at suitable points, such as after writing a certain number of records or number of bytes.
* suitable values can be selected after measuring your application’s performance with different hflush() (or hsync()) frequencies