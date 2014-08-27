import sbt._
import Keys._
import play.Play.autoImport._
import play.PlayScala
import PlayKeys._

object ApplicationBuild extends Build {
  val appName = "swagger-play2"
  val appVersion = "1.3.8"

  checksums in update := Nil

  scalaVersion:= "2.11.1"

  val appDependencies = Seq(
    "org.slf4j" % "slf4j-api" % "1.6.4",
    "com.wordnik" %% "swagger-jaxrs" % "1.3.8",
    "javax.ws.rs" % "jsr311-api" % "1.1.1",
    "org.mockito" % "mockito-core" % "1.9.5" % "test")

  val main = Project(appName, file(".")).enablePlugins(PlayScala).settings(
    crossScalaVersions := Seq("2.10.4", "2.11.1"),
    scalaVersion := "2.11.1",
    version := appVersion,
    libraryDependencies ++= appDependencies,
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
    pomExtra :=
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
      </developers>,
    resolvers := Seq(
      "maven-central" at "https://repo1.maven.org/maven2",
      "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
      "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases",
      "java-net" at "http://download.java.net/maven/2",
      "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"))
}
