

## What's HBase (Hadoop Database)

It’s often described as a sparse, distributed, persistent, multidimensional sorted map, which is indexed by rowkey, column key, and timestamp.

* HBase is a Java-based, open source, NoSQL, non-relational, column-oriented, distributed database built on top of the Hadoop Distributed Filesystem (HDFS), modeled after Google’s BigTable paper
* HBase is built to be a fault-tolerant application hosting a few large tables of sparse data (billions/trillions of rows by millions of columns), while allowing for very low latency and near real-time random reads and random writes.
* HBase was designed with availability over consistency and offers high availability of all its services with a fast and automatic failover.
* Allow creation and usage of a **flexible data model**. **Columns could be created online**
* atomic and strongly consistent row-level operations
* a key value store, a column family-oriented database, and sometimes a database storing versioned maps of maps.
* HBase stores **structured and semistructured** data naturally
* It can store unstructured data too, as long as it’s not too large.
* It doesn’t care about types
* Not SQL, not relational, no interrow transactions
* commodity hardware
* scales horizontally
* no node is unique


### Column-Oriented 

* Next generation data tends to be sparse in nature, meaning not all rows are created equal.
* Loosely defined table
* Dynamic column allocation during write
* Great for non-static and evolving data

Real word use cases:

* as an underlying engine for Solr
* for real-time event processing
* as a master data management (MDM) system
* a document store replacement