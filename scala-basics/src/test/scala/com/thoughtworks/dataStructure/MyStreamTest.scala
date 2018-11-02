package com.thoughtworks.dataStructure
import com.thoughtworks.BaseTest

class MyStreamTest extends BaseTest {

  test("should convert a given empty stream to empty list") {
    MyStream.empty.toList shouldBe List.empty
  }

  test("should convert a given stream to a list") {
    val stream = MyStream(1, 2, 3, 4)
    val list   = List(1, 2, 3, 4)
    stream.toList shouldBe list
  }

  test("should take n elements of a stream") {
    MyStream(1, 2, 3, 4).take(2).toList shouldBe MyStream(1, 2).toList
  }

  test("should take all elements of a stream given when size of stream is less then elements required") {
    MyStream(1, 2, 3, 4).take(20).toList shouldBe MyStream(1, 2, 3, 4).toList
  }

  test("take of empty stream should give empty stream") {
    MyStream.empty.take(2).toList shouldBe MyStream.empty.toList
  }

  test("should give empty stream if element to be taken are negative") {
    MyStream(1, 2, 3, 4).take(-20).toList shouldBe MyStream.empty.toList
  }

  test("should give take elements of stream till they satisfy given predicate") {
    MyStream(1, 2, 3, 4, 5).takeWhile(_ < 4).toList shouldBe MyStream(1, 2, 3).toList
    MyStream(1, 2, 3, 4).takeWhile_1(_ < 3).toList shouldBe MyStream(1, 2).toList
  }

  test("should give empty stream for takeWhile on empty stream") {
    MyStream.empty[Int].takeWhile(_ < 4).toList shouldBe MyStream.empty[Int].toList
  }

  test("should fold stream starting with given default value") {
    MyStream(1, 2, 3).foldRight("")((a, b) => s"$a _ $b") shouldBe "1 _ 2 _ 3 _ "
  }

  test("should give true if a stream contain given element") {
    MyStream(1, 2, 3, 4).exists(_ == 3) shouldBe true
  }

  test("should give false if any element doesn't conform to given predicate") {
    MyStream(1, 2, 3, 4).forAll(_ < 3) shouldBe false
  }

  test("should map all element of stream as per given mapper") {
    MyStream(1, 2, 3, 4).map(_ + 1).toList shouldBe MyStream(2, 3, 4, 5).toList
  }

  test("should filter element of a stream as per given predicate") {
    MyStream(1, 2, 3, 4).filter(_ % 2 == 0).toList shouldBe MyStream(2, 4).toList
  }

}
