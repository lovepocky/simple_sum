# Simple Sum 

## kafka
[Kafka Quickstart](https://kafka.apache.org/quickstart)

按照官方入门教程运行一个kafka broker

```
> bin/zookeeper-server-start.sh config/zookeeper.properties
> bin/kafka-server-start.sh config/server.properties
> bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
```
zookeeper和kafka分别运行在`localhost:2181`和`localhost9092`，kafka topic为`test`

## number producer

运行src/main/scala/NumberProducer.main，产生随机数如下

```
sending data: 32
sending data: 21
sending data: 30
sending data: 71
sending data: 24
sending data: 37
sending data: 93
sending data: 75
sending data: 84
sending data: 29
sending data: 15
sending data: 35
sending data: 50
sending data: 59
sending data: 54
sending data: 28
sending data: 62
sending data: 31
sending data: 95
sending data: 44
sending data: 55
sending data: 0
sending data: 5
sending data: 67
sending data: 48
sending data: 18
sending data: 95
sending data: 37
sending data: 81
sending data: 12
sending data: 46
sending data: 42
sending data: 21
sending data: 3
sending data: 64
sending data: 44
sending data: 93
sending data: 74
sending data: 13
sending data: 4
sending data: 81
sending data: 80
sending data: 8
sending data: 32
sending data: 43
sending data: 76
sending data: 60
sending data: 65
sending data: 9
sending data: 15
sending data: 70
sending data: 1
sending data: 75
sending data: 26
sending data: 10
sending data: 96
sending data: 46
sending data: 26
sending data: 9
sending data: 85

Process finished with exit code 130 (interrupted by signal 2: SIGINT)
```

## spark streaming

运行src/main/scala/SimpleSum.main，从kafka获取数据  
随机数每1秒生成1条，spark streaming每5秒获取一次数据，因此每次取到5条数据

```
Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties
17/03/25 21:41:39 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
17/03/25 21:41:41 INFO VerifiableProperties: Verifying properties
17/03/25 21:41:41 INFO VerifiableProperties: Property group.id is overridden to spark_streaming
17/03/25 21:41:41 INFO VerifiableProperties: Property zookeeper.connect is overridden to localhost:2181
17/03/25 21:41:41 INFO VerifiableProperties: Property zookeeper.connection.timeout.ms is overridden to 10000
17/03/25 21:41:41 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], Connecting to zookeeper instance at localhost:2181
17/03/25 21:41:41 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], starting auto committer every 60000 ms
17/03/25 21:41:41 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], begin registering consumer spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b in ZK
17/03/25 21:41:41 INFO ZKCheckedEphemeral: Creating /consumers/spark_streaming/ids/spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b (is it secure? false)
17/03/25 21:41:41 INFO ZKCheckedEphemeral: Result of znode creation is: OK
17/03/25 21:41:41 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], end registering consumer spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b in ZK
17/03/25 21:41:41 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], starting watcher executor thread for consumer spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b
17/03/25 21:41:41 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], begin rebalancing consumer spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b try #0
17/03/25 21:41:41 INFO ConsumerFetcherManager: [ConsumerFetcherManager-1490449301550] Stopping leader finder thread
17/03/25 21:41:41 INFO ConsumerFetcherManager: [ConsumerFetcherManager-1490449301550] Stopping all fetchers
17/03/25 21:41:41 INFO ConsumerFetcherManager: [ConsumerFetcherManager-1490449301550] All connections stopped
17/03/25 21:41:41 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], Cleared all relevant queues for this fetcher
17/03/25 21:41:41 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], Cleared the data chunks in all the consumer message iterators
17/03/25 21:41:41 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], Committing all offsets after clearing the fetcher queues
17/03/25 21:41:41 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], Releasing partition ownership
17/03/25 21:41:41 INFO RangeAssignor: Consumer spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b rebalancing the following partitions: ArrayBuffer(0) for topic test with consumers: List(spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b-0)
17/03/25 21:41:41 INFO RangeAssignor: spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b-0 attempting to claim partition 0
17/03/25 21:41:41 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b-0 successfully owned partition 0 for topic test
17/03/25 21:41:41 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], Consumer spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b selected partitions : test:0: fetched offset = 544: consumed offset = 544
17/03/25 21:41:41 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], end rebalancing consumer spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b try #0
17/03/25 21:41:41 INFO ConsumerFetcherManager$LeaderFinderThread: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b-leader-finder-thread], Starting 
17/03/25 21:41:41 INFO VerifiableProperties: Verifying properties
17/03/25 21:41:41 INFO VerifiableProperties: Property client.id is overridden to spark_streaming
17/03/25 21:41:41 INFO VerifiableProperties: Property metadata.broker.list is overridden to 192.168.1.10:9092
17/03/25 21:41:41 INFO VerifiableProperties: Property request.timeout.ms is overridden to 30000
17/03/25 21:41:41 INFO ClientUtils$: Fetching metadata from broker BrokerEndPoint(0,192.168.1.10,9092) with correlation id 0 for 1 topic(s) Set(test)
17/03/25 21:41:41 INFO SyncProducer: Connected to 192.168.1.10:9092 for producing
17/03/25 21:41:41 INFO SyncProducer: Disconnecting from 192.168.1.10:9092
17/03/25 21:41:41 INFO ConsumerFetcherThread: [ConsumerFetcherThread-spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b-0-0], Starting 
17/03/25 21:41:41 INFO ConsumerFetcherManager: [ConsumerFetcherManager-1490449301550] Added fetcher for partitions ArrayBuffer([test-0, initOffset 544 to broker BrokerEndPoint(0,192.168.1.10,9092)] )
-------------------------------------------
Time: 1490449305000 ms
-------------------------------------------

-------------------------------------------
Time: 1490449310000 ms
-------------------------------------------

received number: Some(32), sum: 0 -> 32
-------------------------------------------
Time: 1490449315000 ms
-------------------------------------------
...

received number: Some(21), sum: 32 -> 53
received number: Some(30), sum: 53 -> 83
received number: Some(71), sum: 83 -> 154
received number: Some(24), sum: 154 -> 178
-------------------------------------------
Time: 1490449320000 ms
-------------------------------------------
...

received number: Some(37), sum: 178 -> 215
received number: Some(93), sum: 215 -> 308
received number: Some(75), sum: 308 -> 383
received number: Some(84), sum: 383 -> 467
received number: Some(29), sum: 467 -> 496
-------------------------------------------
Time: 1490449325000 ms
-------------------------------------------
...

received number: Some(15), sum: 496 -> 511
received number: Some(35), sum: 511 -> 546
received number: Some(50), sum: 546 -> 596
received number: Some(59), sum: 596 -> 655
received number: Some(54), sum: 655 -> 709
-------------------------------------------
Time: 1490449330000 ms
-------------------------------------------
...

received number: Some(28), sum: 709 -> 737
received number: Some(62), sum: 737 -> 799
received number: Some(31), sum: 799 -> 830
received number: Some(95), sum: 830 -> 925
received number: Some(44), sum: 925 -> 969
received number: Some(55), sum: 969 -> 1024
-------------------------------------------
Time: 1490449335000 ms
-------------------------------------------
...

received number: Some(0), sum: 1024 -> 1024
received number: Some(5), sum: 1024 -> 1029
received number: Some(67), sum: 1029 -> 1096
received number: Some(48), sum: 1096 -> 1144
-------------------------------------------
Time: 1490449340000 ms
-------------------------------------------
...

received number: Some(18), sum: 1144 -> 1162
received number: Some(95), sum: 1162 -> 1257
received number: Some(37), sum: 1257 -> 1294
received number: Some(81), sum: 1294 -> 1375
received number: Some(12), sum: 1375 -> 1387
-------------------------------------------
Time: 1490449345000 ms
-------------------------------------------
...

received number: Some(46), sum: 1387 -> 1433
received number: Some(42), sum: 1433 -> 1475
received number: Some(21), sum: 1475 -> 1496
received number: Some(3), sum: 1496 -> 1499
received number: Some(64), sum: 1499 -> 1563
-------------------------------------------
Time: 1490449350000 ms
-------------------------------------------
...

received number: Some(44), sum: 1563 -> 1607
received number: Some(93), sum: 1607 -> 1700
received number: Some(74), sum: 1700 -> 1774
received number: Some(13), sum: 1774 -> 1787
received number: Some(4), sum: 1787 -> 1791
-------------------------------------------
Time: 1490449355000 ms
-------------------------------------------
...

received number: Some(81), sum: 1791 -> 1872
received number: Some(80), sum: 1872 -> 1952
received number: Some(8), sum: 1952 -> 1960
received number: Some(32), sum: 1960 -> 1992
received number: Some(43), sum: 1992 -> 2035
-------------------------------------------
Time: 1490449360000 ms
-------------------------------------------
...

received number: Some(76), sum: 2035 -> 2111
received number: Some(60), sum: 2111 -> 2171
received number: Some(65), sum: 2171 -> 2236
received number: Some(9), sum: 2236 -> 2245
received number: Some(15), sum: 2245 -> 2260
-------------------------------------------
Time: 1490449365000 ms
-------------------------------------------
...

received number: Some(70), sum: 2260 -> 2330
received number: Some(1), sum: 2330 -> 2331
received number: Some(75), sum: 2331 -> 2406
received number: Some(26), sum: 2406 -> 2432
received number: Some(10), sum: 2432 -> 2442
-------------------------------------------
Time: 1490449370000 ms
-------------------------------------------
...

received number: Some(96), sum: 2442 -> 2538
received number: Some(46), sum: 2538 -> 2584
received number: Some(26), sum: 2584 -> 2610
received number: Some(9), sum: 2610 -> 2619
received number: Some(85), sum: 2619 -> 2704
-------------------------------------------
Time: 1490449375000 ms
-------------------------------------------
...

-------------------------------------------
Time: 1490449380000 ms
-------------------------------------------

-------------------------------------------
Time: 1490449385000 ms
-------------------------------------------

17/03/25 21:43:05 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], ZKConsumerConnector shutting down
17/03/25 21:43:05 INFO ConsumerFetcherManager: [ConsumerFetcherManager-1490449301550] Stopping leader finder thread
17/03/25 21:43:05 INFO ConsumerFetcherManager$LeaderFinderThread: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b-leader-finder-thread], Shutting down
17/03/25 21:43:05 INFO ConsumerFetcherManager$LeaderFinderThread: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b-leader-finder-thread], Shutdown completed
17/03/25 21:43:05 INFO ConsumerFetcherManager$LeaderFinderThread: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b-leader-finder-thread], Stopped 
17/03/25 21:43:05 INFO ConsumerFetcherManager: [ConsumerFetcherManager-1490449301550] Stopping all fetchers
17/03/25 21:43:05 INFO ConsumerFetcherThread: [ConsumerFetcherThread-spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b-0-0], Shutting down
17/03/25 21:43:05 INFO ConsumerFetcherThread: [ConsumerFetcherThread-spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b-0-0], Stopped 
17/03/25 21:43:05 INFO ConsumerFetcherThread: [ConsumerFetcherThread-spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b-0-0], Shutdown completed
17/03/25 21:43:05 INFO ConsumerFetcherManager: [ConsumerFetcherManager-1490449301550] All connections stopped
17/03/25 21:43:05 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], ZKConsumerConnector shutdown completed in 22 ms
17/03/25 21:43:05 ERROR ReceiverTracker: Deregistered receiver for stream 0: Stopped by driver
17/03/25 21:43:05 INFO ZookeeperConsumerConnector: [spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b], stopping watcher executor thread for consumer spark_streaming_lovepockydeMBP15.local-1490449301490-befa263b

Process finished with exit code 130 (interrupted by signal 2: SIGINT)

```