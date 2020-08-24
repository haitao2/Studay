package com.lht.studay.sql

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

object LoadData extends App {
  val conf = new SparkConf()
  conf.setAppName("loadDataTest").setMaster("local[2]")
  val sc = new SparkContext(conf)
  // spark读取json文件时候，每一行都需要符合json格式要求。
  val myData = sc.textFile("data/my.txt")
  val checkData: Array[String] = sc.textFile("data/check.txt").collect()
  val checkBroadcast: Broadcast[Array[String]] = sc.broadcast(checkData)



}
