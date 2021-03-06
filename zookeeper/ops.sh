#!/usr/bin/env bash


# Group Membership in ZooKeeper
# One way of understanding ZooKeeper is to think of it as providing a high-availability filesystem.


 ./zkServer.sh {start|start-foreground|stop|restart|status|upgrade|print-cmd}

zkServer start

$ ps –ef | grep zookeeper | grep –v grep | awk '{print $2}'
$ ps –ef | grep [z]ookeeper | awk '{print $2}'

\jps

# send the ruok command (“Are you OK?”) to the client port using nc
echo ruok | nc localhost 2181
# imok


echo conf | nc localhost 2181
echo envi | nc localhost 2181
echo srvr | nc localhost 2181
echo stat | nc localhost 2181
echo srst | nc localhost 2181
echo isro | nc localhost 2181
echo dump | nc localhost 2181
echo cons | nc localhost 2181
echo crst | nc localhost 2181
echo wchs | nc localhost 2181
echo wchc | nc localhost 2181
echo wchp | nc localhost 2181
echo mntr | nc localhost 2181


zkCli -server localhost:2181
bin/zkCli.sh -server 127.0.0.1:2181
zkCli
> create /hello "hello zookeeper"
> create /hello2 ""
> delete /hello2
> get /hello
> stat /hello
> ls2 /hello

create -e /ephemeralnode "gone"


[zk: localhost:2181(CONNECTED) 0] create -s /[testsequence]- ""
Created /[testsequence]0000000007
[zk: localhost:2181(CONNECTED) 1] ls /
[hello, ZooKeeper, 中文, zookeeper, config, [testsequence]-0000000007]
[zk: localhost:2181(CONNECTED) 2] create -s -e /anothersequence- ""
Created /anothersequence0000000008
[zk: localhost:2181(CONNECTED) 3] ls /
[ZooKeeper, 中文, zookeeper, hello, config, [testsequence]-0000000007, anothersequence-0000000008]

zkCli.sh -server zoo1:2181,zoo2:2181,zoo3:2181

export CLASSPATH=.:target/classes/:$ZOOKEEPER_HOME/*:$ZOOKEEPER_HOME/lib/*:$ZOOCFGDIR
# demo group membership
java net.zhenglai.zk.CreateGroup localhost zoo
java net.zhenglai.zk.ListGroup localhost zoo
java net.zhenglai.zk.ListGroup localhost zookeeper/quota
java net.zhenglai.zk.JoinGroup localhost zoo duck &
java net.zhenglai.zk.JoinGroup localhost zoo duck &
java net.zhenglai.zk.JoinGroup localhost zoo goat &
goat_pid=$!
# The last line saves the process ID of the Java process running the program that adds goat as a member.

java net.zhenglai.zk.ListGroup localhost zoo
kill $goat_pid
# And a few seconds later, it has disappeared from the group because the process’s Zoo‐
#Keeper session has terminated (the timeout was set to 5 seconds) and its associated
#ephemeral node has been removed:
java net.zhenglai.zk.ListGroup localhost zoo


java net.zhenglai.zk.DeleteGroup localhost zoo
java net.zhenglai.zk.ListGroup localhost zoo
# Group zoo does not exist

# kill another 2 join group jvm instances
\jps

zkCli.sh -server localhost ls /zoo



export CLASSPATH=.:target/classes/:$ZOOKEEPER_HOME/*:$ZOOKEEPER_HOME/lib/*:$ZOOCFGDIR
scala net.zhenglai.zk.ResilientConfigUpdater localhost
scala net.zhenglai.zk.ConfigWatcher localhost

zk> get /config

