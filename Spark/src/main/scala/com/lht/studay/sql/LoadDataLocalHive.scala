package com.lht.studay.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
 * 使用本地hive操作
 */
object LoadDataLocalHive extends App {
  val conf = new SparkConf()
  conf.setAppName("loadDataTest").setMaster("local[2]")
  // todo 需要通过enableHiveSupport来让spark支持hive
  val spark = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
  // todo 默认情况下，spark支持操作本地的hive，执行前需要启用hive支持。
  //   该操作会在根目录下创建一个metastore_db和spark-warehouse两个目录，用来存储本地hive的元数据和hive的数据
  spark.sql("create table aa(id int)")
  spark.sql("load data local inpath 'data/data.txt' into table aa")
  spark.sql("show tables;")
}
