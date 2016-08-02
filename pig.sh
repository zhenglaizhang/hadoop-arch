#!/usr/bin/env bash

echo $PIG_HOME
pig -help

# In local mode, Pig runs in a single JVM and accesses the local filesystem.
#     -x, -exectype - Set execution mode: local|mapreduce|tez, default is mapreduce.
pig -x local

# stop timestamps from being logged, MapReduce is the default mode
pig -brief


# auto-local mode
# which is an optimization that runs small jobs locally if the input is less than 100 MB
#   no more than one reducer is being used.


# Script
pig script.pig
pig -e "script code"

# Grunt
# possible to run Pig scripts from within Grunt using run and exec
# > run
# > exec

# Embedded
#   PigServer & PigRunner


pig -x local pig/max_temp.pig

exec pig/max_temp.pig

ILLUSTRATE max_temp

