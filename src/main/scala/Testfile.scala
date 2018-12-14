import java.util.concurrent.Executors

import Database.RetrieveEntry
import akka.actor.{ActorSystem, Props}
import com.ning.http.client.{AsyncCompletionHandler, AsyncHttpClient, AsyncHttpClientConfig, Response}


class Testfile {

}

object Testfile extends App {

  val config = new AsyncHttpClientConfig.Builder()
  val client = new AsyncHttpClient(config
    .setFollowRedirects(true)
    .setExecutorService(Executors.newWorkStealingPool(64))
    .build())

  val resultHead = client.prepareHead("http://bbc.co.uk").execute()



  @throws(classOf[java.io.IOException])
  @throws(classOf[java.net.SocketTimeoutException])
  def get(url: String,
          connectTimeout: Int = 5000,
          readTimeout: Int = 5000,
          requestMethod: String = "HEAD") =
  {
    import java.net.{URL, HttpURLConnection}
    val connection = (new URL(url)).openConnection.asInstanceOf[HttpURLConnection]
    connection.setConnectTimeout(connectTimeout)
    connection.setReadTimeout(readTimeout)
    connection.setRequestMethod(requestMethod)
    val inputStream = connection.getInputStream
    val content = io.Source.fromInputStream(inputStream).mkString
    if (inputStream != null) inputStream.close
    content
  }
//  val system = ActorSystem();
//  val database = system.actorOf(Props [Database],"DatabaseNode")
//  database ! RetrieveEntry("https://www.firesticktricks.com/")
}