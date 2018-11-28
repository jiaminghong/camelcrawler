import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.language.postfixOps
import CrawlServer.{CrawlRequest, CrawlResponse}

object Main extends App {
  println(s"Current Time ${System.currentTimeMillis}")
  val system = ActorSystem();
//  val receptionist = system.actorOf(Props(new CrawlServer))1
   val receptionist = system.actorOf(Props [CrawlServer], "CrawlServer")
  val main = system.actorOf(Props[Main](new Main(receptionist, "http://www.northeastern.edu/", 2)), "BBCActor")
}

class Main(receptionist: ActorRef, url: String, depth: Integer) extends Actor {
  receptionist ! CrawlRequest(url, depth)
  def receive = {
    case CrawlResponse(root, links) =>
      println(s"Root: $root")
      println(s"Links: ${links.toList.sortWith(_.length < _.length).mkString("\n")}")
      println("=========")
      println(s"Current Time ${System.currentTimeMillis}")
  }
}