package com.lht.studay.stream

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WordCountTransform extends App {
  val sparkConf = new SparkConf().setAppName("streamTest").setMaster("local[2]")
  // 批处理周期
  val ssc = new StreamingContext(sparkConf, Seconds(2))
  // 执行逻辑
  val socketSD: ReceiverInputDStream[String] = ssc.socketTextStream("197.255.20.213", 9999)
  // 转换 代码执行一次，在driver端执行
  println("11")
  val res: DStream[String] = socketSD.transform(rdd=>{
    // 这里的代码会被执行多次，也就是每个2秒执行一次这里面的代码，并且是在driver端执行的
    println("22")
    rdd.map(data=>{
      // 代码执行多次，在executor端执行
      data*2
    })
  })
  // transform:rdd=>rdd  transform有返回值。
  // foreahRDD:rdd=>unit 也就是foreachRDD没有返回值
  socketSD.foreachRDD(rdd=>{
    rdd.foreach(data=>{
      println(data)
    })
  })

  res.print()
  ssc.start()
  ssc.awaitTermination()
}
