import sbt.librarymanagement.ModuleID

object Dependencies {

  private val spark = Seq(
    Spark.`core`,
    Spark.`sql`,
    Spark.`ml-lib`,
    Spark.`streaming`
  )
  private val streams = Seq(
    Akka.`akka-stream`,
    Akka.`akka-actor-typed`,
    Akka.`play-json`,
    Akka.`akka-http`,
    Akka.`akka-test-kit`
  )

  val basic = Seq(
    Libs.`scala-test`
  )

  val streamBasics: Seq[ModuleID] = basic ++ streams

  val sparkBasics: Seq[ModuleID] = basic ++ spark

  val all: Seq[ModuleID] = basic ++ sparkBasics

}
