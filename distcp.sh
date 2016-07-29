
# distcp is implemented as a MapReduce job where the work of copying is done by the maps that run in parallel across the cluster

# hadoop fs -cp copies the file via the client running the command.
hadoop fs -cp file1 file2

hadoop distcp file1 file2

# copy to dir2/dir1 if dir2 exits
hadoop distcp dir1 dir2

# override if dir2 exits
hadoop distcp -override dir1 dir2

# sync the change
# If we changed a file in the dir1 subtree, we could synchronize the change with dir2 by running
hadoop distcp -update dir1 dir2

hadoop distcp -update /user/zhenglai /user/Zhenglai
hadoop fs -ls /user/Zhenglai/



# A very common use case for distcp is for transferring data between two HDFS clusters.
hadoop distcp -update -delete -p hdfs://namenode1/foo hdfs://namenode2/foo

# If the two clusters are running incompatible versions of HDFS, then you can use the webhdfs protocol to distcp between them
hadoop distcp webhdfs://namenode1:50070/foo webhdfs://namenode2:50070/foo
