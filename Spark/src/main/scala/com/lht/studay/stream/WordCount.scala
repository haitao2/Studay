package com.lht.studay.stream

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.concurrent.duration.Duration

object WordCount extends App {

  val sparkConf = new SparkConf().setAppName("streamTest").setMaster("local[2]")
  // 批处理周期
  val ssc = new StreamingContext(sparkConf, Seconds(2))
  // 执行逻辑
  val socketSD: ReceiverInputDStream[String] = ssc.socketTextStream("197.255.20.213", 9999)
  val reDS = socketSD.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
  reDS.print()
  // 启动采集器
  ssc.start()
  // 等待采集器的结束
  ssc.awaitTermination()
}
