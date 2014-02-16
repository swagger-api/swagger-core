import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._

object PetstoreBuild extends Build {
  val Organization = "com.wordnik"
  val Name = "Petstore"
  val Version = "1.0.0"
  val ScalaVersion = "2.10.3"
  val ScalatraVersion = "2.2.2"

  lazy val project = Project (
    "petstore",
    file("."),
    settings = Defaults.defaultSettings ++ ScalatraPlugin.scalatraWithJRebel ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate"    % ScalatraVersion,
        "org.scalatra" %% "scalatra-swagger"    % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2"     % ScalatraVersion % "test",
        "org.json4s"   %% "json4s-jackson"      % "3.1.0",
        "org.json4s"   %% "json4s-ext"          % "3.1.0",
        "com.wordnik"  %  "swagger-annotations" % "1.3.2",

        "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106" % "container",
        "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))
      )
    )
  )
}
