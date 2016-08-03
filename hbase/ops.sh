#!/usr/bin/env bash


start-hbase.sh

hbase shell
# This will bring up a JRuby IRB interpreter that has had some HBase-specific commands added to it.
> help



# create test table with data cf
> create 'test', 'data'

# Check the master logs under the HBase logs directory—the default location for the logs directory is ${HBASE_HOME}/logs

hbase(main):002:0> list

hbase(main):003:0> put 'test', 'row1', 'data:1', 'value1'
hbase(main):004:0> put 'test', 'row2', 'data:2', 'value2'
hbase(main):005:0> put 'test', 'row3', 'data:3', 'value3'
hbase(main):006:0> get 'test', 'row1'


hbase(main):007:0> scan 'test'


hbase(main):012:0> put 'test', 'row4', 'data:1', 'value4'
hbase(main):013:0> scan 'test'

# Notice how we added three new columns without changing the schema.


# To remove the table, you must first disable it before dropping it:

hbase(main):009:0> disable 'test'
hbase(main):010:0> drop 'test'
hbase(main):011:0> list

# stop hbase
% stop-hbase.sh



export HBASE_CLASSPATH=target/hadoop-arch.jar
hbase net.zhenglai.hbase.ExampleClientOldApi