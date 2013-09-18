// Comment to get more information during initialization
// logLevel := Level.Warn

resolvers ++= Seq(
    DefaultMavenRepository,
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    Resolver.url("Play", url("http://download.playframework.org/ivy-releases/"))(Resolver.ivyStylePatterns)
)

resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.0-RC2")
