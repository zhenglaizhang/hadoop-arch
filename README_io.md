

## Data Integrity

* because every I/O operation on the disk or network carries with it a small chance of introducing errors into the data that it is reading or writing, when the volumes of data flowing through the system are as large as the ones Hadoop is capable of handling, the chance of data corruption occurring is high.
* The usual way of detecting corrupted data is by computing a checksum for the data when it first enters the system, and again whenever it is transmitted across a channel that is unreliable and hence capable of corrupting the data.
* it is **merely error detection**. (And this is a reason for not using low-end hardware; in particular, be sure to use ECC memory
* it is possible that it’s the checksum that is corrupt, not the data, but this is very unlikely, because the checksum is much smaller than the data.
* CRC-32 (32-bit cyclic redundancy check), which computes a 32-bit integer checksum for input of any size


## Data Integrity in HDFS

* HDFS transparently checksums all data written to it and by default verifies checksums when reading data.
* A separate checksum is created for every `dfs.bytes-per-checksum` bytes of data. 
* The default is 512 bytes, and because a CRC-32C checksum is 4 bytes long, the storage overhead is less than 1%
* Datanodes are responsible for verifying the data they receive before storing the data and its checks
* A client writing data sends it to a pipeline of datanodes (as explained in Chapter 3), and **the last datanode in the pipeline verifies the checksum.** If the datanode detects an error, the client receives a subclass of IOException, which it should handle in an application-specific manner (for example, by retrying the operation).
* When clients read data from datanodes, they verify checksums as well, comparing them with the ones stored at the datanodes.
* When a client successfully verifies a block, it tells the datanode, which updates its log. Keeping statistics such as these is valuable in detecting bad disks.
* each datanode runs a **DataBlockScanner** in a background thread
* if a client detects an error when reading a block, it reports the bad block and the datanode it was trying to read from to the namenode before throwing a `ChecksumException`. Namenode then schedules a copy of the block to be replicated on another datanode, so its **replication factor** is back at the expected level. Once this has happened, the corrupt replica is deleted.
* We can also disable the checksum.
    * `FileSystem.setVerifyChecksum()`
    * `hdfs dfs -get -ignoreCrc ...`
* `hadoop fs -checksum` to check if 2 files are of same content, which `distcp` does



## Compression

* `bzip2` are splitable
* `LZO` files are splittable if they have been indexed in a preprocessing step
* **Splitable**: the compression format supports splitting (that is, whether you can seek to any point in the stream and start reading from some point further on)
* A **codec** is the implementation of a compression-decompression algorithm