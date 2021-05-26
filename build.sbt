lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-sandbox",
    scalaVersion := "3.0.0",
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.2.9",
      "org.scalatest" %% "scalatest" % "3.2.9" % Test,
    ),
  )

addCommandAlias(
  "ciCheck",
  Seq(
    "clean",
    "scalafmtSbtCheck",
    "scalafmtCheckAll",
    "test:compile",
    "test",
  ).mkString(";"),
)
