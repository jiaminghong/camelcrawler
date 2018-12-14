package controllers

import java.io.{File, PrintWriter}

import akka.actor.{ActorSystem, Props}
import akka.util.ByteString
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.regression.LabeledPoint
import play.api.http.Writeable
import play.api.libs.json.{JsObject, JsValue, Json, Writes}
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
  def convertObjectsToJsonOrig(urls: Seq[URL1]): JsValue = {
    Json.toJson(
      urls.map { t =>
        Map("link" -> t.link)
      }
    )
  }

  def convertObjectsToJsonOrig1(url: URL): JsValue = {
    Json.toJson(Map("link"->url.link,"spam"->url.spam))
  }

  implicit val akkaSystem = akka.actor.ActorSystem()
  implicit val jsonwrite = new Writes[URL1] {
    def writes(url: URL1) = Json.obj(
      "link" -> url.link
    )
  }
  implicit val jsonwrite1 = new Writes[URL] {
    def writes(url: URL) = Json.obj(
      "link" -> url.link,
      "spam" -> url.spam
    )
  }


  def getBackLinks(s: String) = Action {
    implicit val akkaSystem = akka.actor.ActorSystem()
    val redis1 = RedisClient(db = Some(1))
    val backlinks = redis1.lrange(s + "_outbound", 0, 19)
    var listurl: Seq[ByteString] = Seq()
    backlinks onComplete {
      case Success(urls: Seq[ByteString]) => listurl = urls
      case Failure(t) => println("An error has occurred: " + t.getMessage)
    }
    Thread.sleep(5000)
    val lists: Seq[String] = for (a <- listurl) yield a.utf8String
    val URLs = for (li <- lists) yield URL1(li)
    val str = convertObjectsToJsonOrig(URLs)
    Ok(str)
  }

  def getBackLinkswithSpam(s: String) = Action {
    implicit val akkaSystem = akka.actor.ActorSystem()
    val spammy = spam(s)
    val URLs = URL(s,spammy.toString())
    val str = convertObjectsToJsonOrig1(URLs)
    Ok(str)
  }

  def spam(s: String): String = {
    val input = s
    val result0 = input.replaceAll("[^a-zA-Z0-9]+", ",")
    val a1 = "https"
    val a2 = "http";
    val a3 = "www";
    println(result0)
    val result1 = result0.replaceFirst(a1, "")
    //    println(result1)
    val result2 = result1.replaceFirst(a2, "")
    //    println(result2)
    val result3 = result2.replaceFirst(a3, "")
    //    println(result3)
    val pw = new PrintWriter(new File("/Users/zhangchi/Downloads/camelcrawler-master/input.txt"))
    pw.write(result3)
    pw.close

    val sc = new SparkContext(new SparkConf().setAppName("Spam").set("spark.driver.allowMultipleContexts", "true").setMaster("local[*]"))
    val good = sc.textFile("/Users/zhangchi/Downloads/camelcrawler-master/good.txt")
    val bad = sc.textFile("/Users/zhangchi/Downloads/camelcrawler-master/bad.txt")
    //    val good = sc.textFile("/Users/jiaminghong/Desktop/FinalProject/ham")
    //    val bad = sc.textFile("/Users/jiaminghong/Desktop/FinalProject/spam")
    val features = new HashingTF(numFeatures = 1000)
    val features_good = good.map(mail => features.transform(mail.split(",")))
    val features_bad = bad.map(mail => features.transform(mail.split(",")))
    val good_data = features_good.map(features => LabeledPoint(0, features))
    val bad_data = features_bad.map(features => LabeledPoint(1, features))
    val data = good_data.union(bad_data)
    data.cache()
    val Array(training, test) = data.randomSplit(Array(0.6, 0.4))
    //val model = NaiveBayes.train(training, lambda = 1.0)
    val logistic_Learner = new LogisticRegressionWithLBFGS()
    val model = logistic_Learner.run(training)
    val predictionLabel = test.map(x => (model.predict(x.features), x.label))

    val accuracy = 1.0 * predictionLabel.filter(x => x._1 == x._2).count() / training.count()
    println(accuracy)
    val Ming = sc.textFile("/Users/zhangchi/Downloads/camelcrawler-master/input.txt")
    val features_Ming = Ming.map(mail => features.transform(mail.split(",")))
    val Ming_data = features_Ming.map(features => LabeledPoint(100, features))
    val Ming_result = Ming_data.map(x => (model.predict(x.features), x.label))
    var spamm = ""
    if (predictionLabel.first()._1 == 0) {
      spamm = "good"
    }
    else {
      spamm = "bad"
    }
    spamm
  }

  case class URL(link : String, spam : String)
  case class URL1(link : String)

}


//object GetDataController extends App {
//  def convertObjectsToJsonOrig(urls: Seq[URL]): JsValue = {
//    Json.toJson(
//      urls.map { t =>
//        Map("link" -> t.link)
//      }
//    )
//  }
//
//  implicit val akkaSystem = akka.actor.ActorSystem()
//  implicit val jsonwrite = new Writes[URL] {
//    def writes(url: URL) = Json.obj(
//      "link" -> url.link
//    )
//  }
//  val link = "https://www.firesticktricks.com/dmca-policy"
//  var listurl: Seq[ByteString] = Seq()
//  val redis1 = RedisClient(db = Some(1))
//  val key = link + "_outbound"
//  val backlinks: Future[Seq[ByteString]] = redis1.lrange(link + "_outbound", 0, -1)
//  backlinks onComplete {
//    case Success(urls: Seq[ByteString]) => listurl = urls
//    case Failure(t) => println("An error has occurred: " + t.getMessage)
//  }
//  Thread.sleep(1000)
//  val lists: Seq[String] = for (a <- listurl) yield a.utf8String
//  println("listurl" + lists.length)
//  println(Json.toJson(lists))
//  val URLs = for (li <- lists) yield URL(li)
//  //val str = println(str)
//
//
//  def giveMEjsons() = {
//    println("worK");
//    play.mvc.Results.TODO;
//  }

// }



//def getBackLinks(s:String)(implicit w : Writeable[Seq[String]]) = Action {
//  implicit val akkaSystem = akka.actor.ActorSystem()
//  val redis1 = RedisClient(db = Some(1))
//  val backlinks = redis1.lrange(s+"_outbound",0,-1)
//  var listurl :Seq[ByteString] = Seq()
//  backlinks onComplete {
//  case Success(urls: Seq[ByteString]) => listurl = urls
//  case Failure(t) => println("An error has occurred: " + t.getMessage)
//}
//  Thread.sleep(5000)
//  val lists: Seq[String] = for (a <- listurl) yield a.utf8String
//  val URLs = for ( li <- lists) yield URL(li)
//  val str = convertObjectsToJsonOrig(URLs)
//  Ok(str)
//}




