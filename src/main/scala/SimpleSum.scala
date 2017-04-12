
/**
  * Created by lovepocky on 17/3/25.
  */
object SimpleSum {

  def main(args: Array[String]): Unit = {

    import org.apache.log4j.{Level, LogManager}

    import org.apache.spark._
    import org.apache.spark.streaming._
    import org.apache.spark.streaming.kafka.KafkaUtils

    import GlobalConfig._

    import org.json4s._
    import org.json4s.native.JsonMethods._
    import org.json4s.ext.DateTimeSerializer

    val conf = new SparkConf().setMaster(sparkMaster).setAppName(sparkAppName)
    val ssc = new StreamingContext(conf, Seconds(5))
    ssc.checkpoint(sparkCheckPoint)

    val singleInputDStream = KafkaUtils.createStream(ssc, zkQuorum = s"$zkHost:$zkPort", groupId = kafkaGroupId, topics = kafkaTopics)

    val inputDStream = singleInputDStream.repartition(2)

    val spec =
      StateSpec.function {
        (key: String, value: Option[String], state: State[Int]) =>
          implicit val formats = DefaultFormats + DateTimeSerializer
          val originState = state.getOption().getOrElse(0)
          val newValue = value.flatMap(x => parse(x).extractOpt[SumMessage])
          val newState = originState + newValue.map(_.value).getOrElse(0)
          state.update(newState)
          println(s"received data: ${value}, sum: $originState -> $newState")
          state.get()
      }

    inputDStream.mapWithState(spec).print(10)

    LogManager.getLogger("org").setLevel(Level.ERROR)
    ssc.start()
    ssc.awaitTermination()
  }
}
