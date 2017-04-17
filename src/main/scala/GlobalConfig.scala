import org.joda.time.DateTime

/**
  * Created by lovepocky on 17/4/11.
  */
object GlobalConfig {

  val host_peach = "192.168.3.14"

  val zkHost = host_peach
  val zkPort = 2181

  val kafkaHost = host_peach
  val kafkaPort = 9092

  val kafkaGroupId = "spark_streaming"
  val kafkaTopicName = "test_simple_sum"
  //val kafkaTopics: Map[String, Int] = Map(kafkaTopicName -> 1)

  //val sparkMaster = "local[*]"
  val sparkMaster = s"spark://$host_peach:7077"
  val sparkAppName = "simple_sum"
  //val sparkCheckPoint = "temp_checkpoint"
  val sparkCheckPointLocal = "temp_checkpoint"
  val sparkCheckPoint = s"alluxio://$host_peach/simple_sum/checkpoint"

  case class SumMessage(id: String, key: Int, value: Int, time: DateTime)

}
