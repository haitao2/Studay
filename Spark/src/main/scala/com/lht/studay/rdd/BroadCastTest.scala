package com.lht.studay.rdd

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.SparkSession

// 广播变量联系。
object BroadCastTest extends App {
  val spark = SparkSession.builder().appName("test").master("local[2]").getOrCreate()
  val sc = spark.sparkContext
  sc.setLogLevel("WARN")
  val rdd1 = sc.makeRDD(List(
    ("a", 1), ("a", 2), ("b", 1), ("c", 1)
  ))
  val rdd2 = sc.makeRDD(List(
    ("a", 3), ("b", 2), ("c", 2), ("d", 1)
  ))
  val list = List[(String, Int)](("a", 3), ("b", 2), ("c", 2), ("d", 1))
  val listBroadCast: Broadcast[List[(String, Int)]] = sc.broadcast(list)
  // 因为join算子会发生shuffle操作，并且join算子相当与笛卡尔积操作。
  val joinRDD = rdd1.join(rdd2)
  // todo spark计算数据时使用闭包操作，会导变量被不通的task所使用，也就是一个变量会传多份到executor端，
  //  也就是会造成大量的数据冗余，会导致性能下降或者内存不够。
  //  为了解决join出现的性能问题，可以将数据独立出来，防止造成shuffle操作，
  //  采用闭包操作的时候需要谨慎，在spark中尽量不采用闭包操作，而是使用广播变量替换
  //  广播变量是在executor端被task所共享、只读变量
  // 替代join方案，使用list采用闭包操作。不推荐使用，而是使用广播变量将list广播到executor端，从而减少task中list的冗余。
  val mapRDD = rdd1.map(x => {
    var v2 = 0
    for ((k, v) <- listBroadCast.value) {
      if (x._1 == k)
        v2 = v
    }
    (x._1, (x._2, v2))
  })
  println(mapRDD.collect().mkString(","))
  println("------------------------------------------")
  // (b,(1,2)),(a,(1,3)),(a,(2,3)),(c,(1,2))
  println(joinRDD.collect().mkString(","))
}
