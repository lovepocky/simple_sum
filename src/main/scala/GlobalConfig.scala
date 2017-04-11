/**
  * Created by lovepocky on 17/4/11.
  */
object GlobalConfig {

  val zkHost = "192.168.1.111"
  val zkPort = 2181

  val kafkaHost = "192.168.1.111"
  val kafkaPort = 9092

  val kafkaGroupId = "spark_streaming"
  val kafkaTopicName = "test_simple_sum"
  val kafkaTopics: Map[String, Int] = Map(kafkaTopicName -> 1)

  //val sparkMaster = "local[*]"
  val sparkMaster = "spark://192.168.1.111:7077"
  val sparkAppName = "simple_sum"
  val sparkCheckPoint = "temp_checkpoint"

}
