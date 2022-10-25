import sbt._

object Dependencies {
  lazy val zio = "dev.zio" %% "zio" % "2.0.2"
  lazy val zioNio = "dev.zio" %% "zio-nio" % "2.0.0"
  lazy val zioJson = "dev.zio" %% "zio-json" % "0.3.0"
  lazy val zioHttp = "dev.zio" %% "zio-http" % "2.0.0-RC11+112-1da8838e+20221024-1521-SNAPSHOT"
  lazy val kantanCsv = "com.nrinaudo" %% "kantan.csv" % "0.7.0"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.11"
}
