package com.lht.summer.framework.util

import java.io.InputStreamReader
import java.util.Properties

import jodd.util.PropertiesUtil

object PropertiesUtil {

  def apply(file:String): PropertiesUtil = {
    val propertiesUtil =new PropertiesUtil()
    val pro = new Properties()
    pro.load(new InputStreamReader(Thread.currentThread().getContextClassLoader.getResourceAsStream(file), "UTF-8"))
    propertiesUtil.properties = pro
    propertiesUtil
  }
}

class PropertiesUtil() {
  var properties: Properties = _
  def getValue(key: String): String = {
    properties.getProperty(key)
  }
}
