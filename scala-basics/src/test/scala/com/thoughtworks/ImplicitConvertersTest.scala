package com.thoughtworks

import com.thoughtworks.ImplicitConverters._
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

    "return a map with key as value of input map and value as key of input map" in {
      keyVals.swap shouldBe Map('a' -> 1, 'b' -> 2)
    }

    "return a empty map when input map is empty" in {
      val emptyMap = Map[Int, Int]()
      emptyMap.swap shouldBe Map.empty
    }
  }

  "Number.isBetweenExc" should {
    "return true if value is equal to start value" in {
      2 isBetweenExcEnd (2, 3) shouldBe true
    }

    "return true if value is greater than start value but less than stop value" in {
      2 isBetweenExcEnd (0, 3) shouldBe true
    }

    "return false if value is equal to stop value" in {
      2 isBetweenExcEnd (0, 2) shouldBe false
    }

    "return false if value is less than start value" in {
      -3 isBetweenExcEnd (0, 2) shouldBe false
    }

    "return false if value is greater than stop value" in {
      10 isBetweenExcEnd (0, 2) shouldBe false
    }
  }
}
