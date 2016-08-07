package net.zhenglai.kafka

/**
  * Created by Zhenglai on 8/7/16.
  */
object SimpleCounter {

  def main(args: Array[String]): Unit = {
    args.length match {
      case 0 => println("SimpleCounter {broker-list} {topic} {type old/new} {type sync/async} " +
        "{delay (ms)} {count}")
        sys.exit(1)
      case _ => None
    }


    /* get arguments */
    val brokerList: String = args(0)
    val topic: String = args(1)
    val age: String = args(2)
    val sync: String = args(3)
    val delay: Int = args(4).toInt
    val count: Int = args(5).toInt

    val producer: DemoProducer = age match {
      case "old" => new DemoProducerOld(topic, brokerList, sync)
      case "new" => new DemoProducerNew(topic, brokerList, sync)
      //      case _ => println(s"Third argument should be [old|new], got $age")
    }

    val startTime = System.currentTimeMillis()
    for (i <- 1 until count) {
      producer.produce(i.toString)
      Thread.sleep(delay)
    }
    val endTime = System.currentTimeMillis()
    println(s"We are done, took ${endTime - startTime}ms")
    producer.produce(s"We are done, took ${endTime - startTime}ms")

    producer.close()

    sys.exit(0)
  }

}
