import sbt._
import Keys._


name := "kafkainjestion"

version := "1.0"

scalaVersion := "2.11.8"



libraryDependencies += "org.postgresql" % "postgresql" % "42.1.0"
libraryDependencies += "joda-time" % "joda-time" % "2.9.4"
libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.7.4"
libraryDependencies += "org.apache.hadoop" % "hadoop-aws" % "2.7.4"
libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "2.7.4"
libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "2.7.4"
libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1"
libraryDependencies += "org.mongodb" % "mongo-java-driver" % "3.5.0"
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.2.0"
libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.2.0"
libraryDependencies += "org.apache.spark" % "spark-hive_2.11" % "2.2.0"
libraryDependencies += "org.mongodb.spark" % "mongo-spark-connector_2.10" % "2.2.0"
libraryDependencies += "com.crealytics" % "spark-excel_2.11" % "0.9.5"
libraryDependencies += "com.databricks" %% "spark-csv" % "1.5.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.2.0" % "provided"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "0.11.0.2"
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.2.0"
libraryDependencies += "org.apache.avro" % "avro" % "1.8.2"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.2.0"


unmanagedBase := baseDirectory.value / "lib"

assemblyMergeStrategy in assembly := {

      case "META-INF/services/org.apache.spark.sql.sources.DataSourceRegister" => MergeStrategy.concat

      case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard    
      case PathList("org", "apache", "hadoop", "yarn", xs @ _*) => MergeStrategy.first
      case PathList("org", "objenesis", xs @ _*) => MergeStrategy.first

      case PathList("com.sun.jersey", "jersey-server", "bundles",xs @ _*) => MergeStrategy.discard
 
      case PathList("org", "apache", "spark","unused",xs @ _*) => MergeStrategy.first
      
      case PathList("org", "apache", "commons","logging", xs @ _*) => MergeStrategy.first

      case PathList("org", "apache", "commons","collections",xs @ _*) => MergeStrategy.first

      case PathList("org", "apache", "commons","beanutils",xs @ _*) => MergeStrategy.first

      case PathList("org", "aopalliance", "intercept",xs @ _*) => MergeStrategy.first

      case PathList("org", "aopalliance", "aop",xs @ _*) => MergeStrategy.first

      
      case PathList("javax", "xml",  xs @ _*) => MergeStrategy.first

      case PathList("javax", "ws",  xs @ _*) => MergeStrategy.first
      
      case PathList("javax", "servlet",  xs @ _*) => MergeStrategy.first

      case PathList("javax", "inject",  xs @ _*) => MergeStrategy.first

      case PathList("javax", "annotation",  xs @ _*) => MergeStrategy.first
    
      case PathList("javax", "activation",  xs @ _*) => MergeStrategy.first
      
      case PathList("com", "sun", "research","ws", xs @ _*) => MergeStrategy.first
    
      case PathList("org", "xerial", "snappy",xs @ _*) => MergeStrategy.first
    
      case PathList("org", "jets3t", "service",xs @ _*) => MergeStrategy.first

      case PathList("org", "slf4j",xs @ _*) => MergeStrategy.first  
   
      case PathList("org", "jboss","netty",xs @ _*) => MergeStrategy.first  

     case PathList("org", "codehaus","jackson",xs @ _*) => MergeStrategy.first    

     case PathList("org", "apache","http",xs @ _*) => MergeStrategy.first 

     case PathList("org", "apache","hadoop",xs @ _*) => MergeStrategy.first

      case PathList("org", "apache","curator",xs @ _*) => MergeStrategy.first
     
      case PathList("org", "apache","commons",xs @ _*) => MergeStrategy.first

     case PathList("org", "apache","avro",xs @ _*) => MergeStrategy.first

      case PathList("plugin.xml") => MergeStrategy.first
      
      case PathList("overview.html") => MergeStrategy.first
     
      case PathList("parquet.thrift") => MergeStrategy.first

      case "reference.conf" => MergeStrategy.concat 

      
       case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
     oldStrategy(x)
              
}    

EclipseKeys.withSource := true 

