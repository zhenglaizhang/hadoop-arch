package net.zhenglai.zk

import org.apache.zookeeper.Watcher.Event.EventType
import org.apache.zookeeper.{KeeperException, WatchedEvent, Watcher}

/**
  * Created by Zhenglai on 8/4/16.
  */


object ConfigWatcher {
  def main(args: Array[String]): Unit = {
    val configWatcher = new ConfigWatcher(args(0))
    configWatcher.displayConfig()
    Thread.sleep(Long.MaxValue)
  }
}

class ConfigWatcher(hosts: String) extends Watcher {
  private val store = new ActiveKeyValueStore
  store.connect(hosts)

  override def process(watchedEvent: WatchedEvent): Unit = {
    watchedEvent.getType match {
      case EventType.NodeDataChanged ⇒
        try {
          displayConfig()
        } catch {
          case e: InterruptedException ⇒
            println("Interrupted. Exiting...")
            Thread.currentThread().interrupt()
          case e: KeeperException ⇒
            println(s"KeeperException: $e. Exiting.")
        }
      case _ ⇒
        println(s"${watchedEvent.getType} happened, ignoring...")
    }
  }

  def displayConfig() = {
    // Because watches are one-time signals, we tell ZooKeeper of the new watch each time we call read() on ActiveKeyValueStore,
    val value = store.read(ResilientConfigUpdater.PATH, this)
    println(s"Read ${ResilientConfigUpdater.PATH} as $value")
  }
}
