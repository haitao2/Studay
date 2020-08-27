package com.lht.studay.rdd

import com.lht.summer.framework.core.TService
import com.lht.summer.framework.util.EnvUtil

class WordCountService extends TService {
  val wordCountDao = new WordCountDao

  override def analysis(): Any = {
    val fileRDD = EnvUtil.getEnv().textFile("data/data.txt")
    val res = fileRDD.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).collect()
    res.foreach(println)
  }
}
