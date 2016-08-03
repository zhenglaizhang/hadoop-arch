#!/usr/bin/env bash


# Group Membership in ZooKeeper
# One way of understanding ZooKeeper is to think of it as providing a high-availability filesystem.


zkServer start

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


bin/zkCli.sh -server 127.0.0.1:2181
zkCli



# demo group membership
% java CreateGroup localhost zoo
% java ListGroup localhost zoo
% java JoinGroup localhost zoo duck &
% java JoinGroup localhost zoo duck &
% java JoinGroup localhost zoo goat &
% goat_pid=$!
# The last line saves the process ID of the Java process running the program that adds goat as a member.

% java ListGroup localhost zoo
% kill $goat_pid
# And a few seconds later, it has disappeared from the group because the process’s Zoo‐
#Keeper session has terminated (the timeout was set to 5 seconds) and its associated
#ephemeral node has been removed:
% java ListGroup localhost zoo


% java DeleteGroup localhost zoo
% java ListGroup localhost zoo
# Group zoo does not exist

% zkCli.sh -server localhost ls /zoo

