package com.thougthworks
import com.thougthworks.Sorts.quickSort
import org.scalatest.{Matchers, WordSpec}

class SortsTest extends WordSpec with Matchers {

  "Quick Sort" should {
    def isLessThan(a: Int, b: Int): Boolean = a < b
    "sort given integers as per given comparator" in {
      val seq = Seq(1, 20, 3, 14, 16, 2)
      quickSort(seq)(isLessThan) shouldBe Seq(1, 2, 3, 14, 16, 20)
    }

    "sort given strings as per given comparator" in {
      val strings = Seq("a","bc","b","abc","","abcd")
      val expectedSortedStrings = Seq("","b","a","bc","abc","abcd")
      val actualSortedStrings = quickSort(strings)((s1,s2) => s1.length < s2.length)

      actualSortedStrings shouldBe expectedSortedStrings
    }

    "return a empty list if empty list is empty" in {
      quickSort(Seq.empty)(isLessThan) shouldBe Seq.empty
    }
  }
}
