
import java.sql.{Connection, DriverManager}

import Database.{Entry, RetrieveEntry, Shutdown}
import akka.actor.Actor

import scala.util.Random

object Database {

  case class Entry(domain:String) {}
  case class Shutdown() {}
  case class RetrieveEntry() {}
}

class Database extends Actor {

  val url = "jdbc:mysql://localhost:3306/web-crawler-db"
  val driver = "com.mysql.jdbc.Driver"
  val username = "root"
  val password = "admin"
  var connection:Connection = _



  override def receive: Receive = {
    case Entry(d:String) => {
      try {
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)
        connection.setAutoCommit(false)
        val statement = connection.createStatement
        val query = ("INSERT INTO domain_table VALUES (0,\"%s\",10,%d)").format(d,Random.nextInt(100))
        println(query)
        val rs = statement.addBatch(query)
        val count = statement.executeBatch()
        connection.commit()
        println(count)

      } catch {
        case e: Exception => e.printStackTrace
      }

  }
    case RetrieveEntry() =>
      try {
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)
        val statement = connection.createStatement
        val rs = statement.executeQuery("SELECT * FROM domain_table")
        while (rs.next) {
          val domainName = rs.getString("domain_url")
          val inboundName = rs.getString("inbound_link")

          println("Domain = %s, Inbound = %s".format(domainName,inboundName))
        }
      } catch {
        case e: Exception => e.printStackTrace
      }

    case Shutdown() => {
    connection.close
  }
  }
}