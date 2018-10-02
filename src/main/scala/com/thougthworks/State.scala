package com.thougthworks

trait RNG {
  def nextInt: (Int, RNG)
}

object RNG {

  case class Simple(seed: Long) extends RNG {
    def nextInt: (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
      val nextRNG = Simple(newSeed)
      val n = (newSeed >>> 16).toInt
      (n, nextRNG)
    }
  }

  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (int, rng2) = rng.nextInt
    val positiveInt = if (int < 0) Math.abs(int + 1) else int
    (positiveInt, rng2)
  }

  def double(rng: RNG): (Double, RNG) = {
    val (int, rng2) = nonNegativeInt(rng)
    val double = (int / (int + 1)).toDouble
    (double, rng2)
  }

  def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
    def go(count: Int, ints: List[Int], rng: RNG): (List[Int], RNG) = {
      if (count <= 0) {
        (ints, rng)
      } else {
        val (int, rng2) = nonNegativeInt(rng)
        go(count - 1, int :: ints, rng2)
      }
    }
    go(count, List.empty, rng)
  }

  type Rand[A] = RNG => (A,RNG)

  val int:Rand[Int] = _.nextInt
}
