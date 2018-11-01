package com.thoughtworks

trait RNG {
  def nextInt: (Int, RNG)
}

object RNG {

  case class Simple(seed: Long) extends RNG {
    def nextInt: (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
      val nextRNG = Simple(newSeed)
      val n       = (newSeed >>> 16).toInt
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
    val double      = (int / (int + 1)).toDouble
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

  type Rand[A] = RNG => (A, RNG)

  val int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] = rng => (a, rng)

  def map[A, B](stringRand: Rand[A])(f: A => B): Rand[B] = { rng =>
    {
      val (a, rng1) = stringRand(rng)
      (f(a), rng1)
    }
  }

  def _double(rng: RNG): (Double, RNG) = {
    map(nonNegativeInt) { a =>
      a.toDouble / (a.toDouble + 1)
    }(rng)
  }

  def map2[A, B, C](ar: Rand[A], br: Rand[B])(f: (A, B) => C): Rand[C] = { rng =>
    {
      val (a, rng1) = ar(rng)
      val (b, rng2) = br(rng1)
      (f(a, b), rng2)
    }
  }

  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] = {
    fs.foldRight[Rand[List[A]]](unit(List.empty)) { (rand, rands) =>
      map2(rand, rands)(_ :: _)
    }
  }

}

case class State[S, +A](run: S => (A, S))

object State {

  import RNG._

  def flatMap[A, B](rand: Rand[A])(f: A => Rand[B]): State[RNG, B] = {
    val rngToTuple: Rand[B] = rng => {
      val (a, rng1) = rand(rng)
      val (b, rng2) = f(a)(rng1)
      (b, rng2)
    }
    State(rngToTuple)
  }

  def _map[S, A, B](rand: Rand[A])(f: A => B): State[RNG, B] = {
    flatMap(rand)(a => unit(f(a)))
  }

  def _map2[A, B, C](randA: Rand[A], randB: Rand[B])(f: (A, B) => C): State[RNG, C] = {
    flatMap(randA)(a => map(randB)(b => f(a, b)))
  }
}

sealed trait Input

case object Coin extends Input

case object Turn extends Input

case class Machine(locked: Boolean, candies: Int, coins: Int) {
  def update(input: Input): Machine =
    if (candies > 0) {
      input match {
        case Coin => if (locked) Machine(locked = false, candies, coins + 1) else this
        case Turn => if (!locked) Machine(locked = true, candies - 1, coins) else this
      }
    } else this
}

object Machine {
  def simulate(inputs: List[Input]): State[Machine, Int] = {
    val machineToTuple: Machine => (Int, Machine) = (machine: Machine) => {
      val newMachineState = inputs.foldLeft(machine)((machine, input) => machine.update(input))
      (newMachineState.coins, newMachineState)
    }
    State(machineToTuple)
  }
}
