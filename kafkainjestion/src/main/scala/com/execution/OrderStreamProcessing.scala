package com.execution

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.sql.types._
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties
import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.sql.SparkSession
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import com.common.Constants
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.sql.{ Connection, Driver, DriverManager, PreparedStatement, ResultSet, ResultSetMetaData, SQLException }
import com.utils.CommonUtils
import com.DAO.LocationDAO

object OrderStreamProcessing extends App {

  private val logger = Logger.getLogger(this.getClass)
  Logger.getLogger("org").setLevel(Level.INFO)
  Logger.getLogger("akka").setLevel(Level.INFO)
  Logger.getLogger("kafka").setLevel(Level.INFO)
  logger.setLevel(Level.INFO)

  val sparksession = SparkSession.builder
    .appName("OrderStreamProcessing")
    .master(s"${Constants.SPARK_MASTER}")
    .getOrCreate

  val sc = sparksession.sparkContext
  val ssc = new StreamingContext(sc, Seconds(30))
  val sqlContext = new org.apache.spark.sql.SQLContext(sc)
  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> s"${Constants.BOOTSTRAP_ZOOKEEPER_SERVERS}",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> s"${Constants.PRODUCER_GROUP}",
    "auto.offset.reset" -> "earliest",
    "enable.auto.commit" -> (false: java.lang.Boolean))
  val topics = Array(s"${Constants.ORDER_TOPICS}")
  val stream = KafkaUtils.createDirectStream[String, String](
    ssc,
    PreferConsistent,
    Subscribe[String, String](topics, kafkaParams))
  stream.foreachRDD { rdd =>
    {
      rdd.foreachPartition(part => {
        Class.forName("org.postgresql.Driver");
        val connection = DriverManager.getConnection(Constants.POSTGRES_CONN_URL)
        val statement = connection.createStatement()
        connection.setAutoCommit(false)
        val parser = new JSONParser()
        part.foreach { row =>
          val jsonParseObj = parser.parse(row.value.asInstanceOf[String]).asInstanceOf[JSONObject]
          val payLoad = jsonParseObj.get("payload").asInstanceOf[JSONObject]
          val locationInfo = CommonUtils.getLocationInfo(payLoad.get("zipcode").asInstanceOf[String])
          if (locationInfo != null) {
            LocationDAO.insertLocationInfo(connection, statement, locationInfo, payLoad.get("zipcode").asInstanceOf[String])
          }
        }
        statement.executeBatch()
        statement.close()
        connection.commit()
        connection.close()
      })
    }
  }
  ssc.start
  ssc.awaitTermination
  sparksession.stop
}