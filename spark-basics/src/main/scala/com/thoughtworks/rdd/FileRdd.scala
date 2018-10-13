package com.thoughtworks.rdd

import com.thoughtworks.GlobalVariables
import org.apache.spark.rdd.RDD

class FileRdd(rdd: RDD[String]) {

  def lineCounts: Long = rdd.count()

  def wordCounts: Long = {
    words.map(_.length).reduce(_ + _)
  }

  private def words: RDD[Array[String]] = {
    rdd.map(l => l.split(" ").filter(w => w.trim.nonEmpty)).cache()
  }

  private def wordFrequency: RDD[(String, Int)] = {
    val wordsTuple: RDD[(String, Int)] = words.flatMap(_.map(w => (w, 1)))
    wordsTuple.reduceByKey(_ + _).cache()
  }

  def count(word: String): Int = {
    val headOption = wordFrequency.collect().find(_._1 == word)
    headOption.getOrElse(("", 0))._2
  }

}

object FileRdd {
  def apply(filePath: String): FileRdd = {
    val rdd: RDD[String] = GlobalVariables.sc.textFile(filePath)
    new FileRdd(rdd)
  }
}
