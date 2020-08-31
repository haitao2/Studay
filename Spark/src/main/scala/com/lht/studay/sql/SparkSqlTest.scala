package com.lht.studay.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object SparkSqlTest extends App {
  val conf = new SparkConf()
  conf.setMaster("local[2]")
  conf.setAppName("SparkSqlTest")
  val spark = SparkSession.builder().config(conf).getOrCreate()
  // 导入隐式转换
  // val修饰的对象可以被导入。

  import spark.implicits._

  val data: DataFrame = spark.read.json("./data/data.json")
  data.createTempView("student")
  spark.sql("select * from student").show()
  data.filter('age > 25).show()
  val dataRDD: RDD[Row] = data.rdd
  val sc = spark.sparkContext
  val listRDD = sc.makeRDD(List(
    (1, "lisi"), (2, "wangwu"), (3, "zhaoliu")
  ))
  // todo DF和DS最主要的区别在于，DF只有数据结构而没有数据类型，DS既有数据结果也有数据类型
  val studentRDD = listRDD.map(x=>(Student(x._1,x._2)))
  // rdd转换为DataFrame：  通过样例类转换
  val studentDF = studentRDD.toDF()
  // rdd转换为DataFrame：通过toDF("列名")
  val rdd2DF: DataFrame = studentRDD.toDF("id","name")
  // DataFrame转换为DataSet
  val studentDS: Dataset[Student] = studentDF.as[Student]
  // rdd转换为DataSet
  val rdd2DataSet: Dataset[Student] = studentRDD.toDS()
  // DataSet转换为DataFrame
  val ds2DF: DataFrame = studentDS.toDF()
  // todo UDF:使用自定义函数进行转换。
  spark.udf.register("addName",(x:String)=>"Name:"+x)
  spark.udf.register("changeAge",(x:Int)=>x-10)
  spark.sql("select id,addName(name),changeAge(age) from student").show()
case class Student(id:Int,name:String)
}
