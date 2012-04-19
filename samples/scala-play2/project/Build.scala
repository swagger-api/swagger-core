import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "petstore"
  val appVersion = "1.0"

  Credentials.add("wordnik-remote-repos", "ci.aws.wordnik.com", "mavenuser", "DEEaffe987a")

  val appDependencies: Seq[sbt.ModuleID] = Seq(
    "org.codehaus.jackson" % "jackson-jaxrs" % "1.8.5",
    "org.codehaus.jackson" % "jackson-xc" % "1.8.5",
    "org.codehaus.jackson" % "jackson-mapper-asl" % "1.8.5",
    "org.codehaus.jackson" % "jackson-core-asl" % "1.8.5",
    "swagger-play2" %% "swagger-play2" % "1.0.1")

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    resolvers += "java-net" at "http://download.java.net/maven/2",
    resolvers += Resolver.url("local-ivy", new URL("file://" + Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("local-ivy-cache", new URL("file://" + Path.userHome.absolutePath + "/.ivy2/cache"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("local-ivy-cache-mvn", new URL("file://" + Path.userHome.absolutePath + "/.ivy2/cache"))(Resolver.mavenStylePatterns),
    resolvers += "local-maven-repo" at "file://" + Path.userHome.absolutePath + "/.m2/repository/")
}
