#!/usr/bin/env bash


start-hbase.sh
hbase shell

> zk_dump

> scan '-ROOT-'
# deprecated

> scan 'hbase:meta'
# ERROR: .META. no longer exists. The table has been renamed to hbase:meta


