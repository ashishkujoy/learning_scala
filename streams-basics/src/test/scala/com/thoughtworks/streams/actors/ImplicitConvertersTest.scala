package com.thoughtworks.streams.actors
import com.thoughtworks.streams.actors.ImplicitConverters._
import org.scalatest.{Matchers, WordSpec}

class ImplicitConvertersTest extends WordSpec with Matchers {
  "MyMap" should {
    val keyVals = Map(1 -> 'a', 2 -> 'b')
    "return key of given value if it is present" in {
      keyVals.findKeyByValue('b') shouldBe Some(2)
    }

    "return none if given value is not present" in {
      keyVals.findKeyByValue('d') shouldBe None
    }
  }
}
