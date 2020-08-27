package com.lht.studay.stream

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WordCountJoin extends App {
  val sparkConf = new SparkConf().setAppName("streamTest").setMaster("local[2]")
  // 批处理周期
  val ssc = new StreamingContext(sparkConf, Seconds(2))
  // 执行逻辑
  val socketDS1: ReceiverInputDStream[String] = ssc.socketTextStream("197.255.20.213", 9999)
  val socketDS2: ReceiverInputDStream[String] = ssc.socketTextStream("197.255.20.213", 8888)
  val res1 = socketDS1.flatMap(_.split(" ")).map((_, 1))
  val res2 = socketDS2.flatMap(_.split(" ")).map((_, 2))
  val res: DStream[(String, (Int, Int))] = res1.join(res2)
  res.print()
  // 启动采集器
  ssc.start()
  // 等待采集器的结束
  ssc.awaitTermination()
}
