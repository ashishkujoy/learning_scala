import sbt.librarymanagement.ModuleID

object Dependencies {

  private val spark = Seq(
    Spark.`core`,
    Spark.`sql`,
    Spark.`ml-lib`,
    Spark.`streaming`
  )

  val basic = Seq(
    Libs.`scala-test`
  )

  val sparkBasics: Seq[ModuleID] = basic ++ spark

  val all: Seq[ModuleID] = basic ++ sparkBasics

}
