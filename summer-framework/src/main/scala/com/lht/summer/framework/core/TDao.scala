package com.lht.summer.framework.core

import java.util.Properties

import com.lht.summer.framework.util.{EnvUtil, PropertiesUtil}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.log4j.Logger
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

trait TDao {
  val log = Logger.getLogger(this.getClass)

  def readFile(path: String) = {
    EnvUtil.getEnv().textFile(path)
  }

  /**
   * 从kafka中读取数据
   */
  def read4Kafka: DStream[String] = {
    val brokerList = PropertiesUtil.getValue("kafka.broker.list")
    val consumerGroup = PropertiesUtil.getValue("kafka.consumer.group")
    val topic = PropertiesUtil.getValue("kafka.topic")
    val kafkaPara = Map[String, Object](ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokerList
      , ConsumerConfig.GROUP_ID_CONFIG -> consumerGroup
      , "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
      , "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
    )
    val kafkaDStream = KafkaUtils.createDirectStream(
      EnvUtil.getStreamEnv()
      , LocationStrategies.PreferConsistent
      , ConsumerStrategies.Subscribe[String, String](Set(topic), kafkaPara)
    )
    kafkaDStream.map(_.value())
  }

  /**
   * 往kafka中写数据
   */
  def write2Kafka(implicit datas: () => Seq[String]): Unit = {
    val brokerList = PropertiesUtil.getValue("kafka.broker.list")
    val topic = PropertiesUtil.getValue("kafka.topic")
    // 创建kafka消费者
    val prop = new Properties()
    log.info("brokerList" + brokerList)
    prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList)
    prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    val kafkaProducer = new KafkaProducer[String, String](prop)
    while (true) {
      for (line <- datas()) {
        kafkaProducer.send(new ProducerRecord[String, String](topic, line))
        println(line)
      }
      Thread.sleep(2000)
    }
  }
}
