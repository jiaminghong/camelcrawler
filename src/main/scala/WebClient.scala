import java.util.concurrent.Executors

import scala.concurrent.Future
import com.ning.http.client.{AsyncCompletionHandler, AsyncHttpClient, AsyncHttpClientConfig, Response}

import scala.None
import scala.concurrent.Promise


object WebClient {

  val config = new AsyncHttpClientConfig.Builder()
  val client = new AsyncHttpClient(config
    .setFollowRedirects(true)
    .setExecutorService(Executors.newWorkStealingPool(64))
    .build())


  def get(url:String,promise:Promise[String]): Future[String] = {
    //val promise = Promise[String]()
    val request = client.prepareGet(url).build()

    client.executeRequest(request, new AsyncCompletionHandler[Response]() {
      override def onCompleted(response: Response): Response = {
        promise.success(response.getResponseBody)
        response
        }
      override def onThrowable(t: Throwable): Unit = {
        promise.failure(t)
      }

    })

    promise.future
  }

  // Request only for Head
  def preGet(url: String): Future[String] = {
    val promise = Promise[String]()
    val rqHead = client.prepareHead(url).build()


    val resultHead = client.prepareHead(url).execute()
    if(resultHead.get().getContentType.contains("text/html")){
      get(url,promise)
    }

    /*
    client.executeRequest(rqHead, new AsyncCompletionHandler[Response]() {
      override def onCompleted(response: Response): Response = {
        promise.success(response.getResponseBody)
        if(response.getContentType.contains("texthtml")) // Proceed to download the entire HTML
          get(url,promise)
          response
      }
      override def onThrowable(t: Throwable): Unit = {
        promise.failure(t)
      }

    })
    */

    promise.future
  }
}

/*

  def get(url: String): Future[String] = {
    val promise = Promise[String]()
    val request = client.prepareGet(url).build()



    promise.future
  }

 */