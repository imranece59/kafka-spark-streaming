package com.utils
import org.json.simple.JSONObject;
object CommonUtils {

  def getLocationInfo(zipCode: String): String = {
    var locationInfo: String = ""
    try {
      val url = s"https://ziptasticapi.com/$zipCode"
      locationInfo = get(url)
    } catch {
      case e: Exception =>
        locationInfo = null
    }
    return locationInfo
  }

  def get(
    url:            String,
    connectTimeout: Int    = 5000,
    readTimeout:    Int    = 5000,
    requestMethod:  String = "GET") =
    {
      import java.net.{ URL, HttpURLConnection }
      val connection = (new URL(url)).openConnection.asInstanceOf[HttpURLConnection]
      connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
      connection.setConnectTimeout(connectTimeout)
      connection.setReadTimeout(readTimeout)
      connection.setRequestMethod(requestMethod)
      val inputStream = connection.getInputStream
      val content = scala.io.Source.fromInputStream(inputStream).mkString
      if (inputStream != null) inputStream.close
      content
    }
}