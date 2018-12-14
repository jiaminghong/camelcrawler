import java.sql.{Connection, DriverManager, SQLException}

import com.mysql.jdbc.Statement

import scala.concurrent.Future
import redis.RedisClient

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Main1 {
//    implicit val akkaSystem = akka.actor.ActorSystem()
//
//    val redis = RedisClient()
//    val graph = redis.multi()
//    graph.del("a","b")
//    graph.set("a","a")
//    graph.exec()
//    graph.set("b","b")
//    val futurePong = redis.ping()
//    println("Ping sent!")
//    futurePong.map(pong => {
//      println(s"Redis replied with a $pong")
//    })
//    Await.result(futurePong, 5 seconds)
//
//    val futureResult = doSomething(redis)
//
//    Await.result(futureResult, 5 seconds)
//
//    akkaSystem.terminate()

    def restoreinbound(redis: RedisClient): Future[Boolean] = {
      // launch command set and del in parallel
      val s = redis.set("redis", "is awesome")
      val d = redis.del("i")
      val p = redis.lpush("list", "aaaa")
      val q = redis.lpush("list", "bbbb", "cccc")
      for {
        set <- s
        del <- d
        lpu <- p
        lpu1 <- q
        incr <- redis.incr("i")
        iBefore <- redis.get("i")
        incrBy20 <- redis.incrby("i", 20)
        iAfter <- redis.get("i")
      } yield {
        println("SET redis \"is awesome\"")
        println("DEL i")
        println("INCR i")
        println("INCRBY i 20")
        val ibefore = iBefore.map(_.utf8String)
        val iafter = iAfter.map(_.utf8String)
        println(s"i was $ibefore, now is $iafter")
        println(iafter == "20")
        iafter == "20"
      }
    }
}
