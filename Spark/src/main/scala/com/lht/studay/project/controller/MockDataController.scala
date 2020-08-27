package com.lht.studay.project.controller

import com.lht.studay.project.service.MockDataService
import com.lht.summer.framework.core.TController

class MockDataController extends TController {
  val mockDataService = new MockDataService

  override def execute(): Unit = {
    val result = mockDataService.analysis()
  }
}
