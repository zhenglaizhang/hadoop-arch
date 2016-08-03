package net.zhenglai.zk

import java.util.concurrent.TimeUnit

import scala.util.Random

/**
  * Created by Zhenglai on 8/4/16.
  */
class ConfigUpdater(hosts: String) {

  import ConfigUpdater._

  val store = new ActiveKeyValueStore
  store.connect(hosts)
  private val random = new Random

  def run(): Unit = {
    while (true) {
      val value = random.nextInt(100).toString
      store.write(PATH, value)
      printf("Set %s to %s\n", PATH, value)
      TimeUnit.SECONDS.sleep(random.nextInt(10))
    }
  }

}

object ConfigUpdater {
  val PATH = "/config"

  def main(args: Array[String]): Unit = {
    val configUpdater = new ConfigUpdater(args(0))
    configUpdater.run()
  }
}


