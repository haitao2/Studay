package com.lht.studay.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * 使用集群hive
 */
object LoadDataHive extends App {
  // 如果提示权限错,可以在代码中添加如下配置
  System.setProperty("HADOOP_USER_NAME","root")
  val conf = new SparkConf()
  conf.setAppName("loadDataTest").setMaster("local[2]")
  // todo 需要通过enableHiveSupport来让spark支持hive
  val spark = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
  // todo 使用集群版的hive，则需要将hive-site.xml引入到resource目录中。
  spark.sql("show databases").show();
}
