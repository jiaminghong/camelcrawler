import java.net.URL

import scala.collection.mutable


@deprecated
class Domain {
  var domain:URL = _
  var inBoundDomain = mutable.HashMap[URL,Integer]()
  var outBoundDomain = mutable.HashMap[URL,Integer]()

  def addInBound(url:URL): Unit = {
    //inBoundDomain.put(url, if(inBoundDomain.get(url) == None) 1 else inBoundDomain.get(url) + 1) //todo
    inBoundDomain.put(url, 1)
  }

  def addOutBound(url:URL)={
    outBoundDomain.put(url,1)
  }

}

object Domain {
  def apply(url: URL): Domain = {
    var d = new Domain
    d.domain = url
    d
  }
}
