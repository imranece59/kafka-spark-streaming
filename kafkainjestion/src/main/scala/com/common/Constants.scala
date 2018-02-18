package com.common

object Constants {
  final val SPARK_MASTER = "local"
  final val BOOTSTRAP_ZOOKEEPER_SERVERS = "localhost:9092"
  final val PRODUCER_GROUP = "connect-cluster"
  final val POSTGRES_CONN_URL = "jdbc:postgresql://localhost:5432/poc?user=postgres&password="
  final val ORDER_TOPICS = "test-postgresql-jdbc-orders"

}