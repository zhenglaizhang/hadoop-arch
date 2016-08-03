package net.zhenglai.zk

import java.nio.charset.Charset

import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.ZooDefs.Ids
import org.apache.zookeeper.data.Stat

/**
  * Created by Zhenglai on 8/4/16.
  */

object ActiveKeyValueStore {
  private val CHARSET = Charset.forName("UTF-8")
}

class ActiveKeyValueStore extends ConnectionWatcher {

  def write(path: String, value: String): Unit = {
    val stat: Stat = zk.exists(path, false)
    // TODO: convert to Option[T]
    if (stat == null) {
      zk.create(path, value.getBytes(ActiveKeyValueStore.CHARSET), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT)
    } else {
      // version set as -1 to override any existing versions
      zk.setData(path, value.getBytes(ActiveKeyValueStore.CHARSET), -1)
    }
  }

}
