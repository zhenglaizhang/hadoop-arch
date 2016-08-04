package net.zhenglai.zk

import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

import org.apache.zookeeper.ZooDefs.Ids
import org.apache.zookeeper.{CreateMode, KeeperException, Watcher}

/**
  * Created by Zhenglai on 8/4/16.
  */

object ActiveKeyValueStore {
  private val CHARSET = Charset.forName("UTF-8")
  private val MAX_RETRIES = 3
  private val RETRY_PERIOD_SECONDS = 5
}

class ActiveKeyValueStore extends ConnectionWatcher {

  import ActiveKeyValueStore._


  // Taken as a whole, the write() method is idempotent, so we can afford to unconditionally retry it.
  def write(path: String, value: String): Unit = {
    var retries = 0;
    while (true) {
      try {
        val stat = Option(zk.exists(path, false))
        stat match {
          // convert the string value to a byte array
          case Some(_) ⇒ zk.create(path, value.getBytes(ActiveKeyValueStore.CHARSET), Ids.OPEN_ACL_UNSAFE, CreateMode
            .PERSISTENT)
          case None ⇒ zk.setData(path, value.getBytes(ActiveKeyValueStore.CHARSET), -1)
          // version set as -1 to override any existing versions
        }
      } catch {
        // when a session expires, the ZooKeeper object enters the CLOSED state, from which it can never reconnect
        case e: KeeperException.SessionExpiredException ⇒ throw e;
        case e: KeeperException ⇒
          retries += 1
          if (retries == MAX_RETRIES) {
            throw e
          }
      }

      TimeUnit.SECONDS.sleep(RETRY_PERIOD_SECONDS)
    }
  }

  def read(path: String, watcher: Watcher) = {
    // we pass a null Stat because we are not interested in the metadata
    val data = zk.getData(path, watcher, null /*stat*/)
    new String(data, ActiveKeyValueStore.CHARSET)
  }

}
