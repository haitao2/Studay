/*


import java.util

import org.I0Itec.zkclient.ZkClient
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext, rdd}
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.streaming.{Duration, StreamingContext}
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils, OffsetRange}

object kafka01 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Direct").setMaster("local[2]")

    val ssc = new StreamingContext(conf,Duration(5000))

    //指定组名
    val groupId = "flume-consumer"
    //指定消费的topic名字
    val topic = "call"
    //指定kafka的Broker地址（SparkStreaming的Task直接连接到Kafka分区上，用的是底层API消费）
    val brokerList ="192.168.205.102:9092"
    //接下来我们要自己维护offset了，将offset保存到ZK中
    val zkQuorum = "192.168.205.102:2181"
    //创建stream时使用的topic名字集合，SparkStreaming可以同时消费多个topic
    val topics:Set[String] = Set(topic)
    //创建一个ZkGroupTopicDirs对象，其实是指定往Zk中写入数据的目录
    // 用于保存偏移量
    val TopicDirs = new ZKGroupTopicDirs(groupId,topic)
    //获取zookeeper中的路径“/gp01/offset/tt/”
    val zkTopicPath = s"${TopicDirs.consumerOffsetDir}"
    //准备kafka参数
    val kafkas = Map(
      "metadata.broker.list"->brokerList,
      "group.id"->groupId,
      //从头开始读取数据
      "auto.offset.reset"->kafka.api.OffsetRequest.SmallestTimeString
    )
    // zookeeper 的host和ip，创建一个client，用于更新偏移量
    // 是zookeeper客户端，可以从zk中读取偏移量数据，并更新偏移量
    val zkClient = new ZkClient(zkQuorum)
    //"/gp01/offset/tt/0/10001"
    //"/gp01/offset/tt/1/20001"
    //"/gp01/offset/tt/2/30001"
    val clientOffset = zkClient.countChildren(zkTopicPath)
    // 创建KafkaStream
    var kafkaStream :InputDStream[(String,String)]= null
    //如果zookeeper中有保存offset 我们会利用这个offset作为KafkaStream的起始位置
    //TopicAndPartition  [/gp01/offset/tt/0/ , 8888]
    var fromOffsets:Map[TopicAndPartition,Long] = Map()
    //如果保存过offset
    if(clientOffset > 0){
      //clientOffset 的数量其实就是 /gp01/offset/tt的分区数目
      for(i<-0 until clientOffset){
        // /gp01/offset/tt/  0/10001
        val partitionOffset = zkClient.readData[String](s"$zkTopicPath/${i}")
        // tt/0
        val tp = TopicAndPartition(topic,i)
        //将不同partition 对应得offset增加到fromoffset中
        // tt/0 -> 10001
        fromOffsets += (tp->partitionOffset.toLong)
      }
      // key 是kafka的key value 就是kafka数据
      // 这个会将kafka的消息进行transform 最终kafka的数据都会变成(kafka的key,message)这样的Tuple
      val messageHandler = (mmd:MessageAndMetadata[String,String])=>
        (mmd.key(),mmd.message())
      // 通过kafkaUtils创建直连的DStream
      //[String,String,StringDecoder, StringDecoder,(String,String)]
      // key    value  key解码方式     value的解码方式   接收数据的格式
      kafkaStream = KafkaUtils.createDirectStream
        [String,String,StringDecoder,
          StringDecoder,(String,String)](ssc,kafkas,fromOffsets,messageHandler)
    }else{
      //如果未保存，根据kafkas的配置使用最新的或者最旧的offset
      kafkaStream = KafkaUtils.createDirectStream
        [String,String,StringDecoder,StringDecoder](ssc,kafkas,topics)
    }
    //偏移量范围
    var offsetRanges = Array[OffsetRange]()
    //从kafka读取的数据，是批次提交的，那么这块注意下，
    // 我们每次进行读取数据后，需要更新维护一下偏移量
    //那么我们开始进行取值
    //    val transform = kafkaStream.transform{
    //      rdd=>
    //        //得到该RDD对应得kafka消息的offset
    //        // 然后获取偏移量
    //        offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
    //        rdd
    //    }
    //    val mes = transform.map(_._2)
    // 依次迭代DStream中的RDD
    val map = new util.HashMap[String,String]()
    val timeStame = 3600
    kafkaStream.foreachRDD{
      //对RDD进行操作 触发Action
      kafkardd=>

        offsetRanges = kafkardd.asInstanceOf[HasOffsetRanges].offsetRanges

        //下面 你就可以怎么写都行了，为所欲为
        val sparkContext = ssc.sparkContext
        val sqlContext: SQLContext = SQLContextSingleton.getInstance(sparkContext)
        import sqlContext.implicits._

        val maps = kafkardd.map(_._2)

        maps.foreach(println)


        val userRDD: RDD[UserCall] = maps.filter(_.split(",").le >=3).map(partition => {
          val str: Array[String] = partition.split(",")
          UserCall(str(0), str(1), str(2))
        })
        //构建df
        val  df: DataFrame = sqlContext.createDataFrame(userRDD)

        df.registerTempTable("user")
        sqlContext.sql("select * from user").show()



        /*        sqlContext.sql(
                  """|select
                    |f.call,
                    |f.callTime,
                    |f.set
                    |from
                    |(
                    |               select  t.call,MAX(t.callTime) callTime , collect_set(t.callAdress) as  set ,MAX(t.cha) max_cha ,MIN(t.cha) min_cha
                    |               from
                    |
                    |
                    |                       (select  a.call,a.callTime,a.rank, a.callAdress,nvl(abs( unix_timestamp(a.callTime )-unix_timestamp(b.callTime ) ),0)  cha
                    |
                    |
                    |
                    |                       from
                    |                       (
                    |
                    |                               (select t.call ,t.callTime,t.callAdress, t.rank  from (select call ,nvl(callTime,0) callTime,callAdress,row_number() over(partition by call order by callTime desc ) rank  from user )  t where t. rank <=2) a
                    |
                    |                                left join
                    |
                    |                               (select t.call ,t.callTime,t.callAdress, t.rank-1 rank  from (select call ,nvl(callTime,0) callTime,callAdress,row_number() over(partition by call order by callTime desc ) rank  from user )  t where t. rank <=2 ) b
                    |
                    |                               on a.call = b.call and a.rank = b.rank
                    |
                    |                       )
                    |                       where a.rank<=2 ) t   group by t.call ) f
                    |
                    |
                    | where size(f.set)=2
                    | and (f.max_cha - min_cha)  <= 3600
                  """.stripMargin).show()*/

        //sqlContext.dropTempTable("user")














        for(o<-offsetRanges){
          // /gp01/offset/tt/  0
          val zkpath = s"${TopicDirs.consumerOffsetDir}/${o.partition}"
          //将该partition的offset保存到zookeeper中
          // /gp01/offset/tt/  0/88889
          ZkUtils.updatePersistentPath(zkClient,zkpath,o.untilOffset.toString)
        }
    }

    // 启动
    ssc.start()
    ssc.awaitTermination()
  }

}
case  class UserCall(call : String , callTime: String , callAdress: String )

object SQLContextSingleton {
  @transient  private var instance: SQLContext = _
  def getInstance(sparkContext: SparkContext): SQLContext = {
    if (instance == null) {
      instance = new SQLContext(sparkContext)
    }
    instance
  }
}*/
