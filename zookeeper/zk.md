# The ZooKeeper Coordination Service

## Data Model

* ZooKeeper maintains a hierarchical tree of nodes called **znodes**. 
* A znode stores **data** and has an associated **ACL**.
* there is a limit of **1 MB** on the amount of data that may be stored in any znode

* Data access is atomic.
* client reading the data stored in a znode will never receive only some of the data;
    * either the data will be delivered in its entirety 
    * or the read will fail
* a write will replace all the data associated with a znode. 
    * ZooKeeper guarantees that the write will either succeed or fail;
* ZooKeeper does not support an append operation.
* Znodes are referenced by paths
* Paths are canonical
    * In ZooKeeper, “.” does not have this special meaning and is actually illegal as a path component
    
### Ephemeral znodes

* uses ephemeral znodes to implement a group membership service


### Sequence numbers

* Sequence numbers can be used to impose a global ordering on events in a distributed system and may be used by the client to infer the ordering
* use sequential znodes to build a shared lock


### Watches

* Watches allow clients to get notifications when a znode changes in some way
* Watchers are triggered only once
* We can build a configuration service to demonstrate how to use watches to update configuration across a cluster


### Operations

* A delete or setData operation has to specify the version number of the znode that is being updated (which is found from a previous exists call)
* Updates are a nonblocking operation
* ZooKeeper can be viewed as a filesystem, files are small and are written and read in their entirety, there is no need to provide open, close, or seek operations
* writes in ZooKeeper are atomic, and a successful write operation is guaranteed to have been written to persistent storage on a majority of ZooKeeper servers. 
* However, it is permissible for reads to lag the latest state of the ZooKeeper service, and the `sync` operation exists to allow a client to bring itself up to date.


### Multiupdate

* `multi`, that batches together multiple primitive operations into a single unit that either succeeds or fails in its entirety.
* maintain some global invariant


### Watch triggers

* The read operations exists, getChildren, and getData may have watches set on them,
* the watches are triggered by write operations: create, delete, and setData. 
* ACL operations do not participate in watches

### ACLs

Clients may authenticate themselves after establishing a ZooKeeper session

* digest    (username + password)
* sasl      (kerberos)
    `zk.addAuthInfo("digest", "tom:secret".getBytes());`
* ip




