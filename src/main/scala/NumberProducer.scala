import org.joda.time.DateTime

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by lovepocky on 17/3/25.
  */
object NumberProducer {
  def main(args: Array[String]): Unit = {
    import java.util.{Date, Properties}
    import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

    import GlobalConfig._
    import org.json4s._
    import org.json4s.native.JsonMethods._
    import org.json4s.ext.JodaTimeSerializers
    implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

    val props = new Properties()
    props.put("bootstrap.servers", s"$kafkaHost:$kafkaPort")
    //props.put("client.id", "ScalaProducerExample")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    val idPrefix = util.Random.alphanumeric.take(5).mkString

    var id = 0

    while (true) {
      val data = compact(render(Extraction.decompose(SumMessage(s"$idPrefix-$id", util.Random.nextInt(100), DateTime.now()))))
      id = id + 1

      val message = new ProducerRecord[String, String](kafkaTopicName, "random_number", data)
      val result = producer.send(message)
      println(s"sending data: ${data}")
      Await.result(Future(result.get), Duration.Inf)
      Thread.sleep(1000)
    }
  }
}
