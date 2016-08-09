#!/usr/bin/env bash


# encrypt the PII info
printf '%s' "Zhenglai Zhang" | md5sum


# salting to make it more secure
printf '%s' "Zhenglai Zhang $(date +%sN)" | md5



# null check

# type check

# length check

# range check (numeric or date ranges)
# age or month


# format check
# email, postcode or zip code