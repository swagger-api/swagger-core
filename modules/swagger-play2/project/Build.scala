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
    "javax.ws.rs" % "jsr311-api" % "1.1.1")

  val wnRepo = Some(Resolver.url("wordnik-remote-repos", new URL("https://ci.aws.wordnik.com/artifactory/libs-snapshots"))(Resolver.ivyStylePatterns))

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    resolvers += "local-maven-repo" at "file://" + Path.userHome.absolutePath + "/.m2/repository/",
    resolvers += Resolver.url("local-ivy", new URL("file://" + Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("local-ivy-cache", new URL("file://" + Path.userHome.absolutePath + "/.ivy2/cache"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("wordnik-remote-repos", new URL("https://ci.aws.wordnik.com/artifactory/libs-snapshots"))(Resolver.ivyStylePatterns),
    resolvers += "java-net" at "http://download.java.net/maven/2")
}
