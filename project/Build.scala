import sbt._
import Keys._
import play.Play.autoImport._
import PlayKeys._

object Build extends Build {
  import BuildSettings._
  import Dependencies._

  // -------------------------------------------------------------------------------------------------------------------
  // Root Project
  // -------------------------------------------------------------------------------------------------------------------

  lazy val root = Project("root",file("."))
    .aggregate(annotations, core, jaxrs, jersey, jersey2, servlet, utils, oauth2Server, play2, play2Util)
    .settings(swaggerModuleSettings: _*)
    .settings(noPublishing: _*)

  // -------------------------------------------------------------------------------------------------------------------
  // Modules
  // -------------------------------------------------------------------------------------------------------------------	
  lazy val annotations = Project("swagger-annotations", file("modules/swagger-annotations"))
    .settings(swaggerModuleSettings: _*)
    .settings(osgiSettings(exports = Seq("com.wordnik.swagger.annotations")): _*)
	
	
  lazy val core = Project("swagger-core", file("modules/swagger-core"))
    .dependsOn(annotations)
    .settings(swaggerModuleSettings: _*)
    .settings(osgiSettings(exports = Seq(
	   "com.wordnik.swagger.converter",
	   "com.wordnik.swagger.core",
	   "com.wordnik.swagger.core.util",
	   "com.wordnik.swagger.reader",
	   "com.wordnik.swagger.config",
	   "com.wordnik.swagger.model"
	 ), imports = Seq(
       "org.json4s.jackson",
       "*"
     )): _*)
    .settings(libraryDependencies ++= Seq(
	    commonsLang,
		self4jApi,
		jacksonModule,
		jacksonSchema,
		jacksonJaxrs,
		json4sExt,
		json4sNative,
		json4sJackson,
		jodaTime,
		jodaConvert,
		logbackClassis,
		junit,
		scalatest
	  )
    )
	
  lazy val jaxrs = Project("swagger-jaxrs", file("modules/swagger-jaxrs"))
    .dependsOn(core)
    .settings(swaggerModuleSettings: _*)
    .settings(libraryDependencies ++= Seq(
	    javaxServlet,
		jsr311Api,
		reflections,
		junit,
		scalatest
	  )
    )
	
  lazy val jersey = Project("swagger-jersey-jaxrs", file("modules/swagger-jersey-jaxrs"))
    .dependsOn(jaxrs)
    .settings(swaggerModuleSettings: _*)
    .settings(libraryDependencies ++= Seq(
	    jerseyMultipart,
		jerseyServlet,
		jerseyClient,
		jerseyCore,
		jerseyServer,
		
		junit,
		scalatest
	  )
    )
	
  lazy val jersey2 = Project("swagger-jersey2-jaxrs", file("modules/swagger-jersey2-jaxrs"))
    .dependsOn(jaxrs)
    .settings(swaggerModuleSettings: _*)
    .settings(libraryDependencies ++= Seq(
	    glassfishJerseyContainer,
		glassfishMediaMultipart,
		
		junit,
		scalatest
	  )
    )
	
  lazy val servlet = Project("swagger-servlet", file("modules/swagger-servlet"))
    .dependsOn(core)
    .settings(swaggerModuleSettings: _*)
    .settings(libraryDependencies ++= Seq(
	    javaxServlet,
		reflections,
		
		junit,
		scalatest
	  )
    )
	
  lazy val utils = Project("swagger-utils", file("modules/swagger-utils"))
    .dependsOn(core)
    .settings(swaggerModuleSettings: _*)
    .settings(libraryDependencies ++= Seq(
	    javaxServlet,
		reflections,

		junit,
		scalatest
	  )
    )
	
  lazy val oauth2Server = Project("swagger-oauth2-auth-server", file("modules/swagger-oauth2-auth-server"))
    .dependsOn(core)
    .settings(swaggerModuleSettings: _*)
    .settings(libraryDependencies ++= Seq(
	    javaxServlet,
		reflections,
		apacheAuthzServer,

		junit,
		scalatest
	  )
    )
   lazy val play2Util = Project("swagger-play2-utils", file("modules/swagger-play2-utils"))
    .enablePlugins(play.PlayScala)
    .dependsOn(core)
    .settings(swaggerModuleSettings: _*)
	.settings(
        scalaSource in Compile := baseDirectory.value / "app",
		resolvers += Resolvers.sonatypeNexusSnapshots,
		libraryDependencies += "com.wordnik" %% "common-utils" % "1.3.0-SNAPSHOT"
    )
	
