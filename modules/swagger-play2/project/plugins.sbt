// Comment to get more information during initialization
// logLevel := Level.Warn

resolvers ++= Seq(
    "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
    DefaultMavenRepository,
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
    Resolver.url("Play", url("http://download.playframework.org/ivy-releases/"))(Resolver.ivyStylePatterns)
)

<<<<<<< HEAD
resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"

=======
>>>>>>> 2abdda71405c19c69c23807ffe562e945d310299
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.0")