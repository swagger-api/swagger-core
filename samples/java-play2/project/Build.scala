import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "petstore"
  val appVersion = "1.2.6-SNAPSHOT"

  val appDependencies: Seq[sbt.ModuleID] = Seq(
<<<<<<< HEAD
    "com.wordnik" %% "swagger-play2-utils" % "1.2.6-SNAPSHOT"
=======
    "com.wordnik" %% "swagger-play2" % "1.3-SNAPSHOT",
    "com.wordnik" %% "swagger-play2-utils" % "1.3-SNAPSHOT"
>>>>>>> 2abdda71405c19c69c23807ffe562e945d310299
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers := Seq(
      "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
      "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases",
      "java-net" at "http://download.java.net/maven/2",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"))
}
