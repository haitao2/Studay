package com.lht.studay.rdd

import com.lht.summer.framework.core.TController

class WordCountController extends TController {
  val wordCountService = new WordCountService

  override def execute(): Unit = {
    val res = wordCountService.analysis()
  }
}
