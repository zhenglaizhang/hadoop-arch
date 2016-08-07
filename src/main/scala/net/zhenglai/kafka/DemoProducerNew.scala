package net.zhenglai.kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}

import scala.concurrent.ExecutionException

/**
  * Created by Zhenglai on 8/7/16.
  */
class DemoProducerNew(topic: String, brokerList: String, sync: String) extends DemoProducer {

  val kafkaProps: Properties = new Properties()

  // mandatory, even though we don't send keys
  kafkaProps.put("bootstrap.servers", brokerList)
  kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  kafkaProps.put("acks", "1")

  //  how many times to return when produce request fails
  kafkaProps.put("retries", "3")

  /*
  By default a buffer is available to send immediately even if there is additional unused space in the buffer. However if you want to reduce the number of requests you can set linger.ms to something greater than 0. This will instruct the producer to wait up to that number of milliseconds before sending a request in hope that more records will arrive to fill up the same batch. This is analogous to Nagle's algorithm in TCP.

  However this setting would add some of latency to our request waiting for more records to arrive if we didn't fill up the buffer. Note that records that arrive close together in time will generally batch together even with linger.ms=0 so under heavy load batching will occur regardless of the linger configuration; however setting this to something larger than 0 can lead to fewer, more efficient requests when not under maximal load at the cost of a small amount of latency.
   */
  //  kafkaProps.put("linger.ms", 5)
  // [ERROR] /Users/Zhenglai/git/hadoop-arch/src/main/scala/net/zhenglai/kafka/DemoProducerNew.scala:29: error: the result type of an implicit conversion must be more specific than AnyRef
  //    [ERROR]   kafkaProps.put("linger.ms", 5)

  val producer = new KafkaProducer[String, String](kafkaProps)

  override def produce(msg: String): Unit = {
    /*
    In Scala, == is equivalent to equals except that it handles null so no NullPointerException is thrown.
  If you want reference equality, use eq.

  There are other minor differences between == and equals, e.g. == is aware of numeric equivalence: 1==1L but !1.equals(1L). In general, == is the reasonable operator to use unless there is an explicit reason not to do so
     */
    sync match {
      case "sync" => produceSync(msg)
      case "async" => produceAsync(msg)
      case _ => throw new IllegalArgumentException(s"Expected sync or async, got $sync")
    }
  }

  /*
  Give a callback when the produce completes
   */
  def produceAsync(msg: String): Unit = {
    val record = new ProducerRecord[String, String](topic, msg)
    producer.send(record, new Callback {
      override def onCompletion(recordMetadata: RecordMetadata, e: Exception): Unit = {
        if (e != null) {
          println(s"Error producing message [$msg] to topic [$topic]")
          e.printStackTrace()
        } else {
          println(s"Success producing message [$msg] to topic [$topic]")
        }
      }
    })
  }

  // produce a record and wait for server to reply
  // throws exception if anything wrong
  @throws(classOf[ExecutionException])
  @throws(classOf[InterruptedException])
  def produceSync(msg: String): Unit = {
    val record = new ProducerRecord[String, String](topic, msg)
    producer.send(record).get()
  }

  override def close(): Unit = {
    producer.close()
  }
}
