
/**
  * Created by lovepocky on 17/3/25.
  */
object SimpleSum {

    def main(args: Array[String]): Unit = {

        import org.apache.log4j.{Level, LogManager}

        import org.apache.spark._
        import org.apache.spark.streaming._
        import org.apache.spark.streaming.kafka.KafkaUtils

        val conf = new SparkConf().setMaster("local[*]").setAppName("simple_sum")
        val ssc = new StreamingContext(conf, Seconds(5))
        ssc.checkpoint("temp")

        val inputDStream = KafkaUtils.createStream(ssc, zkQuorum = "localhost:2181", groupId = "spark_streaming", topics = Map("test" -> 1))

        val spec =
            StateSpec.function {
                (key: String, value: Option[String], state: State[Int]) =>
                    val originState = state.getOption().getOrElse(0)
                    val newState = originState + value.map(_.toInt).getOrElse(0)
                    state.update(newState)
                    println(s"received number: ${value}, sum: $originState -> $newState")
                    state.get()
            }

        inputDStream.mapWithState(spec).print(0)

        LogManager.getLogger("org").setLevel(Level.ERROR)
        ssc.start()
        ssc.awaitTermination()
    }
}
