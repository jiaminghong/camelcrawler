

import java.net.URL

import org.scalatest._

import collection.mutable.Stack

class FirstSpec extends FlatSpec {
  val ex = new URL("http://example.com/")

  "A Domain Object" should "shoud have 1:Inbound & 1:Outbound" in {
    val url:URL = new URL("https://en.wikipedia.org/wiki/Enrico_Fermi")
    val d = Domain(url)

    assert(d.domain === url)
    assert(d.outBoundDomain.get(ex) === None)
    d.addInBound(ex)

    assert(d.inBoundDomain.get(ex) == Some(1))
    println(d.outBoundDomain)

  }

  it should "throw DomainException if test fails" in {
    val emptyStack = new Stack[String]
    assertThrows[NoSuchElementException] {
      emptyStack.pop()
    }
  }
}
