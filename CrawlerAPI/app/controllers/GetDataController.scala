package controllers

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import akka.util.ByteString
import play.api.mvc._
import redis.RedisClient

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class GetDataController extends Controller {

//  def getBackLinks(s:String) = Action {
//    val system = ActorSystem()
//    val redis1 = RedisClient(db = Some(1))
//      val key = s+"_outbound"
//      val backlinks = redis1.lrange(s+"_outbound",0,-1)
//      backlinks onComplete {
//        case Success(urls) => for (url <- urls) yield url.utf8String
//        case Failure(t) => println("An error has occurred: " + t.getMessage)
//      }
//    Ok
//
//  }
  def getBackLinks(s:String) = Action {
  implicit val akkaSystem = akka.actor.ActorSystem()
    val redis1 = RedisClient(db = Some(1))
    val backlinks = redis1.lrange(s+"_outbound",0,-1)
    var listurl :Seq[ByteString] = Seq()
    backlinks onComplete {
      case Success(urls: Seq[ByteString]) => listurl = urls
      case Failure(t) => println("An error has occurred: " + t.getMessage)
    }
    Thread.sleep(5000)
    val lists: Seq[String] = for (a <- listurl) yield a.utf8String
    Ok(lists)
  }
}

object GetDataController extends App {
  implicit val akkaSystem = akka.actor.ActorSystem()
  val link = "https://www.firesticktricks.com/dmca-policy"
  var listurl :Seq[ByteString] = Seq()
  val redis1 = RedisClient(db = Some(1))
  val key = link+"_outbound"
  val backlinks: Future[Seq[ByteString]] = redis1.lrange(link+"_outbound",0,-1)
  backlinks onComplete {
    case Success(urls: Seq[ByteString]) => listurl = urls
    case Failure(t) => println("An error has occurred: " + t.getMessage)
  }
  Thread.sleep(5000)
  val lists: Seq[String] = for (a <- listurl) yield a.utf8String
  println("listurl" + lists)
}
