import org.joda.time.DateTime

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

    val conf = new SparkConf().setAppName(sparkAppName).setMaster(sparkMaster)
    val ssc = new StreamingContext(conf, Seconds(5))
    ssc.checkpoint(sparkCheckPoint)

    val singleInputDStream = KafkaUtils.createStream(ssc, zkQuorum = s"$zkHost:$zkPort", groupId = kafkaGroupId, topics = Map(kafkaTopicName -> 2))

    val inputDStream = singleInputDStream //singleInputDStream.repartition(2)

    val specUpdate =
      StateSpec.function {
        (key: String, value: Option[String], state: State[Int]) =>
          val ip = java.net.InetAddress.getLocalHost.getHostAddress

          implicit val formats = DefaultFormats + DateTimeSerializer
          val originState = state.getOption().getOrElse(0)
          val newValue = value.flatMap(x => parse(x).extractOpt[SumMessage])
          val newState = originState + newValue.map(_.value).getOrElse(0)
          state.update(newState)
          println(s"specUpdate: received data: $key -> $value, sum: $originState -> $newState, process hostAddress: $ip")
          (key, value.get)
      }.numPartitions(2)

    val specUpdate2 =
      StateSpec.function {
        (key: String, value: Option[String], state: State[Int]) =>
          val ip = java.net.InetAddress.getLocalHost.getHostAddress

          implicit val formats = DefaultFormats + DateTimeSerializer
          val originState = state.getOption().getOrElse(0)
          val newValue = value.flatMap(x => parse(x).extractOpt[SumMessage])
          val newState = originState + newValue.map(_.value).getOrElse(0)
          state.update(newState)
          println(s"specUpdate2: received data: $key -> $value, sum: $originState -> $newState, process hostAddress: $ip")
          (key, value.get)
      }

    val specPrint =
      StateSpec.function {
        (key: String, value: Option[String], state: State[Int]) =>
          val ip = java.net.InetAddress.getLocalHost.getHostAddress
          println(s"specPrint: received data: $key -> $value, current state: $state, process hostAddress: $ip")
          (key, value.get)
      }.numPartitions(2)

    val simplePrint = PartialFunction[(String, String), (String, String)] {
      case (key: String, value: String) =>
        val ip = java.net.InetAddress.getLocalHost.getHostAddress
        println(s"simplePrint: received data: $key -> $value, process hostAddress: $ip")
        (key, value)
    }

    args.headOption.getOrElse("print") match {
      case "print" =>
        inputDStream.map(simplePrint).print()
      case "print_sum" =>
        inputDStream.map(simplePrint).mapWithState(specUpdate).print()
      case "print_sum2" =>
        inputDStream.map(simplePrint).mapWithState(specPrint).mapWithState(specUpdate).mapWithState(specUpdate).print()
      case "print_sum2_sum" =>
        inputDStream.map(simplePrint).mapWithState(specPrint).mapWithState(specUpdate).mapWithState(specUpdate)
          .map { case (key, value) => (key + "-2", value) }.mapWithState(specUpdate2).print()
      case "sum_partition" =>
        inputDStream
          .map { case (message_key, message_value) =>
            implicit val formats = DefaultFormats + DateTimeSerializer
            val message = parse(message_value).extract[SumMessage]
            (message.key, message)
          }
          .mapWithState(StateSpec.function {
            (key: Int, value: Option[SumMessage], state: State[Int]) =>
              val ip = java.net.InetAddress.getLocalHost.getHostAddress
              val originState = state.getOption().getOrElse(0)
              val newState = originState + value.get.value
              println(s"print_sum_partition update: received data: $key -> $value, sum: $originState -> $newState, process hostAddress: $ip")
              state.update(newState)
              newState
          }.numPartitions(2)).print()
      case "window_sum" =>
        import org.apache.spark.streaming.Durations._
        inputDStream
          .map { case (message_key, message_value) =>
            implicit val formats = DefaultFormats + DateTimeSerializer
            val message = parse(message_value).extract[SumMessage]
            (message.key, Seq(message))
          }
          .reduceByKeyAndWindow(
            { (a, b) =>
              a ++ b
            }: (Seq[SumMessage], Seq[SumMessage]) => Seq[SumMessage]
            , seconds(60), seconds(30))
          .map { case (key, seq) =>
            val sum = seq.map(_.value).sum
            val display =
              s"""
                 |--------------------------------
                 |now: ${DateTime.now()}
                 |window: 60 seconds
                 |key: $key
                 |seq:
                 |${seq.fold("")((a, b) => s"$a\n$b")}
                 |sum: $sum
                """.stripMargin
            println("in map of (key, seq) => sum")
            (sum, display)
          }
          .foreachRDD { rdd =>
            rdd.foreachPartition { partition =>
              DatabaseConnection.dummyPool
              println(
                s"""
                   |---------------------------------------------------------------------------------------
                   |foreachRDD: rdd.foreachPartition: ${partition.fold("")((a, b) => s"$a\n$b")}
                   |---------------------------------------------------------------------------------------
                """.stripMargin)
            }
          }
      //.print()
      case _ =>
    }

    LogManager.getLogger("org").setLevel(Level.ERROR)
    ssc.start()
    ssc.awaitTermination()
  }
}
