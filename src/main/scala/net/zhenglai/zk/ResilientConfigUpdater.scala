package net.zhenglai.zk

import java.util.concurrent.TimeUnit

import org.apache.zookeeper.KeeperException

import scala.util.Random

/**
  * Created by Zhenglai on 8/4/16.
  */
class ResilientConfigUpdater(hosts: String) {

  import ResilientConfigUpdater._

  val store = new ActiveKeyValueStore
  store.connect(hosts)
  private val random = new Random

  def run(): Unit = {
    while (true) {
      try {
        val value = random.nextInt(100).toString
        store.write(PATH, value)
        printf("Set %s to %s\n", PATH, value)
        TimeUnit.SECONDS.sleep(random.nextInt(10))
      } catch {
        case _: KeeperException.SessionExpiredException ⇒
          // start a new session
          printf("session expired, starting new one...")
        case e: KeeperException ⇒
          // already retried
          e.printStackTrace()
      }
    }
  }

}

object ResilientConfigUpdater {
  val PATH = "/config"

  def main(args: Array[String]): Unit = {
    val configUpdater = new ResilientConfigUpdater(args(0))
    configUpdater.run()
  }
}


