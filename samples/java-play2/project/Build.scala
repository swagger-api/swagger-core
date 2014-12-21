import sbt._
import Keys._
import play.Play.autoImport._
import play.PlayJava
import PlayKeys._

object ApplicationBuild extends Build {
  val appName = "petstore"
  val appVersion = "1.3.12-SNAPSHOT"


  val appDependencies: Seq[sbt.ModuleID] = Seq(
    "com.wordnik" %% "swagger-play2" % "1.3.12-SNAPSHOT",
    "com.wordnik" %% "swagger-play2-utils" % "1.3.12-SNAPSHOT"
  )

  val main = Project(appName, file(".")).enablePlugins(play.PlayJava).settings(
    version := appVersion,
    scalaVersion := "2.11.1",
    libraryDependencies ++= appDependencies,
    resolvers := Seq(
      "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
      Resolver.url("Local Ivy Repository", url("file://"+Path.userHome.absolutePath+"/.ivy2/local"))(Resolver.ivyStylePatterns),
      "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases",
      "java-net" at "http://download.java.net/maven/2",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"))
}
