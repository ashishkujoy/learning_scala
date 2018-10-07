package com.thoughtworks

object App {
  def main(args: Array[String]): Unit = {
    val rdd = GlobalVariables.sc.parallelize(Seq(1, 2, 3))
    rdd.foreach(println)
  }
}
