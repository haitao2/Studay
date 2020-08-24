package com.lht.studay.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * 加载csv文件
 */
object LoadDataCSV extends App {
  val conf = new SparkConf()
  conf.setAppName("loadDataTest").setMaster("local[2]")
  val spark = SparkSession.builder().config(conf).getOrCreate()
  // todo 读取csv文件   option是添加配置项，例如读取jdbc、kafka等信息的时候需要配置信息
  val data = spark.read.format("csv")
    .option("sep",";")
    .option("inferSchema","true")
    .option("header","true")
    .load("data/data.csv")
data.show()

}
