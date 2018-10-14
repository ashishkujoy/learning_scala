package com.thougthworks

import org.scalatest.{Matchers, WordSpec}
import ImplicitConverters._

class ImplicitConvertersTest extends WordSpec with Matchers {
  "MyMap" should {
    val keyVals = Map(1 -> 'a', 2 -> 'b')
    "return key of given value if it is present" in {
      keyVals.findKeyByValue('b') shouldBe Some(2)
    }

    "return none if given value is not present" in {
      keyVals.findKeyByValue('d') shouldBe None
    }

    "return a map with key as value of input map and value as key of input map" in {
      keyVals.swap shouldBe Map('a' -> 1, 'b' -> 2)
    }

    "return a empty map when input map is empty" in {
      val emptyMap = Map[Int, Int]()
      emptyMap.swap shouldBe Map.empty
    }
  }
}
