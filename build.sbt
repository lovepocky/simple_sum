name := "kafka_sparkstreaming"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
    "org.apache.spark" % "spark-streaming_2.11" % "2.1.0"
    , "org.apache.spark" % "spark-streaming-kafka-0-8_2.11" % "2.1.0"

    , "org.apache.kafka" % "kafka_2.11" % "0.10.1.0"
)

logLevel := Level.Warn