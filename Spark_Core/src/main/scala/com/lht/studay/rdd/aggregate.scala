package com.lht.studay.rdd

import org.apache.spark.sql.SparkSession

object aggregate {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.master("local[1]").appName("aggregateTest").getOrCreate()
    val sc = spark.sparkContext
    val rdd1 = sc.makeRDD(List(
      ("a",23),("b",25),("a",42),("b",28),("c",42),("b",33)
    ),2)
    // 使用了柯里化,第二个参数(_+_,_+_)中，第一个参数是分区内的运算函数，第二个参数是分区间的运算函数。
    rdd1.aggregateByKey(0)(_+_,_+_)
  }
}
