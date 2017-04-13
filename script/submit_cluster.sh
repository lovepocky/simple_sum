#!/usr/bin/env bash

spark-submit \
--master spark://192.168.1.111:7077 \
--class SimpleSum \
--deploy-mode cluster \
http://192.168.1.9:9100/target/scala-2.11/kafka_sparkstreaming_simple_sum.jar print_sum
