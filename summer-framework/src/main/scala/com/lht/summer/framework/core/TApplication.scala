package com.lht.summer.framework.core


import java.net.{ServerSocket, Socket}

import com.lht.summer.framework.util.{EnvUtil, PropertiesUtil}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}


trait TApplication {
  var eventData: Any = null

  /**
   * 启动应用程序
   * 1.环境的初始化
   */
  def start(t: String = "jdbc")(op: => Unit)(implicit time: Duration = Seconds(3)): Unit = {
    val prop = PropertiesUtil("default.properties")
    if (t == "socket") {
      eventData = new Socket(prop.getValue("server.host"), prop.getValue("server.port").toInt)
    } else if (t == "serverSocket") {
      eventData = new ServerSocket(prop.getValue("server.port").toInt)
    } else if (t == "spark") {
      eventData = EnvUtil.getEnv()
    } else if (t == "sparkStreaming") {
      val conf = new SparkConf().setMaster("local[2]").setAppName("stringApplication")
      eventData = new StreamingContext(conf, time)
    }
    // 2.业务逻辑
    try {
      op
    } catch {
      case ex: Exception => println("业务执行失败:" + ex.getMessage)
    }
    // 3.环境关闭
    if (t == "socket") {
      val socket: Socket = eventData.asInstanceOf[Socket]
      if (!socket.isClosed) {
        socket.close()
      } else if (t == "serverSocket") {
        val serverSocket = eventData.asInstanceOf[ServerSocket]
        if (!serverSocket.isClosed) {
          serverSocket.close()
        }
      } else if (t == "spark") {
        EnvUtil.clear()
      } else if (t == "sparkStreaming") {
        val ssc = eventData.asInstanceOf[StreamingContext]
        ssc.start()
        ssc.awaitTermination()
        // 优雅的关闭
        // ssc.stop(true,true)
      }
    }
  }
}
