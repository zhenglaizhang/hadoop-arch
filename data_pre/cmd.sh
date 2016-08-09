#!/usr/bin/env bash


# encrypt the PII info
printf '%s' "Zhenglai Zhang" | md5sum


# salting to make it more secure
printf '%s' "Zhenglai Zhang $(date +%sN)" | md5