# Overview
The challenge is to build an exemplary ETL-DWH-Visualization pipeline using Kafka, PostgreSQL and MicroStrategy. The ETL aims at integrating information from a source database with information available from a public API using Kafka as the medium to move data around. The DWH and Visualization tool includes setting up a reasonable schema and building dashboards using MicroStrategy.
Prerequisites
The assignment requires availability of a personal laptop with Internet access.
Setup
In order to be completed, the assignment requires some infrastructure tooling to be in place. You should be able to proceed as follows:
Install Kafka locally on your laptop. Here we want to check your ability to apply our technologies and your ability to learn fast (if you do not have experience with Kafka). If you struggle to apply Kafka you also can do this part with a scripted solution
Install PostgreSQL locally on your laptop
Install MicroStrategy desktop client (either the Mac client or the Windows client)

After this is done, you need to create the source database and import the provided data. You should be able to create a new schema in your local PostgreSQL containing one table called orders with the following fields:
order_id (integer)
delivery_date (date)
user_id (integer)
zipcode (varchar)
total (double)
item_count (integer)

After the schema is available, you should be able to import the data in this CSV (you can use PostgreSQL’s COPY command).
# Requirements
The main goal is to build a DWH from the data contained in the source database in order for the business stakeholders to perform geographical analysis of the user base and revenue streams and to visualize two main KPI on top of it.
The source table includes information at the zipcode level, but the business stakeholders want to be able to perform analysis based on city information. Your task is to implement an ETL process using Kafka that loads data from both the source database and the free https://ziptasticapi.com, using the zipcode information contained in the former to resolve information about the city it belongs to through mentioned API.

After the ETL is built and the DWH populated, you should connect the MicroStrategy client to the DWH and build visualizations for the following KPI:
Total revenue per city
Week by week order frequency in a line graph (the graph should contain a filter by city)

Finally, we would like you to suggest any additional KPI that comes to your mind based on the data available on hand.



# DWH design Approach

As we don’t have much attributes here I am simply desiging the below 3 flat dimension tables

1. Orders Dimension 

2. Date Dimesion 

3. Location Dimension

Note : For the larger Datawarehousing perspective, We can keep some daily, weekly, monthly aggregated facts tables. I am not including any aggregated fact tables for this demo as this dealt with lesser records and attributes

# ER Diagram

Refer : ER_Diagram.png



# Infrastructure

Refer : Infrastructure.png



# Design:-
1. Install the confluent JDBC source postgres kafka connector from here https://www.confluent.io/product/connectors/

2.  Confluent connector is connected to postgres orders table for capturing the incoming streams(CDC - change Data Capture). For the Demo purpose, I had kept incremental column to identify Change Capture. In production environment we should keep either timestamp or Incremental & Timestamp column to capture the changes.

3. Capture the order streams through confluent kafka connector  and process the messages from spark streaming.

4. pass the zipcode from order stream to  https://ziptasticapi.com API to get city/state/country operation and load the Location table

5. Connect to Order/Date/Location Dimensions from the Microstrategy Dashboard for the visualization

# Installation & Run

1. Start the confluent kafka connector in the below order
(For the Demo purpose , start Kakfa in standalone mode)


	./bin/zookeeper-server-start ./etc/kafka/zookeeper.properties

	 ./bin/kafka-server-start /opt/confluent-4.0.0/etc/kafka/server.properties

	 ./bin/connect-standalone etc/schema-registry/connect-json-standalone.properties etc/kafka-	connect-jdbc/source-quickstart-sqlite.properties

	 ./bin/confluent load jdbc-source

Note :- set the below parameters in the /etc/kafka-connect-jdbc/source-quickstart-sqlite.properties before starting

connection.url=jdbc:postgresql://localhost:5432/poc?user=postgres&password=postgres
mode=incrementing
incrementing.column.name=order_id
topic.prefix=test-postgresql-jdbc-

2. check the messages by running the below consumer

./bin/kafka-console-consumer --bootstrap-server localhost:9092 --topic test-postgresql-jdbc-orders –from-beginning

mohamedimran@imran:/opt/confluent-4.0.0$ ./bin/kafka-console-consumer --bootstrap-server localhost:9092 --topic test-postgresql-jdbc-orders --from-beginning
{"schema":{"type":"struct","fields":[{"type":"int32","optional":false,"field":"order_id"},{"type":"int32","optional":true,"name":"org.apache.kafka.connect.data.Date","version":1,"field":"delivery_date"},{"type":"int32","optional":true,"field":"user_id"},{"type":"string","optional":true,"field":"zipcode"},{"type":"double","optional":true,"field":"total"},{"type":"int32","optional":true,"field":"item_count"}],"optional":false,"name":"orders"},"payload":{"order_id":3976843,"delivery_date":17350,"user_id":51344,"zipcode":"10028","total":61.5,"item_count":6}}
{"schema":{"type":"struct","fields":[{"type":"int32","optional":false,"field":"order_id"},{"type":"int32","optional":true,"name":"org.apache.kafka.connect.data.Date","version":1,"field":"delivery_date"},{"type":"int32","optional":true,"field":"user_id"},{"type":"string","optional":true,"field":"zipcode"},{"type":"double","optional":true,"field":"total"},{"type":"int32","optional":true,"field":"item_count"}],"optional":false,"name":"orders"},"payload":{"order_id":3977178,"delivery_date":17350,"user_id":153778,"zipcode":"90034","total":61.5,"item_count":6}}

3. Start the Spark Streaming with Master as local and messages will be captured whenever if there is a new entry into orders table with the new incremental order_id




