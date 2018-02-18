package com.DAO
import java.sql.{ Connection, Driver, DriverManager, Statement, PreparedStatement, ResultSet, ResultSetMetaData, SQLException }
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

object LocationDAO {
  def insertLocationInfo(connection: Connection, statement: Statement, locationInfo: String, zipCode: String) = {
    val parser = new JSONParser()
    val locationObject = parser.parse(locationInfo).asInstanceOf[JSONObject]
    val country = locationObject.get("country").asInstanceOf[String]
    val city = locationObject.get("city").asInstanceOf[String]
    val state = locationObject.get("state").asInstanceOf[String]
    val query = s"""
          INSERT INTO location(zipcode,country,city,state)
          VALUES ('$zipCode','$country','$city','$state')
          ON CONFLICT(zipcode)
          DO NOTHING
      """
    statement.addBatch(query)
  }
}