import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "petstore"
  val appVersion = "1.0"

  val appDependencies: Seq[sbt.ModuleID] = Seq(
    "org.codehaus.jackson" % "jackson-jaxrs" % "1.9.5",
    "org.codehaus.jackson" % "jackson-xc" % "1.9.5",
    "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.5",
    "org.codehaus.jackson" % "jackson-core-asl" % "1.9.5",
    "com.wordnik" %% "swagger-play2-utils" % "1.1-SNAPSHOT")

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    resolvers := Seq(
      Resolver.url("swagger-core-github-repo", url("http://wordnik.github.com/repository"))(Resolver.ivyStylePatterns),
      "java-net" at "http://download.java.net/maven/2",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"))
}
