import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "petstore"
  val appVersion = "1.2.1-SNAPSHOT"

  val appDependencies: Seq[sbt.ModuleID] = Seq(
    "com.wordnik" %% "swagger-play2" % "1.2.1-SNAPSHOT")

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
    resolvers := Seq(
      "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
      "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases",
      "java-net" at "http://download.java.net/maven/2",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"))
}
