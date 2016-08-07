package net.zhenglai.zk.cm

import org.apache.zookeeper.{KeeperException, WatchedEvent, Watcher, ZooKeeper}

/**
  * Created by Zhenglai on 8/6/16.
  */

object ClusterMonitor {
  val membershipRoot = "/net.zhenglai.zk.cm.Members"
}

class ClusterMonitor(val host: String, val port: String) {


  val connectionManager = new Watcher {
    override def process(event: WatchedEvent): Unit = {
      if (event.getType == Watcher.Event.EventType.None
        && event.getState == Watcher.Event.KeeperState.SyncConnected) {
        println(s"\nEvent Received: $event")
      }
    }
  }

  val zk = new ZooKeeper(s"$host:$port", 2000, connectionManager)
  val childrenWatcher = new Watcher {
    override def process(event: WatchedEvent): Unit = {
      println(s"\nEvent Received: $event")
      if (event.getType == Watcher.Event.EventType.NodeChildrenChanged) {
        try {
          val children = zk.getChildren(ClusterMonitor.membershipRoot, this)
          wall("!!!Cluster Membership Change!!!")
          wall(s"Members: $children")
        } catch {
          case e: KeeperException ⇒
            throw new RuntimeException(e)
          case e: InterruptedException ⇒
            Thread.currentThread().interrupt()
            alive = false
            throw new RuntimeException(e)
        }
      }
    }
  }
  var alive = true

  def close(): Unit = {
    zk.close()
  }

  def wall(message: String): Unit = {
    println(s"MESSAGE: $message")
  }

}

object ClassMonitorApp {
  def main(args: Array[String]): Unit = {

  }
}
