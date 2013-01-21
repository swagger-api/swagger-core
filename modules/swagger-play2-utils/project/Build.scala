import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
  val appName = "swagger-play2-utils"
  val appVersion = "1.2.1-SNAPSHOT"

  val appDependencies: Seq[sbt.ModuleID] = Seq(
    "org.slf4j" % "slf4j-api" % "1.6.4",
    "com.wordnik" % "swagger-play2_2.10" % "1.2.1-SNAPSHOT",
    "com.wordnik" % "common-utils_2.10.0" % "1.1.5",
    "javax.ws.rs" % "jsr311-api" % "1.1.1")

  val main = play.Project(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    publishArtifact in Test := false,
    publishMavenStyle := true,
    pomIncludeRepository := { x => false },
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    organization := "com.wordnik",
    pomExtra := (
  <url>http://swagger.wordnik.com</url>
  <licenses>
    <license>
      <name>Apache License 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:wordnik/swagger-core.git</url>
    <connection>scm:git:git@github.com:wordnik/swagger-core.git</connection>
  </scm>
  <developers>
    <developer>
      <id>fehguy</id>
      <name>Tony Tam</name>
      <email>fehguy@gmail.com</email>
    </developer>
    <developer>
      <id>ayush</id>
      <name>Ayush Gupta</name>
      <email>ayush@glugbot.com</email>
    </developer>
  </developers>)
    ,
    resolvers := Seq(
      "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases",
      "java-net" at "http://download.java.net/maven/2",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"))
}
