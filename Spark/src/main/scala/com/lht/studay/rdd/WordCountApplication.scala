package com.lht.studay.rdd

import com.lht.summer.framework.core.TApplication

object WordCountApplication extends App with TApplication {
  start("spark") {
    val controller = new WordCountController
    controller.execute()
  }

}
