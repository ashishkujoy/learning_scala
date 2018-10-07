package com.thoughtworks.rdd

import com.thoughtworks.GlobalVariables
import org.apache.spark.rdd.RDD

class FileRdd(rdd: RDD[String]) {
  def lineCounts: Long = rdd.count()

  def wordCounts: Long =
    rdd
      .map(l => l.split(" ").filter(w => w.trim.nonEmpty))
      .count()
}

object FileRdd {
  def apply(filePath: String): FileRdd = {
    val rdd: RDD[String] = GlobalVariables.sc.textFile(filePath)
    new FileRdd(rdd)
  }
}
