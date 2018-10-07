inThisBuild(List(
  organization := "com.thoughtworks",
  scalaVersion := "2.12.7",
  version := "0.1.0-SNAPSHOT",
  transitiveClassifiers in Global := Seq(Artifact.SourceClassifier)
))

lazy val `scala-basics` = project
  .settings(
    libraryDependencies ++= Dependencies.basic
  )

lazy val `spark-basics` = project
  .settings(
    libraryDependencies ++= Dependencies.sparkBasics
  )
