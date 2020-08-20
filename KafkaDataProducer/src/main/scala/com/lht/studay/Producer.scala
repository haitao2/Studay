package com.lht.studay

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.Source
import scala.util.Random

object Producer extends App {
  val events = args(0).toInt
  val topic = args(1)
  val brokers = args(2)
  val rnd = new Random()
  val props = new Properties()
  // 添加配置项
  props.put("bootstrap.servers", brokers)
  props.put("client.id", "kafkaDataProducer")
  props.put("key.serializer", "org.apache.kafka.common.serialize.StringSerialize")
  props.put("value.serializer", "org.apache.kafka.common.serialize.StringSerialize")
  //构建kafka生产者
  val producer = new KafkaProducer[String, String](props)
  val t = System.currentTimeMillis()
  // 读取汉字字典
  val source = Source.fromFile("hanzi.txt")
  val lines = try source.mkString finally source.close()
  for (nEvents <- Range(0, events)) {
    //生成评论数据
    val sb = new StringBuilder()
    for (ind <- Range(0, rnd.nextInt(200))) {
      sb += lines.charAt(rnd.nextInt(lines.length))
    }
    val userName = "user_" + rnd.nextInt(100)
    //构建生产者记录
    val data = new ProducerRecord[String, String](topic, userName, sb.toString())
    producer.send(data)
  }
  System.out.println("send per second:" + events * 1000 / (System.currentTimeMillis() - t))
  producer.close()
}
