import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar

import scala.io.Source

object Utility {

  def readFile(): Unit ={
    val relativePath = "root_domain.txt"
    val f = new File(getClass.getResource(relativePath).getPath)
    val bufferedSource = Source.fromFile(f)
    for (line <- bufferedSource.getLines) {
      println(line.toUpperCase)
    }
    bufferedSource.close()
  }

  // as "Wednesday, October 20"
  def getCurrentDate:String = getCurrentDateTime("EEEE, MMMM d")

  // as "6:20 p.m."
  def getCurrentTime: String = getCurrentDateTime("K:m aa")

  // a common function used by other date/time functions
  private def getCurrentDateTime(dateTimeFormat: String): String = {
    val dateFormat = new SimpleDateFormat(dateTimeFormat)
    val cal = Calendar.getInstance()
    dateFormat.format(cal.getTime())
  }

}
