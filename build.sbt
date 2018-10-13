inThisBuild(List(
  organization := "com.thoughtworks",
  scalaVersion := "2.11.8",
  version := "0.1.0-SNAPSHOT",
  transitiveClassifiers in Global := Seq(Artifact.SourceClassifier)
))

lazy val `scala-basics` = project
  .settings(
    libraryDependencies ++= Dependencies.basic
  ).enablePlugins(Coverage)

lazy val `spark-basics` = project
  .settings(
    libraryDependencies ++= Dependencies.sparkBasics
  ).enablePlugins(Coverage)


lazy val `streams-basics` = project
  .settings(
    libraryDependencies ++= Dependencies.streamBasics
  ).enablePlugins(Coverage)
