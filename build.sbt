import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"    // 3.0.2

lazy val root = (project in file("."))
  .settings(
    name := "udemy-advanced-scala"
  )

libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.9.1"

