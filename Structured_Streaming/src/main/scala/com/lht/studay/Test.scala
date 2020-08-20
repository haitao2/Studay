package com.lht.studay

import java.util.concurrent.TimeUnit

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object Test {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "D:\\soft\\hadoop-2.7.1")
    // 控制日志级别
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    val conf = new SparkConf().setMaster("local[1]").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val spark = SparkSession
      .builder
      .appName("test")
      .master("local[1]")
      .getOrCreate()
    import spark.implicits._
    val socketDF: DataFrame = spark.readStream
      .format("socket")
      .option("host", "197.255.20.214")
      .option("port", 9988)
      .load()
    val words: Dataset[login] = socketDF.map(x=>{
      val str = x.toString().split(" ")
      login(str(0).toLong,str(1))
      })
    // words.createOrReplaceTempView("word")
    // val query1 = spark.sql("select value,count(1) from word group by value").writeStream.outputMode("complete").format("console").start()
    val wordCounts = words.withWatermark("times","1 seconds")
      .groupBy("times","name").count()
    val query = wordCounts
      .writeStream
      .outputMode("append")
      .format("console")
      .trigger(Trigger.ProcessingTime(1,TimeUnit.SECONDS))
      .start()
    query.awaitTermination()
  }
}
case class login(times:Long,name:String)
