package com.lht.studay.rdd

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

object CacheAndScheckPointTest extends App {
/*  // 屏蔽日志
  // Logger.getRootLogger.setLevel(Level.WARN)
  val conf = new SparkConf()
  val spark = SparkSession.builder.appName("cacheTest").master("local[2]").getOrCreate()
  val sc = spark.sparkContext
  sc.setCheckpointDir("out")
  //sc.setLogLevel("WARN")
  val rdd1 = sc.makeRDD(List(
    1, 2, 3, 4
  )).map(x => {
    println("------这是一个map方法---------")
    x
  })
  // 一般程序中checkpoint是和cache一起使用的，先对rdd中的数据进行cache，然后再进行checkpoint，从而减少数据再次运算。
  rdd1.cache()
  rdd1.checkpoint()
  rdd1.collect().mkString("&").foreach(print)
  rdd1.collect().mkString(",").foreach(print)
  rdd1.collect().mkString("*").foreach(print)*/

}
