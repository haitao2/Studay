package com.lht.summer.framework.util

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object EnvUtil {
  private val scLocal = new ThreadLocal[SparkContext]
  private val sscLocal = new ThreadLocal[StreamingContext]

  def getEnv[T](t: T):T = {
    if (t == SparkContext) {
      // 从当前线程的共享内存空间中获取环境对象
      var sc: SparkContext = scLocal.get()
      // 如果获取不到环境对象，则创建新的环境对象，保存导共享内存中
      if (sc == null) {
        val conf = new SparkConf().setMaster("local[2]").setAppName("sparkApplication")
        // 创建环新的环境对啊ing
        sc = new SparkContext(conf)
        // 保存到线程共享内存中
        scLocal.set(sc)
      }
      sc
    } else if (t == StreamingContext) {
      // 从当前线程的共享内存空间中获取环境对象
      var ssc: StreamingContext = sscLocal.get()
      // 如果获取不到环境对象，则创建新的环境对象，保存导共享内存中
      if (ssc == null) {
        val conf = new SparkConf().setMaster("local[2]").setAppName("sparkApplication")
        // 创建环新的环境对啊ing
        ssc = new StreamingContext(conf, Seconds(5))
        // 保存到线程共享内存中
        sscLocal.set(ssc)
      }
      ssc
    }
  }


 /* def getEnv(): SparkContext = {
    // 从当前线程的共享内存空间中获取环境对象
    var sc: SparkContext = scLocal.get()
    // 如果获取不到环境对象，则创建新的环境对象，保存导共享内存中
    if (sc == null) {
      val conf = new SparkConf().setMaster("local[2]").setAppName("sparkApplication")
      // 创建环新的环境对啊ing
      sc = new SparkContext(conf)
      // 保存到线程共享内存中
      scLocal.set(sc)
    }
    sc
  }*/

  def clear(): Unit = {
    getEnv().stop()
    // 将共享内存中的数据清除掉
    scLocal.remove()
  }

}