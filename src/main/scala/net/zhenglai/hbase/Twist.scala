package net.zhenglai.hbase

import org.apache.hadoop.hbase.client.HTablePool

/**
  * Created by Zhenglai on 8/5/16.
  */
//noinspection ScalaDeprecation

class HBaseOp {
  def run(tableName: String) = {
    val pool = new HTablePool
    val tableInterface = pool.getTable(tableName)
    // work with the table

    tableInterface.close()
  }
}

class Twist {


}
