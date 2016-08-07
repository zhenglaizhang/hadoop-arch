package net.zhenglai.kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}

import scala.concurrent.ExecutionException

/**
  * Created by Zhenglai on 8/7/16.
  */
class DemoProducerNew(topic: String, brokerList: String, sync: String) extends DemoProducer {

  val kafkaProps = new Properties()

  // mandatory, even though we don't send keys
  kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  kafkaProps.put("acks", "1")

  //  how many times to return when produce request fails
  kafkaProps.put("retries", "3")
  kafkaProps.put("linger.ms", 5)

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
