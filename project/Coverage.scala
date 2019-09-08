import org.scoverage.coveralls.Imports.CoverallsKeys._
import sbt.{Def, _}

import scala.util.Properties

object Coverage extends AutoPlugin {
  import scoverage.ScoverageSbtPlugin.autoImport._
  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    coverallsToken := Some(Properties.envOrElse("COVERALLS_REPO_TOKEN", "")),
    coverageMinimum := 20,
    coverageFailOnMinimum := false,
    coverageExcludedPackages := "com.thoughtworks.streams.*"
  )
}
