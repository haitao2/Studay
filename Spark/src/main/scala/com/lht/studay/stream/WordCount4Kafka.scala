package com.lht.studay.stream

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WordCount4Kafka extends App {
  val sparkConf = new SparkConf().setAppName("streamTest").setMaster("local[2]")
  // 批处理周期
  val ssc = new StreamingContext(sparkConf, Seconds(2))
  // kafka的配置信息
  val kafkaPara = Map[String,Object](
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG->"197.255.20.215:9092",
    ConsumerConfig.GROUP_ID_CONFIG->"lht_test",
    "key.deserializer"->"org.apache.kafka.common.serialization.StringDeserializer",
    "value.deserializer"->"org.apache.kafka.common.serialization.StringDeserializer"
  )
    val res: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(ssc,
    LocationStrategies.PreferBrokers,
    ConsumerStrategies.Subscribe(Set("test"),kafkaPara))
}
