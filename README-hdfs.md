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
