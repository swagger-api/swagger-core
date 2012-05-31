import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {
  val appName = "swagger-play2"
  val appVersion = "1.1-SNAPSHOT"

  val appDependencies: Seq[sbt.ModuleID] = Seq(
    "org.codehaus.jackson" % "jackson-jaxrs" % "1.8.5",
    "org.codehaus.jackson" % "jackson-xc" % "1.8.5",
    "org.codehaus.jackson" % "jackson-mapper-asl" % "1.8.5",
    "org.codehaus.jackson" % "jackson-core-asl" % "1.8.5",
    "org.slf4j" % "slf4j-api" % "1.6.4",
    "com.wordnik" % "swagger-core_2.9.1" % "1.1-SNAPSHOT",
    "com.wordnik" % "swagger-annotations_2.9.1" % "1.1-SNAPSHOT",
    "javax.ws.rs" % "jsr311-api" % "1.1.1")

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    organization := "com.wordnik",
    resolvers := Seq(
      Resolver.url("swagger-core-github-repo", url("http://wordnik.github.com/repository"))(Resolver.ivyStylePatterns),
      "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "java-net" at "http://download.java.net/maven/2",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"))
}
