package net.zhenglai.kafka

import java.util
import java.util.Properties

import kafka.consumer.{Consumer, ConsumerConfig, ConsumerTimeoutException}
import kafka.serializer.StringDecoder
import kafka.utils.VerifiableProperties
import org.apache.commons.collections.buffer.CircularFifoBuffer

/**
  * Created by Zhenglai on 8/7/16.
  */
class SimpleMovingAvgZkConsumer(zkUrl: String, groupId: String, topic: String, waitTime: String) {
  val kafkaProps = new Properties()
  kafkaProps.put("zookeeper.connect", zkUrl)
  kafkaProps.put("group.id", groupId)
  kafkaProps.put("auto.commit.interval.ms", "1000")

  /*if we lose offset, we start from largest (earliest...etc.)*/
  kafkaProps.put("auto.offset.reset", "largest")

  // un-comment this if you want to commit offsets manually
  //  kafkaProps.put("auto.commit.enable", "false")

  // how long if we want to wait if there is no message
  kafkaProps.put("consumer.timeout.ms", waitTime)

  val config = new ConsumerConfig(kafkaProps)

  val consumer = Consumer.createJavaConsumerConnector(config)

  /* We tell Kafka how many threads will read each topic. We have one topic and one thread */
  val topicCountMap = new util.HashMap[String, Integer]()
  topicCountMap.put(topic, new Integer(1))


  /* We will use a decoder to get Kafka to convert messages to Strings
        * valid property will be deserializer.encoding with the charset to use.
        * default is UTF8 which works for us */
  val decoder = new StringDecoder(new VerifiableProperties())

  /* Kafka will give us a list of streams of messages for each topic.
        In this case, its just one topic with a list of a single stream */

  val stream = consumer.createMessageStreams(topicCountMap, decoder, decoder).get(topic).get(0);

  def run(window: Int): Unit = {
    val buffer = new CircularFifoBuffer(window)

    // local variable must be initialized, according to JVM spec
    // _ is not working here
    var next: String = ""

    // notice the difference with imperative programming here!
    while ( {
      next = getNextMessage
      next != null
    }) {
      var sum = 0

      try {
        val num = next.toInt
        buffer.add(num)
      } catch {
        case _: NumberFormatException => Unit // just ignore strings
      }

      // calculate current moving average in the buffer
      val it = buffer.iterator()
      while (it.hasNext) {
        sum = sum + it.next().asInstanceOf[Int]
      }

      if (buffer.size() > 0) {
        println(s"Moving average is: ${sum / buffer.size().toFloat}")
      }

      // uncomment if you want to commit offsets on every message
      // consumer.commitOffsets()
    }

    consumer.shutdown()
    sys.exit(0)
  }

  def getNextMessage: String = {
    val it = stream.iterator()
    try {
      it.next().message()
    } catch {
      case e: ConsumerTimeoutException =>
        println(s"Waited $waitTime and no message arrived.")
        null
    }

  }
}
