package com.lht.summer.framework.core

import com.lht.summer.framework.util.EnvUtil
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkContext
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

trait TDao {
  def readFile(path: String) = {
    EnvUtil.getEnv().textFile(path)
  }

  /**
   * 从kafka中读取数据
   */
  def read4Kafka: Unit = {
    val kafkaPara = Map[String, Object](ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "197.255.20.214:9092,197.255.20.213:9092,197.255.20.215:9092"
      , ConsumerConfig.GROUP_ID_CONFIG -> "lht_test_group"
      , "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
      , "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
    )
    val kafkaDStream = KafkaUtils.createDirectStream(
      EnvUtil.getStreamEnv()
      , LocationStrategies.PreferConsistent
      , ConsumerStrategies.Subscribe[String, String](Set("test"), kafkaPara)
    )
  }

  /**
   * 往kafka中写数据
   */
  def write2Kafka
}
