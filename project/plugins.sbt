import sbt._

addSbtPlugin("org.scoverage"                     % "sbt-scoverage"              % "1.5.1")
addSbtPlugin("org.scoverage"                     % "sbt-coveralls"              % "1.2.5")
addSbtPlugin("com.geirsson"                      % "sbt-scalafmt"               % "1.5.1")
addSbtPlugin("com.thesamet"                      % "sbt-protoc"                 % "0.99.19")
addSbtPlugin("org.scalastyle"                    %% "scalastyle-sbt-plugin"     % "1.0.0")
addSbtPlugin("com.geirsson"                      % "sbt-scalafmt"               % "1.5.1")
addSbtPlugin("com.dwijnand"                      % "sbt-dynver"                 % "3.3.0")
addSbtPlugin("com.eed3si9n"                      % "sbt-unidoc"                 % "0.4.2")
addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings"           % "2.0.1")
addSbtPlugin("org.foundweekends"                 % "sbt-bintray"                % "0.5.4")
addSbtPlugin("com.timushev.sbt"                  % "sbt-updates"                % "0.4.0")
addSbtPlugin("com.typesafe.sbt"                  % "sbt-ghpages"                % "0.6.3")
addSbtPlugin("com.typesafe.sbt"                  % "sbt-site"                   % "1.3.2")
addSbtPlugin("com.typesafe.sbt"                  % "sbt-native-packager"        % "1.3.15")
addSbtPlugin("com.typesafe.sbt"                  % "sbt-multi-jvm"              % "0.4.0")
addSbtPlugin("com.eed3si9n"                      % "sbt-buildinfo"              % "0.9.0")
addSbtPlugin("pl.project13.scala"                % "sbt-jmh"                    % "0.3.5")
addSbtPlugin("com.orrsella"                      % "sbt-stats"                  % "1.0.7")
addSbtPlugin("io.github.jonas"                   % "sbt-paradox-material-theme" % "0.6.0")
addSbtPlugin("de.johoop"                         % "sbt-testng-plugin"          % "3.1.1")
addSbtPlugin("io.spray"                          % "sbt-revolver"               % "0.9.1")
addSbtPlugin("com.typesafe.sbt"                  % "sbt-git"                    % "1.0.0")
addSbtPlugin("org.portable-scala"                % "sbt-scalajs-crossproject"   % "0.6.0")
addSbtPlugin("org.scala-js"                      % "sbt-scalajs"                % "0.6.26")
addSbtPlugin("com.github.cb372"                  % "sbt-explicit-dependencies"  % "0.2.9")

scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-feature",
  "-unchecked",
  "-deprecation",
  //"-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Xfuture"
)
