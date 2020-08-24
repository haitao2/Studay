package com.lht.studay.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.expressions.{Aggregator, MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, IntegerType, StructField, StructType}
import org.apache.spark.sql.{Dataset, Encoder, Encoders, Row, SparkSession}

// 用户自定义聚合函数。UDAF
object UDAFTest_class extends App {
  val conf = new SparkConf()
  conf.setAppName("UDAFTest").setMaster("local[2]")
  val spark = SparkSession.builder().config(conf).getOrCreate()

  import spark.implicits._

  val sc = spark.sparkContext
  val dataRDD = sc.makeRDD(List((1, "zhangsan", 22), (2, "lisi", 23), (3, "wangwu", 24)))
  val studentRDD: RDD[Student] = dataRDD.map(x => (Student(x._1, x._2, x._3)))
  val studentDS: Dataset[Student] = studentRDD.toDS()
  // 1.创建udaf函数
  val udaf = new MyAvgFunction
  // todo 2.因为聚合函数的参数是强类型，所以无法使用sql的形式来注册并表用该函数，
  //   需要采用dsl形式进行访问，将聚合函数转换为查询的列让DataSet访问。
  studentDS.select(udaf.toColumn).show()

  // todo 定义自定义聚合函数,强类型，也就是又结构，面向对象编程。
  //   1.定义泛型
  class MyAvgFunction extends Aggregator[Student, AvgBuffer, Int] {
    // todo 缓冲区的初始值
    override def zero: AvgBuffer = {
      AvgBuffer(0, 0)
    }

    // todo 聚合操作，也就是缓冲区和输入值的操作
    override def reduce(b: AvgBuffer, a: Student): AvgBuffer = {
      b.count = b.count + 1
      b.totalAge = b.totalAge + a.age
      b
    }

    // todo 多个缓冲区的合并操作
    override def merge(b1: AvgBuffer, b2: AvgBuffer): AvgBuffer = {
      b1.totalAge = b1.totalAge + b2.totalAge
      b1.count = b1.count + b2.count
      b1
    }

    // todo 最终的缓冲区计算逻辑
    override def finish(reduction: AvgBuffer): Int = {
      reduction.totalAge / reduction.count
    }

    // todo 固定写法
    override def bufferEncoder: Encoder[AvgBuffer] = Encoders.product

    // todo 固定写法
    override def outputEncoder: Encoder[Int] = Encoders.scalaInt
  }

  case class AvgBuffer(var totalAge: Int, var count: Int)

  case class Student(id: Int, name: String, age: Int)

}