  lazy val play2 = Project("swagger-play2", file("modules/swagger-play2"))
    .enablePlugins(play.PlayScala)
    .dependsOn(jaxrs)
    .settings(swaggerModuleSettings: _*)
	.settings(
        scalaSource in Compile := baseDirectory.value / "app",
		libraryDependencies += mockitoCore
	 )
	
   // -------------------------------------------------------------------------------------------------------------------
   // Some Samples
   // -------------------------------------------------------------------------------------------------------------------

   lazy val javaPlay2Sample = Project("java-play2", file("samples/java-play2"))
    .enablePlugins(play.PlayJava)
    .dependsOn(play2, play2Util)
    .settings(swaggerModuleSettings: _*)
	.settings(
        scalaSource in Compile := baseDirectory.value / "app",
		resolvers += Resolvers.sonatypeNexusSnapshots  // for common-utils/1.3.0-SNAPSHOT used by play2-util
    )

   lazy val scalaPlay2Sample = Project("scala-play2", file("samples/scala-play2"))
    .enablePlugins(play.PlayScala)
    .dependsOn(play2, play2Util)
    .settings(swaggerModuleSettings: _*)
	.settings(
        scalaSource in Compile := baseDirectory.value / "app",
		resolvers += Resolvers.sonatypeNexusSnapshots  // for common-utils/1.3.0-SNAPSHOT used by play2-util
    )
	
   import com.earldouglas.xwp.XwpPlugin._
   
   lazy val scalaServletSample = Project("scala-servlet", file("samples/scala-servlet"))
    .webappDependsOn(servlet, jaxrs, core, annotations)
    .settings(swaggerModuleSettings: _*)
	.settings(jetty():_*)
    .settings(libraryDependencies ++= Seq(
	      javaxServlet,
		  jettyServlets,
	      junit,
	      scalatest,
	      logbackClassis
	   )
    )
	
   lazy val scalaJaxrsSample = Project("scala-jaxrs", file("samples/scala-jaxrs"))
    .webappDependsOn(servlet, jaxrs, core, annotations)
    .settings(swaggerModuleSettings: _*)
	.settings(jetty():_*)
    .settings(libraryDependencies ++= Seq(
	      javaxServlet,
		  jettyServlets,
		  jerseyServlet,
		  jerseyServer,
		  jerseyCore,
	      junit,
	      scalatest,
	      logbackClassis
	   )
    )

	lazy val oauth2ServerSample = Project("scala-oauth-authorization-server", file("samples/scala-oauth-authorization-server"))
    .webappDependsOn(jersey, jaxrs, oauth2Server, core, annotations)
    .settings(swaggerModuleSettings: _*)
	.settings(jetty():_*)
    .settings(libraryDependencies ++= Seq(
	      javaxServlet,
		  jettyServlets,
		  apacheOauth2Client,
		  jerseyBundle,
		  commonsIO,
	      junit,
	      scalatest,
	      logbackClassis
	   )
    )

	lazy val noServerSample = Project("no-server", file("samples/no-server"))
    .settings(swaggerModuleSettings: _*)
	.settings(jetty():_*)
	
   import sbtassembly.Plugin._
   import AssemblyKeys._

   lazy val javaDropwizardSample = Project("java-dropwizard", file("samples/java-dropwizard"))
    .webappDependsOn(jaxrs)
    .settings(swaggerModuleSettings: _*)
	.settings(assemblySettings :_*)
	.settings(mainClass in assembly := Some("com.wordnik.swagger.sample.SwaggerSampleService"))
    .settings(libraryDependencies ++= Seq(
	   dropwizardCore,
	   scalatest,
	   logbackClassis),
       mergeStrategy in assembly <<= (mergeStrategy in assembly) { 
		  (old) =>
          {
             case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first 
             case PathList("javax", "ws", "rs", xs @ _*) => MergeStrategy.first
			 case PathList(ps @ _*) if ps.last endsWith ".xml" => MergeStrategy.first  // logback.xml conflicted
             case x => old(x)
          }
        }
	  )
}
