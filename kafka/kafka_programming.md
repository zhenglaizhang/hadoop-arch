
## Old producers

* Sync - safe but slow ( know when to retry)
* Async - high performance, but no notification on errors, (keep writing, hope
  it works, for fast system)

## New producer

* Can be used synchronously or asynchronously (Future with callback)
* Both modes can handle errors
* Bounded memory usage (try to keep memory minimal)
* Multi-threaded