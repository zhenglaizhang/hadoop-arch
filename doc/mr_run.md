
## Map reduce running

* For a start, a job’s classes must be packaged into a job JAR file to send to the cluster. Hadoop will find the job JAR automatically by searching for the JAR on the driver’s classpath that contains the class set in the `setJarByClass()` method (on JobConf or Job).
* Alternatively, if you want to set an explicit JAR file by its file path, you can use the setJar() method. (The JAR file path may be local or an HDFS file path.)
* `HADOOP_CLASSPATH` is a client-side setting and only sets the classpath for the driver JVM, which submits the job.

### Task classpath precedence

* On the client side, you can force Hadoop to put the user classpath first in the search order by setting the `HADOOP_USER_CLASSPATH_FIRST` environment variable to true. 
* For the task classpath, you can set `mapreduce.job.user.classpath.first` to true.


### Application, Job, Task, and Task Attempt IDs

* In Hadoop 2, MapReduce job IDs are generated from YARN application IDs that are created by the YARN resource manager: 
    * `application_1410450250506_0003`
    * `job_1410450250506_0003`
    * `task_1410450250506_0003_m_000003`
    * `attempt_1410450250506_0003_m_000003_0`
* Tasks may be executed more than once, due to failure or speculative execution, identify different instances of a task execution, task attempts are given unique IDs.
* The task IDs are created for a job when it is initialized, so they do not necessarily dictate the order in which the tasks will be executed.
* Task attempts are allocated during the job run as needed, so their ordering represents the order in which they were created to run.
* Each reducer produces one output file, e.g. 30 reducers might produce 30 part files named part-r-00000 to partr-00029 in the max-temp directory.
* reduce output partitions are unordered (owing to the hash partition function). Doing a bit of postprocessing of data from MapReduce is very common,


## Debugging

### Counter

* you should always ask yourself if you can use a counter to get the information you need to find out what’s happening. Even if you need to use logging or a status message, it may be useful to use a counter to gauge the extent of the problem


### MapReduce Workflow

* When the processing gets more complex, this complexity is generally manifested by having more MapReduce jobs, rather than having more complex map and reduce functions. In other words, as a rule of thumb, **think about adding more jobs, rather than adding complexity to jobs**.