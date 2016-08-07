package net.zhenglai.kafka

/**
  * Created by Zhenglai on 8/7/16.
  */
trait DemoProducer {
  def close()

  def produce(msg: String)
}
