package com.lht.studay.stream

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WordCount4Kafka extends App {
  val sparkConf = new SparkConf().setAppName("streamTest").setMaster("local[2]")
  // 批处理周期
  val ssc = new StreamingContext(sparkConf, Seconds(2))
  val kafkaPara = Map[String,Object](
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG->"",
    ConsumerConfig.GROUP_ID_CONFIG->"",
  )
  KafkaUtils.createDirectStream(ssc,
    LocationStrategies.PreferBrokers,
    ConsumerStrategies.Subscribe(Set("test"),kafkaPara))
}
