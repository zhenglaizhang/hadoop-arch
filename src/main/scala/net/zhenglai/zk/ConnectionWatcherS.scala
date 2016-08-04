package net.zhenglai.zk

import java.io.IOException
import java.util.concurrent.CountDownLatch

import org.apache.zookeeper.Watcher.Event
import org.apache.zookeeper.{WatchedEvent, Watcher, ZooKeeper}

/**
  * Created by Zhenglai on 8/4/16.
  *
  *
  * TODO workaround for scala cann't access java classes via maven plugin
  * The easiest workaround would be to split it into two submodules. That way you're able to reference the Java classes as dependency. After that generate an Uber jar or shaded jar with both modules inlined.
  */
object ConnectionWatcherS {
  private val SESSION_TIMEOUT: Int = 5000
}

class ConnectionWatcherS extends Watcher {
  private val connectedSignal: CountDownLatch = new CountDownLatch(1)
  // variable has some default value (probably null)
  // since it can be reassigned
  protected var zk: ZooKeeper = _

  @throws[IOException]
  @throws[InterruptedException]
  def connect(hosts: String) {
    zk = new ZooKeeper(hosts, ConnectionWatcherS.SESSION_TIMEOUT, this)
    connectedSignal.await()
  }

  /*
      An alternative way of dealing with session expiry would be to look for a KeeperState
  of type Expired in the watcher (that would be the ConnectionWatcher
       */ def process(event: WatchedEvent) {
    if (event.getState eq Event.KeeperState.SyncConnected) {
      connectedSignal.countDown()
    }
  }

  @throws[InterruptedException]
  def close() {
    zk.close()
  }
}
