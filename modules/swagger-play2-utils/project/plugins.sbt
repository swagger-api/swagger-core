resolvers ++= Seq(
    DefaultMavenRepository,
    Resolver.url("Play", url("http://download.playframework.org/ivy-releases/"))(Resolver.ivyStylePatterns)
)

addSbtPlugin("play" % "sbt-plugin" % "2.0")
//libraryDependencies += "play" %% "play" % "2.0-beta"
