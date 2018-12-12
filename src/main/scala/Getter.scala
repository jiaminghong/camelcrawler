import java.net.URL

import Database.TraceEntry
import akka.actor.{Actor, Status}
import org.apache.commons.validator.routines.UrlValidator
import org.jsoup.Jsoup

import scala.collection.JavaConverters._
import scala.util.{Failure, Success}


object Getter {

  case class Done() {}

  case class Abort() {}

}

class Getter(url: String, depth: Int) extends Actor {

  import Getter._
  implicit val ec = context.dispatcher
  val currentHost = new URL(url).getHost

  val database = context.actorSelection("/user/DatabaseNode")
  val sourceURL = url
  val urlValidator = new UrlValidator()

  WebClient.preGet(url) onComplete {
    case Success(body) => self ! body
    case Failure(err) => self ! Status.Failure(err)
  }


  def getAllLinks(content: String): Iterator[String] = {

    Jsoup.parse(content, this.url).select("a[href]").iterator().asScala.map(_.absUrl("href"))

  }

  def receive = {
    case body: String => {
      val extensions = List("mailTo", "tif", "jpg", "svg", "#")

      getAllLinks(body)
        .filter(link => link != null && link.length > 0)
        .filter(link => !extensions.exists(e => link.matches(s".*\\.$e$$")))
        .filter(link => !link.contains("#"))
        .filter(link => urlValidator.isValid(link))
        .foreach(link => {
          context.parent ! LinkChecker.CheckUrl(link, depth)
          database ! TraceEntry(sourceURL, link) // Source -> Current Link AKA OutBound Link
        })
      stop()
      //context.self ! Abort
    }
    case _: Status.Failure => stop()

    case Abort => stop()

  }

  def stop(): Unit = {
    context.parent ! Done
    context.stop(self)
  }

}

/*
ZombieCode:
.filter(link => !link.contains("mailto"))
//.filter(link =>  currentHost  == new URL(link).getHost)
 */