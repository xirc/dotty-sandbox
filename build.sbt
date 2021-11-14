name := "scala3-sandbox"
ThisBuild / scalaVersion := "3.1.0"
ThisBuild / scalacOptions ++= Seq(
  "-source",
  "future",
  "-Ysafe-init",
)
ThisBuild / libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.2.10",
  "org.scalatest" %% "scalatest" % "3.2.10" % Test,
)

lazy val ciFormat = taskKey[Unit]("CI format")
ciFormat :=
  Def
    .sequential(
      Compile / scalafmtSbt,
      scalafmtAll,
    )
    .value

lazy val ciCheck = taskKey[Unit]("CI check")
ciCheck :=
  Def
    .sequential(
      clean,
      Compile / scalafmtSbtCheck,
      scalafmtCheckAll,
      Test / compile,
      Test / test,
    )
    .value
