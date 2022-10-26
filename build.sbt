import Dependencies._

resolvers ++= Resolver.sonatypeOssRepos("snapshots")

enablePlugins(JavaAppPackaging, DockerPlugin)

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.tickets4sale"
ThisBuild / organizationName := "Tickets4Sale"

Compile / mainClass := Some("com.tickets4sale.tickets.TicketsApp")
Docker / packageName := "ploddi/tickets4sale"
Docker / dockerExposedPorts += 8080

dockerBaseImage := "openjdk:11-slim"
dockerUpdateLatest := true

lazy val root = (project in file("."))
  .settings(
    name := "tickets4sale",
    libraryDependencies ++= Seq(
      zio,
      zioNio,
      zioJson,
      zioHttp,
      kantanCsv,
      zioTestJUnit % Test),

  )