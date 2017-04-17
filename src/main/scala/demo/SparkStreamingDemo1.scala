package demo

import org.apache.log4j.{Level, LogManager}
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.ext.DateTimeSerializer
import schema.ExtractUtility

/**
  * Created by lovepocky on 17/4/17.
  */
object SparkStreamingDemo1 {
  def main(args: Array[String]): Unit = {

    //spark context config
    val checkpoint = "alluxio://192.168.3.14:19998/checkpoint/spark_streaming/demo1/"
    val conf = new SparkConf()
    val ssc = new StreamingContext(conf, Seconds(5))
    ssc.checkpoint(checkpoint)

    //kafka config
    val zkHost = "192.168.3.14"
    val zkPort = 2181
    val kafkaGroupId = "spark_streaming_demo"
    val kafkaTopicName = "test_simple_sum"
    val inputStream = KafkaUtils.createStream(ssc, zkQuorum = s"$zkHost:$zkPort", groupId = kafkaGroupId, topics = Map(kafkaTopicName -> 1))

    //run app
    val app = new SparkStreamingDemo1()
    app.run(inputStream)

    LogManager.getLogger("org").setLevel(Level.ERROR)
    ssc.start()
    ssc.awaitTermination()
  }
}

class SparkStreamingDemo1() {
  def run(inputStream: ReceiverInputDStream[(String, String)]) = {
    val extractedStream =
      inputStream
        //.map(simplePrint)
        .map(ExtractUtility.extract)

    //UserCreateEvent
    val userCreateEventStream = extractedStream.transform(_.collect { case x: schema.UserCreateEvent => x })

    userCreateEventStream
      .window(Seconds(60), Seconds(20))
      .transform(rdd =>
        rdd.groupBy(_.community_id)
        //.map { case (community_id, seq) => (community_id, seq.size) }
      )
      .foreachRDD { rdd =>
        println("-----------------------------start of 1.1")
        println(s"userCreateEventStream groupBy(_.community_id):")
        println(s"count: ")
        rdd.foreach(x => println(s"${x._1}, ${x._2.size}"))
        rdd.foreach(println)
        println("-----------------------------end of 1.1")
      }

    userCreateEventStream.foreachRDD { rdd =>
      println("-----------------------------start of 1.2")
      println(s"userCreateEventStream print")
      println(s"count: ${rdd.count()}")
      rdd.foreach(println)
      println("-----------------------------end of 1.2")
    }

    //UserLastSignInEvent
    val userLastSignInEventStream = extractedStream.transform(_.collect { case x: schema.UserLastSignInEvent => x })
    userLastSignInEventStream
      .foreachRDD { rdd =>
        println("-----------------------------start of 2.1")
        println(s"userLastSignInEventStream print")
        println(s"count: ${rdd.count()}")
        rdd.foreach(println)
        println("-----------------------------end of 2.1")
      }

  }

  val simplePrint: PartialFunction[(String, String), (String, String)] = PartialFunction[(String, String), (String, String)] {
    case (key: String, value: String) =>
      val ip = java.net.InetAddress.getLocalHost.getHostAddress
      println(s"simplePrint: received data: $key -> $value, process hostAddress: $ip")
      (key, value)
  }
}

