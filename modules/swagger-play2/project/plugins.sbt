// Comment to get more information during initialization
// logLevel := Level.Warn

resolvers ++= Seq(
    DefaultMavenRepository,
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    Resolver.url("Play", url("http://download.playframework.org/ivy-releases/"))(Resolver.ivyStylePatterns)
)

addSbtPlugin("play" % "sbt-plugin" % "2.1.0")