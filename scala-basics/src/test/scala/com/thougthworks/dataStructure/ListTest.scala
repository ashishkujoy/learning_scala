package com.thougthworks.dataStructure

import org.scalatest.{FunSuite, Matchers}

class ListTest extends FunSuite with Matchers {

  test("should give nil when for no arguments to apply") {
    val actualList = List.apply()

    actualList shouldBe Nil
  }

  test("should give list of one element") {
    val actualList   = List.apply(1)
    val expectedList = Cons(1, Nil)

    actualList shouldBe expectedList
  }

  test("should give list of multiple elements") {
    val actualList   = List.apply("ashish", "kumar", "joy")
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
    val list         = List(1, 2, 3)
    val expectedTail = Cons(2, Cons(3, Nil))
    val actualTail   = list.tail

    actualTail shouldBe expectedTail
  }

  test("should throw UnsupportedOperationException for tail of empty list") {
    intercept[UnsupportedOperationException] {
      List.empty.tail
    }
  }

  test("should give head of a list") {
    val list = List(1, 2, 3)
    list.head shouldBe 1
  }

  test("should throw UnsupportedOperationException for head of empty list") {
    intercept[UnsupportedOperationException] {
      List.empty.head
    }
  }

  test("should drop n element of list") {
    val list         = List(1, 2, 3)
    val expectedList = Cons(3, Nil)
    val actualList   = list.drop(2)

    actualList shouldBe expectedList
  }

  test("should empty list when elements to drop is equal to size of list") {
    val list       = List(1, 2, 3)
    val actualList = list.drop(3)

    actualList shouldBe List.empty
  }

  test("should empty list when elements to drop is greater than size of list") {
    val list       = List(1, 2, 3)
    val actualList = list.drop(20)

    actualList shouldBe List.empty
  }

  test("should give empty list when calling drop on emptyList") {
    List.empty.drop(21) shouldBe List.empty
  }

  test("should drop elements of list till then continue satisfying given condition") {
    val integers                  = List(1, 2, 3, 4, 5, 6, 7)
    val expectedRemainingIntegers = List(5, 6, 7)

    integers.dropWhile(_ < 5) shouldBe expectedRemainingIntegers
  }

  test("should give empty list when calling dropWhile on empty list") {
    List.empty[Int].dropWhile(_ != 100) shouldBe List.empty
  }

  test("should append first list to second list") {
    val list1 = List(1, 2, 3)
    val list2 = List(4, 5, 6)

    List.append(list1, list2) shouldBe List(1, 2, 3, 4, 5, 6)
    List.append(list2, list1) shouldBe List(4, 5, 6, 1, 2, 3)
  }

  test("should give list back on appending empty list") {
    val list = List(1, 2, 3)
    List.append(list, List.empty) shouldBe list
  }

  test("should give list back on appending list on empty list") {
    val list = List(1, 2, 3)
    List.append(List.empty, list) shouldBe list
  }

  test("should appending empty list on another empty list should give empty list") {
    List.append(List.empty, List.empty) shouldBe List.empty
  }

  test("should give sum of length of all words in given list") {
    val words = List("hello", "scala", "is", "fun")
    words.foldLeft(0)((a, b) => a + b.length) shouldBe 15
  }

  test("should give initial value for foldLeft on empty list") {
    List.empty[String].foldLeft(0)((a, b) => a + b.length) shouldBe 0
  }

  test("should give reverse of a given list") {
    List(1, 2, 3, 4).reverse shouldBe List(4, 3, 2, 1)
  }

  test("reverse of empty list should be empty list") {
    List.empty[String] shouldBe List.empty
  }

  test("should concat multiple list to a single list") {
    val list1 = List(1, 2, 3)
    val list2 = List(4, 5, 6)
    val list3 = List(7, 8, 9)

    List.concat(list1, list2, list3) shouldBe List(1, 2, 3, 4, 5, 6, 7, 8, 9)
  }

  test("should return empty list on concatenating zero lists") {
    List.concat() shouldBe List.empty
  }
}
