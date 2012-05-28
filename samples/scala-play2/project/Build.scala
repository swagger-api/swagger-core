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
      Resolver.url("swagger-core-github-repo", url("https://github.com/wordnik/swagger-core/tree/master/modules/swagger-play2/repository"))(Resolver.ivyStylePatterns),
      "local-maven-repo" at "file://" + Path.userHome.absolutePath + "/.m2/repository/",
      Resolver.url("local-ivy", new URL("file://" + Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns),
      Resolver.url("local-ivy-cache", new URL("file://" + Path.userHome.absolutePath + "/.ivy2/cache"))(Resolver.ivyStylePatterns),
      "java-net" at "http://download.java.net/maven/2",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"))
}
