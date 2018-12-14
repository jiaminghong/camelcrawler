
import java.sql.{Connection, DriverManager, PreparedStatement, SQLException}

import Database._
import akka.actor.Actor
import com.mysql.jdbc.Statement
import redis.RedisClient

import scala.language.postfixOps
import scala.util.{Failure, Random, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object Database {

  case class Entry(domain:String) {}
  case class Shutdown() {}
  case class RetrieveEntry(d:String) {}
  case class RootEntry(d:String){}
  case class TraceEntry(source:String,end:String){}
  case class Test(msg:String){}
}

class Database extends Actor {
  implicit val akkaSystem = Main.system
  val url = "jdbc:mysql://localhost:3306/web-crawler-db"
  val driver = "com.mysql.jdbc.Driver"
  val username = "root"
  val password = "admin"
  var connection:Connection = _
  var redis0: RedisClient = RedisClient(db = Some(0))
  var redis1 : RedisClient= RedisClient(db = Some(1))

  val statement:Statement = null


  override def receive: Receive = {
//    case Test(msg:String) => {
//      println("Message => " + msg)
//    }
//
//    case Entry(d: String) => {
//
//      try {
//          val redis0 = RedisClient(db = Some(0))
//          val futureResult = doSomething(redis)
//      } catch {
//        case e: Exception => e.printStackTrace
//      } finally {
//        try {if(statement!=null) statement.close()} catch{ case e:SQLException => }
//        try {if(connection!=null) connection.close()} catch{ case e:SQLException => }
//      }
//
//    }
//    case RetrieveEntry() =>
//      try {
//        Class.forName(driver)
//        connection = DriverManager.getConnection(url, username, password)
//        val statement = connection.createStatement
//        val rs = statement.executeQuery("SELECT * FROM domain_table")
//        while (rs.next) {
//          val domainName = rs.getString("domain_url")
//          val inboundName = rs.getString("inbound_link")
//
//          println("Domain = %s, Inbound = %s".format(domainName, inboundName))
//        }
//      } catch {
//        case e: Exception => e.printStackTrace
//      }

//    case Shutdown() => {
//      connection.close
//    }
//
//    case RootEntry(d:String) => {
//
//        try {
//          Class.forName(driver)
//          connection = DriverManager.getConnection(url, username, password)
//          connection.setAutoCommit(false)
//          val statement = connection.createStatement
//          val query = ("INSERT INTO root_domain_table VALUES (0,\"%s\")").format(d)
//
//          statement.addBatch(query)
//          statement.executeBatch()
//          connection.commit()
//
//        } catch {
//          case e: SQLException  => e.printStackTrace
//        } finally {
//        try {if(statement!=null) statement.close()} catch{ case e:SQLException => }
//        try {if(connection!=null) connection.close()} catch{ case e:SQLException => }
//
//      }
//      }

      case TraceEntry(source:String,end:String) => {
        try {
  //        Class.forName(driver)
  //        connection = DriverManager.getConnection(url, username, password)
  //        connection.setAutoCommit(false)
  //        val statement = connection.createStatement
  //        val query = ("INSERT INTO trace_domain_table VALUES (0,\"%s\",\"%s\")").format(source,end)
  //        statement.addBatch(query)
  //        statement.executeBatch()
  //        connection.commit()
          redis0.lpush(source+"_inbound",end)
          println("end : "+redis0.get(source+"_inbound"))
          redis1.lpush(end+"_outbound",source)
          println("source : "+redis1.get(end+"_outbound"))
        } catch {
          case e: Exception => e.printStackTrace
        }
  //      finally {
  //        try {if(redis0!=null) redis0.wait()} catch{ case e:SQLException => }
  //        try {if(redis1!=null) redis1.wait()} catch{ case e:SQLException => }
  //      }
      }

      case RetrieveEntry(d:String) => {
      try {
        val key = d+"_outbound"
        val backlinks = redis1.lrange(d+"_outbound",0,-1)
        backlinks onComplete {
          case Success(urls) => for (url <- urls) println(url.utf8String)
          case Failure(t) => println("An error has occurred: " + t.getMessage)
        }
        println("backlinks found:" + backlinks)
      } catch {
        case e: Exception => e.printStackTrace
      }
    }
  }
}

/*
ZombieCode
        /*
        pstmt = connection.prepareStatement("INSERT INTO domain_table VALUES (?,?,?,?)")
        pstmt.setInt(1,0)
        pstmt.setString(2,d)
        pstmt.setInt(3,Random.nextInt(100))
        pstmt.setInt(4,Random.nextInt(100))
        pstmt.execute()
        */

 */