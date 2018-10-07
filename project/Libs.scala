import sbt._

object Libs {
  val `scala-test` = "org.scalatest" %% "scalatest" % "3.0.5"
}

object Spark {
  private val Org          = "org.apache.spark"
  private val Version      = "2.3.2"
  private val scalaVersion = "2.11"

  val `core`      = Org % s"spark-core_$scalaVersion"      % Version
  val `sql`       = Org % s"spark-sql_$scalaVersion"       % Version
  val `streaming` = Org % s"spark-streaming_$scalaVersion" % Version
  val `ml-lib`    = Org % s"spark-mllib_$scalaVersion"     % Version
}
