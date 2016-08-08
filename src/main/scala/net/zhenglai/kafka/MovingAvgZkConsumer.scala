package net.zhenglai.kafka

/**
  * Created by Zhenglai on 8/7/16.
  */
object MovingAvgZkConsumer {

  def main(args: Array[String]): Unit = {
    args.length match {
      case 0 =>
        println("SimpleMovingAvgZkConsumer {zookeeper} {group.id} {topic} {window-size} {wait-time}")
        sys.exit(1)
      case _ =>
        new SimpleMovingAvgZkConsumer(
          args(0),
          args(1),
          args(2),
          args(4)
        ).run(args(3).toInt)
    }
  }

}
