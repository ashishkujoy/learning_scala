package com.thougthworks
import com.thougthworks.Sorts.quickSort
import org.scalatest.{Matchers, WordSpec}

class SortsTest extends WordSpec with Matchers {

  "Quick Sort" should {
    def isLessThan(a: Int, b: Int): Boolean = a < b
    "sort a sorted sequence as per given comparator" in {
      val seq = Seq(1, 20, 3, 14, 16, 2)
      quickSort(seq)(isLessThan) shouldBe Seq(1, 2, 3, 14, 16, 20)
    }

    "return a empty list if empty list is empty" in {
      quickSort(Seq.empty)(isLessThan) shouldBe Seq.empty
    }
  }
}
