package com.lht.studay.stream

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

// 窗口操作
object WordCountWindow extends App {
  val sparkConf = new SparkConf().setAppName("streamTest").setMaster("local[2]")
  // 批处理周期
  val ssc = new StreamingContext(sparkConf, Seconds(3))
  ssc.checkpoint("checkPointDir1")
  // 执行逻辑
  val socketSD: ReceiverInputDStream[String] = ssc.socketTextStream("197.255.20.213", 9999)
  // 窗口操作
  val word = socketSD.map(num=>("key",num.toInt))
  // todo reduceByKeyAndWindow，一般使用到滑动过程中重复的数据过多的时候使用，可以保留重复窗口的数据。
  val res = word.reduceByKeyAndWindow(
    (x,y)=>{
      println(s"x=${x},y=${y}")
      x+y
    },
    (a,b)=>{
      println(s"a=${a},b=${b}")
      a-b
    },
    Seconds(9)
  )
  res.print()
  // 启动采集器
  ssc.start()
  // 等待采集器的结束
  ssc.awaitTermination()
}
