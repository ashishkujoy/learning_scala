package com.thougthworks

import com.thougthworks.Machine.simulate
import org.scalatest.{FunSuite, Matchers}

class MachineTest extends FunSuite with Matchers {

  private val locked = true
  private val unLocked = false
  private val machine = Machine(locked, 10, 4)

  test("should not give any candy or increase coin's counts for empty input") {
    val actualState: State[Machine, Int] = simulate(List.empty)

    val (value, machineNewState) = actualState.run(machine)
    value shouldBe 4
    machineNewState shouldBe machine
  }

  test("should increase coin's count by one when coin is entered") {
    val actualState: State[Machine, Int] = simulate(List(Coin))

    val (value, machineNewState) = actualState.run(machine)
    value shouldBe 5
    machineNewState shouldBe Machine(unLocked, 10, 5)
  }

  test("should decrease candies count by number of times handle is turned after putting equal number of coin's") {
    val actualState: State[Machine, Int] = simulate(List(Coin, Turn, Coin, Turn))

    val (value, machineNewState) = actualState.run(machine)
    value shouldBe 6
    machineNewState shouldBe Machine(locked, 8, 6)
  }

  test("should not decrease candies count when coin is not entered and handle is turned") {
    val actualState: State[Machine, Int] = simulate(List(Turn))

    val (value, machineNewState) = actualState.run(machine)
    value shouldBe 4
    machineNewState shouldBe machine
  }

  test("should not give more than one candy on one coin followed by multiple turns") {
    val actualState: State[Machine, Int] = simulate(List(Coin, Turn, Turn))

    val (totalCoins, machineNewState) = actualState.run(machine)
    totalCoins shouldBe 5
    machineNewState shouldBe Machine(locked, 9, 5)
  }

  test("should not increase coin's count when machine is unlocked and a coin is entered") {
    val actualState: State[Machine, Int] = simulate(List(Coin, Coin, Coin, Turn))

    val (totalCoins, machineNewState) = actualState.run(machine)
    totalCoins shouldBe 5
    machineNewState shouldBe Machine(locked, 9, 5)
  }

  test("should not increase coin's count when their is no candy in machine") {
    val machine = Machine(locked,0,4)
    val actualState: State[Machine, Int] = simulate(List(Coin, Turn, Coin, Turn))

    val (totalCoins, machineNewState) = actualState.run(machine)
    totalCoins shouldBe 4
    machineNewState shouldBe machine
  }
}
