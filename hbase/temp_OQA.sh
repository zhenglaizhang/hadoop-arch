#!/usr/bin/env bash


hbase(main):001:0> create 'stations', {NAME => 'info'}
hbase(main):002:0> create 'observations', {NAME => 'data'}


# Reversed Timestamps
# The technique involves appending (Long.MAX_VALUE - timestamp) to the end of any key, e.g., [key][reverse_timestamp]
# The most recent value for [key] in a table can be found by performing a Scan for [key] and obtaining the first record. Since HBase keys are in sorted order, this key sorts before any older row-keys for [key] and thus is first.
# It’s much more efficient to get the first n rows, then exit the scanner (this is sometimes called an “earlyout” scenario).


# HBase 0.98 added the ability to do reverse scans
# Reverse scans are a few percent slower than forward scans.