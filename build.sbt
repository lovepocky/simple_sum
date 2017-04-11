name := "kafka_sparkstreaming_simple_sum"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.1.0" % "provided"
  , "org.apache.spark" % "spark-streaming_2.11" % "2.1.0" % "provided"
  , "org.apache.spark" % "spark-streaming-kafka-0-8_2.11" % "2.1.0"

  , "org.apache.kafka" % "kafka_2.11" % "0.10.1.0"
)

logLevel := Level.Warn

assemblyJarName in assembly := name.value + ".jar"

//sbt-assembly: Merge Errors - Deduplicate
//http://stackoverflow.com/a/38291175/5570244
assemblyMergeStrategy in assembly := {
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}