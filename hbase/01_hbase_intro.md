

## What's HBase (Hadoop Database)

It’s often described as a sparse, distributed, persistent, multidimensional sorted map, which is indexed by rowkey, column key, and timestamp.

* HBase is a Java-based, open source, NoSQL, non-relational, column-oriented, distributed database built on top of the Hadoop Distributed Filesystem (HDFS), modeled after Google’s BigTable paper
* HBase is built to be a fault-tolerant application hosting a few large tables of sparse data (billions/trillions of rows by millions of columns), while allowing for very low latency and near real-time random reads and random writes.
* HBase is designed for **terabytes to petabytes** of data
* HBase was designed with availability over consistency and offers high availability of all its services with a fast and automatic failover.
* Allow creation and usage of a **flexible data model**. **Columns could be created online**
* atomic and strongly consistent row-level operations
* **strong consistency** so clients can see data immediately after it’s written
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

----

The intent to be an online or an offline system influences many technology decisions when implementing an application. HBase is an online system. Its tight integration with Hadoop MapReduce makes it equally capable of offline access as well.

collecting user-interaction data a perfect fit for HBase, and HBase has been successfully used to capture raw clickstream and user-interaction data incrementally and then process it (clean it, enrich it, use it) using different processing mechanisms (MapReduce being one of them).

* It’s metadata that is used to enrich the user’s interaction.


### Installation

* standalone
* pseudo-distributed
* fully distributed

----

HBase uses the table as the top-level structure for storing data.

```bash
create 'mytable', 'cf'
list
# “Put the bytes 'hello HBase' to a cell in 'mytable' in the 'first' row at the 'cf:message' column.”
put 'mytable', 'first', 'cf:message', 'hello HBase'
put 'mytable', 'second', 'cf:foo', 0x0
put 'mytable', 'third', 'cf:bar',3.14159
describe 'mytable'
```

HBase is a **schema-less** database.

```bash
# complement of put, get one single row
get 'mytable', 'first'

# by default, return all rows
scan 'mytable'
```
the order in which HBase returns rows. They’re ordered by the row name; HBase calls this the **rowkey**.

* HBase can store multiple versions of each cell. The default number of versions stored is three
* At read time, only the latest version is returned, unless otherwise specified

```bash
`put 'mytable', 'second', 'cf:bar', 'test cq sorting'`
hbase(main):008:0> scan 'mytable'
ROW                                            COLUMN+CELL                                                                                                                          
 first                                         column=cf:message, timestamp=1470371681656, value=hello hbase                                                                        
 second                                        column=cf:bar, timestamp=1470376565990, value=test cq sorting                                                                        
 second                                        column=cf:foo, timestamp=1470371710055, value=0                                                                                      
 third                                         column=cf:bar, timestamp=1470371740142, value=3.14159                                                                                
3 row(s) in 0.0140 seconds

```
