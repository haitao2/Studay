package com.lht.studay.stream

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * 状态保存
 */
object WordCountState extends App {
  val sparkConf = new SparkConf().setAppName("streamTest").setMaster("local[2]")
  // 批处理周期
  val ssc = new StreamingContext(sparkConf, Seconds(2))
  // 执行逻辑
  val socketSD: ReceiverInputDStream[String] = ssc.socketTextStream("197.255.20.213", 9999)
  // 该检查点是用来存储updateStateByKey的中间结果路径。
  ssc.checkpoint("checkPointDir")
  val reDS = socketSD.flatMap(_.split(" ")).map((_, 1))
    // reduceByKey是无状态的操作，不能保存上一批次的结果
    // .reduceByKey(_ + _)
    // 又状态的目的其实就是将每一个采集周期数据的计算结果临时保存起来
    // 然后在下一次数据处理中继续使用。
    // updateStateByKey 是有状态的计算
    // 第一个参数表示 相同key的value集合
    // 第二个参数表示 相同key缓冲区的数据，有可能为空。
    // 注意计算中间结果需要保存到检查点中
      .updateStateByKey[Int](
        (seq:Seq[Int],buffer:Option[Int])=>{
          val newBufferValue = buffer.getOrElse(0)+seq.sum
          Option[Int](newBufferValue)
        }
      )



  reDS.print()
  // 启动采集器
  ssc.start()
  // 等待采集器的结束
  ssc.awaitTermination()
}
