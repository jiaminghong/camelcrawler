import java.net.URL

import CrawlServer.{CrawlRequest, CrawlResponse}
import Database.{RetrieveEntry, Test}
import LinkChecker.Result
import Main.{database, system}
import akka.actor.{Actor, ActorRef, Props}

import scala.collection.mutable
import scala.collection.immutable.HashSet

object CrawlServer {
  case class CrawlRequest(url: String, depth: Integer) {}
  case class CrawlResponse(url: String, links: Set[String]) {}
}

class CrawlServer extends Actor {

  val clients: mutable.Map[String, Set[ActorRef]] = collection.mutable.Map[String, Set[ActorRef]]()
  val controllers: mutable.Map[String, ActorRef] = mutable.Map[String, ActorRef]()

  // ActorReferences
  val database = context.actorSelection("/user/DatabaseNode")


  def receive = {

    case CrawlRequest(url, depth) =>
      val controller = controllers get url
      if (controller.isEmpty) {
        controllers += (url -> context.actorOf(Props[LinkChecker](new LinkChecker(url, depth))))
        clients += (url -> Set.empty[ActorRef])

      }
      clients(url) += sender

    case Result(url, links) =>
      context.stop(controllers(url))
      clients(url) foreach (_ ! CrawlResponse(url, links))
      clients -= url
      controllers -= url
  }

}