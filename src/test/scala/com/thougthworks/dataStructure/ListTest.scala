package com.thougthworks.dataStructure

import org.scalatest.{FunSuite, Matchers}

import scala.util.Try

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

  test("should give tail of a list") {
    val list = List(1, 2, 3)
    val expectedTail = Cons(2, Cons(3, Nil))
    val actualTail = list.tail

    actualTail shouldBe expectedTail
  }

  test("should throw UnsupportedOperationException for tail of empty list") {
    val triedTail = Try(List().tail)

    triedTail.isFailure shouldBe true

    val exception = triedTail.toEither.left.get

    exception.isInstanceOf[UnsupportedOperationException] shouldBe true
  }

  test("should drop n element of list") {
    val list = List(1, 2, 3)
    val expectedList = Cons(3, Nil)
    val actualList = list.drop(2)

    actualList shouldBe expectedList
  }

  test("should empty list when elements to drop is equal to size of list") {
    val list = List(1, 2, 3)
    val actualList = list.drop(3)

    actualList shouldBe List.empty
  }

  test("should empty list when elements to drop is greater than size of list") {
    val list = List(1, 2, 3)
    val actualList = list.drop(20)

    actualList shouldBe List.empty
  }

  test("should give empty list when calling drop on emptyList") {
    List.empty.drop(21) shouldBe List.empty
  }

  test("should drop elements of list till then continue satisfying given condition") {
    val intergers = List(1, 2, 3, 4, 5, 6, 7)
    val expectedRemainingIntegers = List(5, 6, 7)

    intergers.dropWhile(_ < 5) shouldBe expectedRemainingIntegers
  }

  test("should give empty list when calling dropWhile on empty list") {
    List.empty[Int].dropWhile(_ != 100) shouldBe List.empty
  }
}
