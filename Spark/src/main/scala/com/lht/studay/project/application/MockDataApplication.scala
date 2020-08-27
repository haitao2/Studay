package com.lht.studay.project.application

import com.lht.studay.project.controller.MockDataController
import com.lht.summer.framework.core.TApplication

object MockDataApplication extends App with TApplication {
  start("sparkStreaming") {
    val mockDataController = new MockDataController
    mockDataController.execute()
  }
}
