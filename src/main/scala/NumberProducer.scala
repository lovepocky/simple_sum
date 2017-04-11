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

    val props = new Properties()
    props.put("bootstrap.servers", s"$kafkaHost:$kafkaPort")
    //props.put("client.id", "ScalaProducerExample")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    while (true) {
      val number = util.Random.nextInt(100)
      val data = new ProducerRecord[String, String](kafkaTopicName, "random_number", number.toString)
      val result = producer.send(data)
      println(s"sending data: ${number}")
      Await.result(Future(result.get), Duration.Inf)
      Thread.sleep(1000)
    }
  }
}
