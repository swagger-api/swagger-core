import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "petstore"
  val appVersion = "1.2.4"

  val appDependencies: Seq[sbt.ModuleID] = Seq(
    "com.wordnik" %% "swagger-play2-utils" % "1.2.4"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers := Seq(
      "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
      "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases",
      "java-net" at "http://download.java.net/maven/2",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"))
}
