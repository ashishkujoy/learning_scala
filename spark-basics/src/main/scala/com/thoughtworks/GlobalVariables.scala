package com.thoughtworks

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object GlobalVariables {
  private val sparkConf = new SparkConf()
    .setMaster("local")
    .setAppName("spark-basics")

  private val sparkSession = SparkSession
    .builder()
    .config(sparkConf)
    .getOrCreate()

  val sc: SparkContext = sparkSession.sparkContext

  sc.setLogLevel("ERROR")
}
