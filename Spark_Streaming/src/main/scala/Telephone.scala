object Telephone extends App {

  // 主叫  被叫  注册时间  注册地方
  // 判断主叫   最近两次注册时间大于一小时   并且注册地点不通
  case class CallInfo(call: String, called: String, registerTime: String, registerPlace: String)
}

