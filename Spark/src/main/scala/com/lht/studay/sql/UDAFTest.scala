package com.lht.studay.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, IntegerType, StructField, StructType}

// 用户自定义聚合函数。UDAF
object UDAFTest extends App {
  val conf = new SparkConf()
  conf.setAppName("UDAFTest").setMaster("local[2]")
  val spark = SparkSession.builder().config(conf).getOrCreate()

  import spark.implicits._

  val sc = spark.sparkContext
  val dataRDD = sc.makeRDD(List((1, "zhangsan", 22), (2, "lisi", 23), (3, "wangwu", 24)))
  val studentRDD: RDD[Student] = dataRDD.map(x => (Student(x._1, x._2, x._3)))
  val studentDS = studentRDD.toDS()
  studentDS.createTempView("student")
  spark.sql("select * from student").show()
  // 1.创建udaf函数
  val udaf = new MyAvgFunction
  // 2.注册udaf函数
  spark.udf.register("myavg",udaf)
  // 3.sql中使用udaf函数
  spark.sql("select myavg(age) from student").show()
  // todo 定义 自定义聚合函数
  class MyAvgFunction extends UserDefinedAggregateFunction {
    // todo 数据的输入类型
    override def inputSchema: StructType = {
      StructType(Array(StructField("age", IntegerType)))
    }

    // todo 缓冲区的数据类型
    override def bufferSchema: StructType = {
      StructType(Array(StructField("totalAge", IntegerType), StructField("count", IntegerType)))
    }

    // todo 聚合函数返回的结果类型
    override def dataType: DataType = IntegerType

    // todo 函数稳定性，返回值的类型是否一致
    override def deterministic: Boolean = true

    // todo 函数缓冲区的初始化，
    override def initialize(buffer: MutableAggregationBuffer): Unit = {
      // 对应的是bufferSchema方法中totalAge的初始值
      buffer(0) = 0
      // 对应的是bufferSchema方法中count的初始值
      buffer(1) = 0
    }

    // todo 更新缓冲区的值,也就是将输入的值合并到缓冲区中。
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      buffer(0) = buffer.getInt(0) + input.getInt(0)
      buffer(1) = buffer.getInt(1) + 1
    }

    // todo 合并缓冲区中的值
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1(0) = buffer1.getInt(0) + buffer2.getInt(0)
      buffer1(1) = buffer1.getInt(1) + buffer2.getInt(1)
    }

    // todo 计算最终缓冲区的值，也就是函数的计算
    override def evaluate(buffer: Row): Any = {
      buffer.getInt(0) / buffer.getInt(1)
    }
  }


  case class Student(id: Int, name: String, age: Int)

}
