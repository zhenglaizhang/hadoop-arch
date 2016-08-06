package net.zhenglai.zk

import org.apache.zookeeper.{KeeperException, ZooKeeper}

/**
  * Created by Zhenglai on 8/6/16.
  */
object HelloZookeeper {

  def main(args: Array[String]): Unit = {
    val address = "localhost:2181"
    val znodePath = "/"
    // connect to ZK and return one handle (session)
    val zk = new ZooKeeper(address /*connection string, comma-separated list*/ , 2000 /*session timeout*/ , null
      /*watcher*/)
    if (zk != null) {
      try {
        val zooChildren = zk.getChildren(znodePath, false)
        println("Znodes of '/': ")
        zooChildren.toArray().foreach(
          println(_)
        )
      } catch {
        case e: KeeperException ⇒
          e.printStackTrace()
        case e: InterruptedException ⇒
          e.printStackTrace()
      }

    }
  }
}
