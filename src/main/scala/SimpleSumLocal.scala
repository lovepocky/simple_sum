
/**
  * Created by lovepocky on 17/3/25.
  */
object SimpleSumLocal {

  def main(args: Array[String]): Unit = {

    import GlobalConfig._
    import org.apache.log4j.{Level, LogManager}
    import org.apache.spark._
    import org.apache.spark.streaming._
    import org.apache.spark.streaming.kafka.KafkaUtils
    import org.json4s._
    import org.json4s.ext.DateTimeSerializer
    import org.json4s.native.JsonMethods._

    val conf = new SparkConf().setAppName(sparkAppName).setMaster("local[1]")
    val ssc = new StreamingContext(conf, Seconds(5))
    ssc.checkpoint(sparkCheckPointLocal)

    val singleInputDStream = KafkaUtils.createStream(ssc, zkQuorum = s"$zkHost:$zkPort", groupId = kafkaGroupId, topics = Map(kafkaTopicName -> 2))

    val inputDStream = singleInputDStream //singleInputDStream.repartition(2)

    val spec =
      StateSpec.function {
        (key: String, value: Option[String], state: State[Int]) =>
          val ip = java.net.InetAddress.getLocalHost.getHostAddress

          implicit val formats = DefaultFormats + DateTimeSerializer
          val originState = state.getOption().getOrElse(0)
          val newValue = value.flatMap(x => parse(x).extractOpt[SumMessage])
          val newState = originState + newValue.map(_.value).getOrElse(0)
          state.update(newState)
          println(s"received data: ${value}, sum: $originState -> $newState, process hostAddress: $ip")
          state.get()
      }

    val simplePrint = PartialFunction[(String, String), String] {
      case (key: String, value: String) =>
        val ip = java.net.InetAddress.getLocalHost.getHostAddress
        println(s"received data: $value, process hostAddress: $ip")
        value
    }

    //inputDStream.mapWithState(spec).print(10)
    //inputDStream.map(simplePrint).print()
    inputDStream.foreachRDD { rdd =>
      rdd.foreachPartition { records =>
        val ip = java.net.InetAddress.getLocalHost.getHostAddress
        records.foreach { case (key, value) =>
          println(s"received data: $value, process hostAddress: $ip")
        }
      }
    }

    //LogManager.getLogger("org").setLevel(Level.ERROR)
    ssc.start()
    ssc.awaitTermination()
  }
}
