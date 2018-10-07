package com.thoughtworks.rdd

import org.scalatest.{FunSuite, Matchers}

class FileRddTest extends FunSuite with Matchers {

  test("should give number of line in a given file") {
    val filePath = "spark-basics/src/test/resources/rdd/fileWithFourLines.txt"
    val fileRdd  = FileRdd(filePath)

    fileRdd.lineCounts shouldBe 4
  }
}
