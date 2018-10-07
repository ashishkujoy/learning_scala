package com.thougthworks.dataStructure

import org.scalatest.{FunSuite, Matchers}

class ListTest extends FunSuite with Matchers {

  test("should give nil when for no arguments to apply") {
    val actualList = List.apply()

    actualList shouldBe Nil
  }

  test("should give list of one element") {
    val actualList = List.apply(1)
    val expectedList = Cons(1, Nil)

    actualList shouldBe expectedList
  }

  test("should give list of multiple elements") {
    val actualList = List.apply("ashish", "kumar", "joy")
    val expectedList = Cons("ashish", Cons("kumar", Cons("joy", Nil)))

    actualList shouldBe expectedList
  }

  test("should give zero as sum of empty list") {
    val list: List[Int] = List.apply()

    List.sum(list) shouldBe 0
  }

  test("should give sum of list of multiple integers") {
    val integers = List(1, 2, 3, 4)

    List.sum(integers) shouldBe 10
  }
}
