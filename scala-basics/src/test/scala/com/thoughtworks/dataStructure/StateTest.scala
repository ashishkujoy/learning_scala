package com.thoughtworks.dataStructure
import com.thoughtworks.RNG.{Rand, Simple}
import com.thoughtworks.{BaseTest, RNG, State}

class StateTest extends BaseTest {

  private val rng = Simple(12)

  test("should flatten the result of function returning a Rand[B]") {
    def stringToIntRand(s: String): Rand[Int] = RNG.unit(s.length)
    val stringRand                            = RNG.unit("seed")
    val intRand                               = State.flatMap(stringRand)(stringToIntRand).run

    intRand.isInstanceOf[Rand[Int]] shouldBe true
    intRand(rng)._1.isInstanceOf[Int] shouldBe true
  }

  test("should convert one type of rand to another as per given mapper") {
    val stringRand = State._map(RNG.int)(_.toString).run
    stringRand.isInstanceOf[Rand[String]] shouldBe true
    stringRand(rng)._1.isInstanceOf[String] shouldBe true
  }

  test("should combine two rands as per given combinator") {
    val stringRand   = RNG.unit("seed")
    val intRand      = RNG.unit(1)
    val combinedRand = State._map2(stringRand, intRand)((s, i) => s"$s $i").run

    combinedRand.isInstanceOf[Rand[String]] shouldBe true
    combinedRand(rng)._1 shouldBe "seed 1"
  }

}
