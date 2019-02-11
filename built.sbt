import sbt.Keys.libraryDependencies

lazy val root = (project in file("."))
  .settings(
    name         := "FizzBuzz Mappy",
    organization := "soat",
    scalaVersion := "2.11.8",
    version      := "0.1.0-SNAPSHOT",
	libraryDependencies += "org.scalafx" % "scalafx_2.11" % "8.0.102-R11",
	libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.2.0",
	libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.2.0",
		libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6",
		libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.8",
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.2.9",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  )
