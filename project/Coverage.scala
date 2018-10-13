import sbt.{Def, _}
object Coverage extends AutoPlugin {
//  import scoverage.ScoverageSbtPlugin.autoImport._
  override def projectSettings: Seq[Def.Setting[_]] = Seq(
//    coverageMinimum := 50,
//    coverageFailOnMinimum := false
  )
}
