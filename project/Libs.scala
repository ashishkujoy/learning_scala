import sbt._

object Libs {
  val `scala-test`          = "org.scalatest"          %% "scalatest"           % "3.0.5"
  val `akka-http-play-json` = "de.heikoseeberger"      %% "akka-http-play-json" % "1.21.0"
  val `scala-async`         = "org.scala-lang.modules" %% "scala-async"         % "0.9.7"
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

object Akka {
  val Version            = "2.5.16"
  val Org                = "com.typesafe.akka"
  val `akka-stream`      = Org %% "akka-stream" % Version
  val `akka-actor-typed` = Org %% "akka-actor-typed" % Version
  val `akka-http`        = Org %% "akka-http" % "10.1.5"
  val `play-json`        = "com.typesafe.play" %% "play-json" % "2.6.10"

}
