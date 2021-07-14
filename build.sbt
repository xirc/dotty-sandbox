lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-sandbox",
    scalaVersion := "3.0.1",
    scalacOptions ++= Seq(
      "-source",
      "future",
      "-Yexplicit-nulls",
      "-Ysafe-init",
    ),
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.2.9",
      "org.scalatest" %% "scalatest" % "3.2.9" % Test,
    ),
  )

addCommandAlias(
  "ciFormat",
  Seq(
    "scalafmtSbt",
    "scalafmtAll",
  ).mkString(";"),
)

addCommandAlias(
  "ciCheck",
  Seq(
    "clean",
    "scalafmtSbtCheck",
    "scalafmtCheckAll",
    "Test/compile",
    "test",
  ).mkString(";"),
)
