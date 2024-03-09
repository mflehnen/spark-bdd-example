ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

lazy val root = (project in file("."))
  .settings(
    name := "spark-bdd"
  )


lazy val sparkVersion = "3.5.0"
lazy val cucumberVersion = "7.15.0" //"7.2.3"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % Provided,
  "org.apache.spark" %% "spark-sql" % sparkVersion % Provided,
  "org.scalatest" %% "scalatest" % "3.2.11" % Test,
  "io.cucumber" %% "cucumber-scala" % "8.20.0" % Test,
  "io.cucumber" % "cucumber-core" % cucumberVersion % Test,
  "io.cucumber" % "cucumber-junit" % cucumberVersion % Test
)