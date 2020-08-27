package com.lht.summer.framework.core

import com.lht.summer.framework.util.EnvUtil

trait TDao {
  def readFile(path: String) = {
    EnvUtil.getEnv().textFile(path)
  }

  /**
   * 从kafka中读取数据
   */
  def read4Kafka: Unit ={

  }

  /**
   * 往kafka中写数据
   */
  def write2Kafka
}
