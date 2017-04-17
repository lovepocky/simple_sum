package producer

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.joda.time.DateTime
import org.json4s.ext.JodaTimeSerializers
import org.json4s.{DefaultFormats, Extraction}
import org.json4s.native.JsonMethods.{compact, render}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by lovepocky on 17/4/17.
  */
object EventProducer {

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future

  def main(args: Array[String]): Unit = {
    import java.util.{Date, Properties}
    import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

    import org.json4s._
    import org.json4s.native.JsonMethods._
    import org.json4s.ext.JodaTimeSerializers
    implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

    val host_peach = "192.168.3.14"

    val kafkaHost = host_peach
    val kafkaPort = 9092

    val props = new Properties()
    props.put("bootstrap.servers", s"$kafkaHost:$kafkaPort")
    //props.put("client.id", "ScalaProducerExample")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val kafkaTopicName = "test_simple_sum"
    val producer = new KafkaProducer[String, String](props)

    userCreateEventProducer(producer, kafkaTopicName)
    userLastSignInEventProducer(producer)
    println("over")
  }

  def userCreateEventProducer(producer: KafkaProducer[String, String], kafkaTopicName: String): Future[_] = Future.successful {
    implicit val formats = DefaultFormats ++ JodaTimeSerializers.all
    val idPrefix = util.Random.alphanumeric.take(5).mkString
    var id = 0
    while (true) {
      import schema.UserCreateEvent
      val data = compact(render(Extraction.decompose(UserCreateEvent(s"$idPrefix-$id", util.Random.nextInt(5).toString, DateTime.now()))))
      id = id + 1

      val message = new ProducerRecord[String, String](kafkaTopicName, "userCreateEventProducer", data)
      val result = producer.send(message)
      println(s"sending data (userCreateEventProducer): ${data}")
      Await.result(Future(result.get), Duration.Inf)
      Thread.sleep(util.Random.nextInt(500))
    }
  }

  def userLastSignInEventProducer(producer: KafkaProducer[String, String]): Future[_] = Future.successful {

  }
}
