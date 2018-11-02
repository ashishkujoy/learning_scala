package com.thoughtworks.dataStructure

import com.thoughtworks.BaseTest

class ListTest extends BaseTest {

  test("should give nil when for no arguments to apply") {
    val actualList = MyList.apply()

    actualList shouldBe Nil
  }

  test("should give list of one element") {
    val actualList   = MyList.apply(1)
    val expectedList = Cons(1, Nil)

    actualList shouldBe expectedList
  }

  test("should give list of multiple elements") {
    val actualList   = MyList.apply("ashish", "kumar", "joy")
    val expectedList = Cons("ashish", Cons("kumar", Cons("joy", Nil)))

    actualList shouldBe expectedList
  }

  test("should give zero as sum of empty list") {
    val list: MyList[Int] = MyList.apply()

    MyList.sum(list) shouldBe 0
  }

  test("should give sum of list of multiple integers") {
    val integers = MyList(1, 2, 3, 4)

    MyList.sum(integers) shouldBe 10
  }

  test("should give tail of a list") {
    val list         = MyList(1, 2, 3)
    val expectedTail = Cons(2, Cons(3, Nil))
    val actualTail   = list.tail

    actualTail shouldBe expectedTail
  }

  test("should throw UnsupportedOperationException for tail of empty list") {
    intercept[UnsupportedOperationException] {
      MyList.empty.tail
    }
  }

  test("should give head of a list") {
    val list = MyList(1, 2, 3)
    list.head shouldBe 1
  }

  test("should throw UnsupportedOperationException for head of empty list") {
    intercept[UnsupportedOperationException] {
      MyList.empty.head
    }
  }

  test("should drop n element of list") {
    val list         = MyList(1, 2, 3)
    val expectedList = Cons(3, Nil)
    val actualList   = list.drop(2)

    actualList shouldBe expectedList
  }

  test("should empty list when elements to drop is equal to size of list") {
    val list       = MyList(1, 2, 3)
    val actualList = list.drop(3)

    actualList shouldBe MyList.empty
  }

  test("should empty list when elements to drop is greater than size of list") {
    val list       = MyList(1, 2, 3)
    val actualList = list.drop(20)

    actualList shouldBe MyList.empty
  }

  test("should give empty list when calling drop on emptyList") {
    MyList.empty.drop(21) shouldBe MyList.empty
  }

  test("should drop elements of list till then continue satisfying given condition") {
    val integers                  = MyList(1, 2, 3, 4, 5, 6, 7)
    val expectedRemainingIntegers = MyList(5, 6, 7)

    integers.dropWhile(_ < 5) shouldBe expectedRemainingIntegers
  }

  test("should give empty list when calling dropWhile on empty list") {
    MyList.empty[Int].dropWhile(_ != 100) shouldBe MyList.empty
  }

  test("should append first list to second list") {
    val list1 = MyList(1, 2, 3)
    val list2 = MyList(4, 5, 6)

    MyList.append(list1, list2) shouldBe MyList(1, 2, 3, 4, 5, 6)
    MyList.append(list2, list1) shouldBe MyList(4, 5, 6, 1, 2, 3)
  }

  test("should give list back on appending empty list") {
    val list = MyList(1, 2, 3)
    MyList.append(list, MyList.empty) shouldBe list
  }

  test("should give list back on appending list on empty list") {
    val list = MyList(1, 2, 3)
    MyList.append(MyList.empty, list) shouldBe list
  }

  test("should appending empty list on another empty list should give empty list") {
    MyList.append(MyList.empty, MyList.empty) shouldBe MyList.empty
  }

  test("should give sum of length of all words in given list") {
    val words = MyList("hello", "scala", "is", "fun")
    words.foldLeft(0)((a, b) => a + b.length) shouldBe 15
  }

  test("should give initial value for foldLeft on empty list") {
    MyList.empty[String].foldLeft(0)((a, b) => a + b.length) shouldBe 0
  }

  test("should give reverse of a given list") {
    MyList(1, 2, 3, 4).reverse shouldBe MyList(4, 3, 2, 1)
  }

  test("reverse of empty list should be empty list") {
    MyList.empty[String] shouldBe MyList.empty
  }

  test("should concat multiple list to a single list") {
    val list1 = MyList(1, 2, 3)
    val list2 = MyList(4, 5, 6)
    val list3 = MyList(7, 8, 9)

    MyList.concat(list1, list2, list3) shouldBe MyList(1, 2, 3, 4, 5, 6, 7, 8, 9)
  }

  test("should return empty list on concatenating zero lists") {
    MyList.concat() shouldBe MyList.empty
  }

  test("should prepend a given element at the end of list") {
    val numbers  = MyList(1, 2, 3, 4)
    val expected = MyList(1, 2, 3, 4, 5)

    MyList.+:(numbers, 5) shouldBe expected
  }

  test("should prepend a given element in empty list") {
    MyList.+:(MyList.empty, 5) shouldBe MyList(5)
    Seq(1) :+ 1
  }
}
