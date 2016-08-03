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


### Implementation

* standalone mode
* replicated mode
    * ensemble
    * achieve HA through replication
    * majority
    * it is usual to have an odd number of machines in an ensemble.
* all ZK has to do is ensure that every modification to the tree of znodes is replicated to a majority of the ensemble 
* If a minority of the machines fail, then a minimum of one machine will survive with the latest state. The other remaining replicas will eventually catch up with this state.
* Zab Protocol
    * Phase 1: Leader election 
        * leaders 
        * followers 
        * This phase is finished once a majority (or quorum) of followers have synchronized their state with the leader.
    * Phase 2: Atomic broadcast
* If the leader fails, the remaining machines hold another leader election.
* Leader election is very fast, around 200ms
* All machines in the ensemble write updates to disk before updating their in-memory copies of the znode tree. 
* Read requests may be serviced from any machine, and because they involve only a lookup from memory, they are very fast.

* Reads are satisfied by followers, whereas writes are committed by the leader
* A follower may lag the leader by a number of updates

### Constiency

![](.zk_images/zk_rw_flow.png)

* Every update made to the znode tree is given a globally unique identifier, called a `zxid`
* Updates are ordered, so if zxid z1 is less than z2, then z1 happened before z2, according to ZooKeeper (which is the single authority on ordering in the distributed system).

----
- Sequential consistency
    - Updates from any particular client are applied in the order that they are sent 
- Atomicity
    - Updates either succeed or fail   
- Single system image
    - A client will see the same view of the system
    - a server that is behind the one that failed will not accept connections from the client until it has caught up with the failed server. 
- Durability
    - Updates will survive server failures 
- Timeliness
    - The lag in any client’s view of the system is bounded 
    - rather than allow a client to see data that is very stale, a server will shut down, forcing the client to switch to a more up-to-date server.
- For performance reasons, reads are satisfied from a ZooKeeper server’s memory and do not participate in the global ordering of writes
    - the `sync` operation is available only as an asynchronous call. This is because you don’t need to wait for it to return, since ZooKeeper guarantees that any subsequent operation will happen after the sync completes on the server 



