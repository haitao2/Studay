package com.lht.studay.stream

import com.lht.studay.stream.WordCountContinue.ssc
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * 从指定的检查点进行恢复数据和计算
 */
object WordCountContinue extends App {
  val ssc = StreamingContext.getActiveOrCreate("cp", getStreamingContext)

  // 启动采集器
  ssc.start()
  // 等待采集器的结束
  ssc.awaitTermination()

  def getStreamingContext(): StreamingContext = {
    val sparkConf = new SparkConf().setAppName("streamTest").setMaster("local[2]")
    val ssc: StreamingContext = new StreamingContext(sparkConf, Seconds(3))
    // sparkStreaming中不仅保存了中间处理数据，还保存了逻辑
    ssc.checkpoint("cp")
    val socketSD: ReceiverInputDStream[String] = ssc.socketTextStream("197.255.20.213", 9999)
    val mapDS = socketSD.flatMap(_.split(" ")).map((_, 1))
    val res = mapDS.updateStateByKey((seq: Seq[Int], option: Option[Int]) => {
      Option[Int](seq.sum + option.getOrElse(0))
    })
    res.print()
    ssc
  }
}
