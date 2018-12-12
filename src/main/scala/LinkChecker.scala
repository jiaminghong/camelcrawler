import java.net.URL

import Database.{RootEntry, Test}
import Getter.Done
import LinkChecker.{CheckUrl, Result}
import akka.actor.{Actor, ActorRef, Props, ReceiveTimeout}

import scala.collection.immutable.HashSet
import scala.concurrent.duration._
object LinkChecker {

  case class CheckUrl(url: String, depth: Int) {}

  case class Result(url: String, links: Set[String]) {}

}

class LinkChecker(root: String, originalDepth: Integer) extends Actor {

  var cache = Set.empty[String]
  var children = Set.empty[ActorRef]

  self ! CheckUrl(root, originalDepth)
  context.setReceiveTimeout(10 seconds)

  // ActorReferences
  val database = context.actorSelection("/user/DatabaseNode")

  // Domain Storage
  var rootDomains: HashSet[String] = HashSet()

  def receive = {
    case CheckUrl(url, depth) => {
      if(!rootDomains(new URL(url).getAuthority)) { // build the root domain db
        rootDomains += new URL(url).getAuthority
        database ! RootEntry(new URL(url).getAuthority)
        }

      if (!cache(url) && depth > 0) { // If URL doesn't exist in the cache, add it
        print(s"Crawl: ${url} at depth: ${depth}\n")
        children += context.actorOf(Props[Getter](new Getter(url, depth+1)))
      }
      cache += url
    }

    case Done =>
      children -= sender
      if (children.isEmpty) context.parent ! Result(root, cache)

    case ReceiveTimeout => children foreach (_ ! Getter.Abort)
  }
}