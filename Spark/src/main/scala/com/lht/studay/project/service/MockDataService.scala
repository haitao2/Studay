package com.lht.studay.project.service

import com.lht.studay.project.dao.MockDataDao
import com.lht.summer.framework.core.TService

class MockDataService extends TService {
  private val mockDataDao = new MockDataDao

  // 数据分析
  override def analysis(): Any = {
    // todo 生成数据
    //   发送给kafka数据
    // mockDataDao.getMockData().foreach(println)
    import mockDataDao._
    mockDataDao.write2Kafka
  }
}
