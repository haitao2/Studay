package com.lht.studay.rdd

import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
 * 累加器
 */
object Accumulator extends App {
  val conf = new SparkConf().setAppName("test").setMaster("local[2]")
  val sc = new SparkContext(conf)
  sc.setLogLevel("WARN")
  val dataRDD = sc.makeRDD(List(
    ("a", 1), ("a", 2), ("a", 3), ("a", 4), ("a", 5)
  ))
  /*  // todo 累加器
    // var sum = 0
    // dataRDD.foreach { case (a, b) => sum = sum + b }
    // todo 声明累加器对象,累加器在executor端是共享的，每个executor端会有一个独立的sum累加器对象，
    // todo 该累加器对象是只写的，多个executor端累加器的结果会返回到driver端，driver端会将累加器进行合并求和
    val sum = sc.longAccumulator("sum")
    dataRDD.foreach(num=>{
      sum.add(num._2)
    })
    println(sum.value)*/
  val dataRDD1 = sc.makeRDD(List(
    "hello", "word hello time"
  ))
  // todo 自定义累加器
  // 1.创建累加器
  val acc = new MyAccumulator
  // 2.注册累加器
  sc.register(acc)
  // 3.使用自定义累加器。
  dataRDD1.flatMap(_.split(" ")).foreach(word => {
    acc.add(word)
  })
  acc.value.mkString(",").foreach(print)


  //  继承AccumulatorV2[In,Out]抽象类
  //    In:累加器输入类型
  //    Out:累加器输出类型
  class MyAccumulator extends AccumulatorV2[String, mutable.Map[String, Int]] {
    //存储wordcount的集合 可变的
    var wordCountMap = mutable.Map[String, Int]()

    // AccumulatorV2是抽象类，抽象类中的抽象方法子类必须实现。抽象类中没有具体实现的方法都是抽象方法。
    // todo 是否重置，程序自动调用。
    override def isZero: Boolean = wordCountMap.isEmpty

    // todo 复制累加器,程序会自动创建
    override def copy(): AccumulatorV2[String, mutable.Map[String, Int]] = new MyAccumulator

    // todo 重置累加器,可以通过acc.reset方法重置累加器
    override def reset(): Unit = wordCountMap.clear()

    // todo 往累加器中增加值 也就是程序调用acc.add(word)中的add方法。
    override def add(word: String): Unit = {
      // map.getOrElse(key,0)从map中获取key对应的value值，如果不存在则使用默认值:0
      wordCountMap(word) = wordCountMap.getOrElse(word, 0) + 1
    }

    // todo 因为executor端的累加器是互相独立的，需要在driver端进行合并，所以该方法用于累加器在driver端进行合并操作。
    override def merge(other: AccumulatorV2[String, mutable.Map[String, Int]]): Unit = {
      // 也就是合并两个map,key相同则累加,
      // todo 这里需要更新wordCountMap，因为累加器在driver端是两两合并的，需要将两两合并的值更新。
      wordCountMap = wordCountMap.foldLeft(other.value)((a, b) => {
        // a代表的是other.value集合，则b代表的是wordCountMap中每一个key-value值。
        a(b._1) = a.getOrElse(b._1, 0) + b._2
        a
      })
    }

    // todo 获取累加器的返回值，也就是程序调用的acc.value会调用该方法。
    override def value: mutable.Map[String, Int] = {
      wordCountMap
    }
  }

}
