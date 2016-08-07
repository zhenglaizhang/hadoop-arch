package net.zhenglai.kafka

import java.util.Properties

import kafka.javaapi.producer.Producer
import kafka.producer.{KeyedMessage, ProducerConfig}

/**
  * Created by Zhenglai on 8/7/16.
  */
class DemoProducerOld(topic: String, brokerList: String, sync: String) extends DemoProducer {


  // create Producer config
  val kafkaProps = new Properties()
  kafkaProps.put("metadata.broker.list", brokerList)
  kafkaProps.put("serializer.class", "kafka.serializer.StringEncoder")
  kafkaProps.put("request.required.acks", "1")
  kafkaProps.put("producer.type", sync)

  val config = new ProducerConfig(kafkaProps)


  val producer = new Producer[String, String](config)


  /*
      create record and send to Kafka
      because the key is null, data will be sent to a random partition.
      exact behavior will be different depending on producer implementation
   */
  override def produce(msg: String): Unit = {
    producer.send(new KeyedMessage[String, String](topic = topic, null, msg))
  }

  override def close() = {
    producer.close
  }
}
