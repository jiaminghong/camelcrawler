
import java.sql.{Connection, DriverManager, PreparedStatement, SQLException}

import Database._
import akka.actor.Actor
import com.mysql.jdbc.Statement

import scala.util.Random

object Database {

  case class Entry(domain:String) {}
  case class Shutdown() {}
  case class RetrieveEntry() {}
  case class RootEntry(d:String){}
  case class TraceEntry(source:String,end:String){}
  case class Test(msg:String){}
}

class Database extends Actor {

  val url = "jdbc:mysql://localhost:3306/web-crawler-db"
  val driver = "com.mysql.jdbc.Driver"
  val username = "root"
  val password = "admin"
  var connection:Connection = _

  val statement:Statement = null


  override def receive: Receive = {
    case Test(msg:String) => {
      println("Message => " + msg)
    }

    case Entry(d: String) => {

      try {
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)
        connection.setAutoCommit(false)
        val statement = connection.createStatement()
        val query = ("INSERT INTO domain_table VALUES (0,\"%s\",10,%d)").format(d, Random.nextInt(100))
        statement.addBatch(query)
        statement.executeBatch()
        connection.commit()

      } catch {
        case e: Exception => e.printStackTrace
      } finally {
        try {if(statement!=null) statement.close()} catch{ case e:SQLException => }
        try {if(connection!=null) connection.close()} catch{ case e:SQLException => }
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

          println("Domain = %s, Inbound = %s".format(domainName, inboundName))
        }
      } catch {
        case e: Exception => e.printStackTrace
      }

    case Shutdown() => {
      connection.close
    }

    case RootEntry(d:String) => {

        try {
          Class.forName(driver)
          connection = DriverManager.getConnection(url, username, password)
          connection.setAutoCommit(false)
          val statement = connection.createStatement
          val query = ("INSERT INTO root_domain_table VALUES (0,\"%s\")").format(d)

          statement.addBatch(query)
          statement.executeBatch()
          connection.commit()

        } catch {
          case e: SQLException  => e.printStackTrace
        } finally {
        try {if(statement!=null) statement.close()} catch{ case e:SQLException => }
        try {if(connection!=null) connection.close()} catch{ case e:SQLException => }

      }
      }

    case TraceEntry(source:String,end:String) => {
      try {
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)
        connection.setAutoCommit(false)
        val statement = connection.createStatement
        val query = ("INSERT INTO trace_domain_table VALUES (0,\"%s\",\"%s\")").format(source,end)
        statement.addBatch(query)
        statement.executeBatch()
        connection.commit()
      } catch {
        case e: Exception => e.printStackTrace
      } finally {
        try {if(statement!=null) statement.close()} catch{ case e:SQLException => }
        try {if(connection!=null) connection.close()} catch{ case e:SQLException => }
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