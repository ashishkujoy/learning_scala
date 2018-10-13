package com.thoughtworks.rdd

import org.scalatest.{FunSuite, Matchers}

class FileRddTest extends FunSuite with Matchers {

  test("should give number of line in a given file") {
    val filePath = "spark-basics/src/test/resources/rdd/fileWithFourLines.txt"
    val fileRdd  = FileRdd(filePath)

    fileRdd.lineCounts shouldBe 4
  }

  test("should give number of words in a given file") {
    val filePath = "spark-basics/src/test/resources/rdd/fileWithFourLines.txt"
    val fileRdd  = FileRdd(filePath)

    fileRdd.wordCounts shouldBe 12
  }

  test("should give count of a word in a given file") {
    val filePath = "spark-basics/src/test/resources/rdd/fileWithRepeatedWords.txt"
    val fileRdd  = FileRdd(filePath)

    fileRdd.count("yes") shouldBe 10
  }

  test("should give count of a unknown as zero") {
    val filePath = "spark-basics/src/test/resources/rdd/fileWithRepeatedWords.txt"
    val fileRdd  = FileRdd(filePath)

    fileRdd.count("unknownWord") shouldBe 0
  }
}
