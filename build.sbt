import Dependencies._

resolvers ++= Resolver.sonatypeOssRepos("snapshots")

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.tickets4sale"
ThisBuild / organizationName := "Tickets4Sale"


Docker / dockerUsername := Some("ploddi")
Docker / dockerExposedUdpPorts := Seq(8080)


lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(
    name := "tickets4sale",
    libraryDependencies ++= Seq(
      zio,
      zioNio,
      zioJson,
      zioHttp,
      kantanCsv,
      zioTestJUnit % Test)
  )