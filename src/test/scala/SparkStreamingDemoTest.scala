import demo.SparkStreamingDemo1
import org.apache.spark.SparkConf
import org.scalatest.WordSpec
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.kafka.KafkaUtils

/**
  * Created by lovepocky on 17/4/17.
  */
class SparkStreamingDemoTest extends WordSpec {

  "SparkStreamingDemoTest" should {
    "run demo1" in {
      val conf = new SparkConf().setAppName("SparkStreamingDemo1Test").setMaster("local[*]")
      val checkpoint = "checkpoint/spark_streaming/demo1"
      val ssc = new StreamingContext(conf, Seconds(5))
      ssc.checkpoint(checkpoint)

      //kafka config
      val zkHost = "192.168.3.14"
      val zkPort = 2181
      val kafkaGroupId = "spark_streaming_demo_test"
      val kafkaTopicName = "test_simple_sum"
      val inputStream = KafkaUtils.createStream(ssc, zkQuorum = s"$zkHost:$zkPort", groupId = kafkaGroupId, topics = Map(kafkaTopicName -> 1))

      val app = new SparkStreamingDemo1()
      app.run(inputStream)
      ssc.start()
      ssc.awaitTermination()
    }
  }

}
