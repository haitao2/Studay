package com.lht.studay.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
 * 加载csv文件
 */
object LoadDataMysql extends App {
  val conf = new SparkConf()
  conf.setAppName("loadDataTest").setMaster("local[2]")
  val spark = SparkSession.builder().config(conf).getOrCreate()
  // todo 读取mysql中数据
  import spark.implicits._
  val dataMysql = spark.read.format("jdbc")
    .option("url","jdbc:mysql://172.16.4.14/test")
    .option("driver","com.mysql.jdbc.Driver")
    .option("user","root")
    .option("password","Root#123")
    .option("dbtable","test").load
  // 会保存为一张新表
  dataMysql.write.mode(SaveMode.Append).format("jdbc")
    .option("url","jdbc:mysql://172.16.4.14/test")
    .option("driver","com.mysql.jdbc.Driver")
    .option("user","root")
    .option("password","Root#123")
    .option("dbtable","test1")
    .save()

}
