package com.thoughtworks

import com.thoughtworks.RNG.{double, Rand, Simple}

class RNGTest extends BaseTest {

  private val stringRand: Rand[String] = RNG.unit("string")
  private val rng: RNG                 = Simple(12)

  test("should give a positive integer") {
    var randomInts = Set.empty[Int]
    for (seed <- 1 to 1000 * 1000) {
      val randomInt = RNG.nonNegativeInt(Simple(seed))._1
      randomInt > 0 shouldBe true
      randomInts = randomInts + randomInt
    }
    randomInts.size > 1 shouldBe true
  }

  test("should give a double value between 0 and 1, excluding 1") {
    var randomDoubles = Set.empty[Double]
    for (seed <- 1 to 1000 * 1000) {
      val randomDouble = double(Simple(seed))._1
      randomDouble >= 0 && randomDouble < 1 shouldBe true
      randomDoubles = randomDoubles + randomDouble
    }
    randomDoubles.size > 1 shouldBe true
  }

  test("should give a list of positive integers of give size") {
    val rng      = Simple(1024)
    val integers = RNG.ints(10)(rng)._1

    integers.size shouldBe 10
    integers.forall(_ >= 0) shouldBe true
  }

  test("should give a list of empty list for size 0") {
    val rng      = Simple(1024)
    val integers = RNG.ints(0)(rng)._1

    integers.isEmpty shouldBe true
  }

  test("should give a list of empty list for negative size") {
    val rng      = Simple(1024)
    val integers = RNG.ints(-10)(rng)._1

    integers.isEmpty shouldBe true
  }

  test("should give generator which always returns given value") {
    val testValue       = 21
    val rand: Rand[Int] = RNG.unit(testValue)
    for (i <- 1 to 100) {
      val rng = Simple(i)
      rand(rng)._1 shouldBe testValue
    }
  }

  test("should convert Rand[String] to Rand[Int] for a given string to int transformer") {
    val stringToIntTransformer: String => Int = (s: String) => s.length

    val actualRand = RNG.map(stringRand)(stringToIntTransformer)

    actualRand.isInstanceOf[Rand[Int]] shouldBe true
  }

  test("should combine two Rand as per given function") {
    val intRand = RNG.unit(1)
    val rand    = RNG.map2(stringRand, intRand)((s, i) => s"$s $i")

    rand.isInstanceOf[Rand[String]] shouldBe true
    rand(rng)._1 shouldBe "string 1"
  }

  test("should convert list of Rand to a Rand of list") {
    def stringRand2 = RNG.unit("String2")

    val rands = List(stringRand, stringRand2)

    val randOfList = RNG.sequence(rands)
    randOfList.isInstanceOf[Rand[List[String]]] shouldBe true
  }

}
