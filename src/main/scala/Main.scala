import java.io.File
import java.net.URL

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.language.postfixOps
import CrawlServer.{CrawlRequest, CrawlResponse}
import Database._
import org.slf4j.LoggerFactory
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import scala.io.Source

object Main extends App {
  println(s"Current Time ${System.currentTimeMillis}")
  //Utility.readFile()
//  val rootURL = "https://en.wikipedia.org/wiki/Enrico_Fermi"
  //val rootURL = "http://www.dianping.com/beijing/food"

  val rootURL = "https://www.firesticktricks.com/"
  val system = ActorSystem();
  val receptionist = system.actorOf(Props [CrawlServer], "CrawlServer")
  val main = system.actorOf(Props[Main](new Main(receptionist, rootURL, 1)), "MainNode")
  val database = system.actorOf(Props [Database],"DatabaseNode")

  LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[Logger].setLevel(Level.WARN)
//  Thread.sleep(1000)
//  database ! Shutdown()

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