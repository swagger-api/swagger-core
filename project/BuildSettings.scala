import sbt._
import Keys._
import com.typesafe.sbt.SbtPgp
import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin._

object BuildSettings {
  val appVersion = "1.3.8"
  val thisScalaVersion = "2.11.1"
  val scalaVersions4ScrossBuild = Seq("2.11.1", "2.10.4")

  lazy val basicSettings = seq(
    version               := appVersion,
    homepage              := Some(new URL("https://github.com/wordnik/swagger-core")),
    organization          := "com.wordnik",
    licenses              := Seq("Apache License 2.0" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.html")),
    scalaVersion          := thisScalaVersion,
    resolvers             ++= Dependencies.resolutionRepos,
    scalacOptions         := Seq(
      "-encoding", "utf8",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-target:jvm-1.6",
      "-language:_",
      "-Xlog-reflective-calls"
    )
  )
  
  lazy val excludeLogBackXMLFile = mappings in (Compile, packageBin) ~= { _.filter(_._1.getName != "logback.xml") }

  lazy val swaggerModuleSettings =
    basicSettings ++
    SbtPgp.settings ++
    seq(
      // scaladoc settings
      (scalacOptions in doc) <++= (name, version).map { (n, v) => Seq("-doc-title", n, "-doc-version", v) },
      crossScalaVersions := scalaVersions4ScrossBuild,
	  excludeLogBackXMLFile,

      // publishing
      crossPaths := true, //you should never set crossVersions to false on publicly published Scala artifacts.
      publishMavenStyle := true,
      SbtPgp.useGpg := true,
	  credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
      publishTo <<= (version) { version: String =>
         val nexus = "https://oss.sonatype.org/"
         if (version.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
         else                             Some("releases" at nexus + "service/local/staging/deploy/maven2")
      },
      pomIncludeRepository := { _ => false },
      pomExtra :=
        <scm>
           <connection>scm:git:git@github.com:wordnik/swagger-core.git</connection>
           <developerConnection>scm:git:git@github.com:wordnik/swagger-core.git</developerConnection>
           <url>https://github.com/wordnik/swagger-core</url>
        </scm>
        <developers>
           <developer>
             <id>fehguy</id>
             <name>Tony Tam</name>
             <email>fehguy@gmail.com</email>
           </developer>
        </developers>
    )
	
  lazy val noPublishing = seq(
    publish := (),
    publishLocal := (),
    // required until these tickets are closed https://github.com/sbt/sbt-pgp/issues/42,
    // https://github.com/sbt/sbt-pgp/issues/36
    publishTo := None
  )

  lazy val docsSettings = basicSettings ++ noPublishing ++ seq(
    unmanagedSourceDirectories in Test <<= baseDirectory { _ ** "code" get }
  )
  
  object Resolvers {
    val sonatypeNexusSnapshots = "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
    val sonatypeNexusReleases = "Sonatype Nexus Releases" at "https://oss.sonatype.org/content/repositories/releases"
  }  
  
  import com.typesafe.sbt.osgi.SbtOsgi
  import SbtOsgi._
  def osgiSettings(exports: Seq[String], imports: Seq[String] = Seq.empty) =
    SbtOsgi.osgiSettings ++ Seq(
      OsgiKeys.exportPackage := exports map { pkg => pkg + ";version=\"${Bundle-Version}\"" },
      OsgiKeys.importPackage <<= scalaVersion { sv => Seq("""scala.*;version="$<range;[==,=+);%s>"""".format(sv)) },
      OsgiKeys.importPackage ++= imports,
      OsgiKeys.additionalHeaders := Map("-removeheaders" -> "Include-Resource,Private-Package")
    )
  
}
