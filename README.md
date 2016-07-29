

## Key points

### Map Tasks

* input splits
    * why is a good input split the same as block size?
    * one split maps to one map task
* data locality optimization
    * data-local map task
    * rack-local map task
    * off-rack map task
* map tasks write their output to local disk, not HDFS(with replication)
    * it's the intermediate output, once job is done, it could be thrown away
    * hadoop could re-run the map task on another node to re-create the map output if any failure
* map-only task (zero reduce task) may write output to HDFS directly
* combiner function
    * map local reduce function
    * map output -> combiner function -> reduce function
    * combiner function should be *commutative* and *associative*
    * it may cut down the network transfer between map and reduce tasks
    * optimization only, hadoop doesn't provide a guarantee of how many times it will call it for a particular map output record if any. 
    * always considering if it's possible to apply combiner function to map
    
### Reduce Tasks

* no data locality
* output is written to HDFS for reliability