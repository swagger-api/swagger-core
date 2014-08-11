import sbt._
import Keys._

name := "petstore"

version := "1.3.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala).settings(
  Defaults.itSettings: _*
)

libraryDependencies ++= Seq(
  "com.wordnik" %% "swagger-play2" % "1.3.7",
  "com.wordnik" %% "swagger-play2-utils" % "1.3.7"
)

resolvers := Seq(
  "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
  Resolver.url("Local Ivy Repository", url("file://"+Path.userHome.absolutePath+"/.ivy2/local"))(Resolver.ivyStylePatterns),
  "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases",
  "java-net" at "http://download.java.net/maven/2",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)
