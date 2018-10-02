package com.thougthworks

import com.thougthworks.RNG.Simple
import org.scalatest.{FunSuite, Matchers}

class RNGTest extends FunSuite with Matchers {

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
}
