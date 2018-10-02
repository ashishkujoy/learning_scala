package com.thougthworks

import com.thougthworks.RNG.{Rand, Simple}
import org.scalatest.{FunSuite, Matchers}

class RNGTest extends FunSuite with Matchers {

  private val stringRand: Rand[String] = RNG.unit("string")


  test("should give a list of positive integers of give size") {
    val rng = Simple(1024)
    val integers = RNG.ints(10)(rng)._1

    integers.size shouldBe 10
    integers.forall(_ >= 0) shouldBe true
  }

  test("should give a list of empty list for size 0") {
    val rng = Simple(1024)
    val integers = RNG.ints(0)(rng)._1

    integers.isEmpty shouldBe true
  }

  test("should give a list of empty list for negative size") {
    val rng = Simple(1024)
    val integers = RNG.ints(-10)(rng)._1

    integers.isEmpty shouldBe true
  }

  test("should give generator which always returns given value") {
    val testValue = 21
    val rand: Rand[Int] = RNG.unit(testValue)
    for (i <- 1 to 100) {
      val rng = Simple(i)
      rand(rng)._1 shouldBe testValue
    }
  }

  test("should Rand[String] to Rand[Int] for a given string to int transformer") {
    val stringToIntTransformer: String => Int = (s: String) => s.length

    val actualRand = RNG.map(stringRand)(stringToIntTransformer)

    actualRand.isInstanceOf[Rand[Int]] shouldBe true
  }

  test("should convert list of Rand to a Rand of list") {
    def stringRand2 = RNG.unit("String2")

    val rands = List(stringRand, stringRand2)

    val randOfList = RNG.sequence(rands)
    randOfList.isInstanceOf[Rand[List[String]]] shouldBe true
  }

  test("should flatten the result of function returning a Rand[B]") {
    def stringToIntRand(s: String): Rand[Int] = {
      val size = s.length
      RNG.unit(size)
    }

    val intRand = State.flatMap(stringRand)(stringToIntRand)

    intRand.run.isInstanceOf[Rand[Int]] shouldBe true
  }
}
