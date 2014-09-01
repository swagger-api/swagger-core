import sbt._

object Dependencies {

  val resolutionRepos = Seq()
  
  val json4sExt = "org.json4s" %% "json4s-ext" % "3.2.10"
  val json4sNative = "org.json4s" %% "json4s-native" % "3.2.10"
  val json4sJackson = "org.json4s" %% "json4s-jackson" % "3.2.10"
  val jacksonModule = "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.4.1"
  val jacksonSchema = "com.fasterxml.jackson.module" % "jackson-module-jsonSchema" % "2.4.1"
  val jacksonJaxrs = "com.fasterxml.jackson.jaxrs" % "jackson-jaxrs-json-provider" % "2.4.1"
  val jodaTime = "joda-time" % "joda-time" % "2.3"
  val jodaConvert = "org.joda" % "joda-convert" % "1.2"
  val commonsLang = "commons-lang" % "commons-lang" % "2.4"
  val commonsIO = "commons-io" % "commons-io" % "2.4"
  val self4jApi = "org.slf4j" % "slf4j-api" % "1.6.3"
  //val javaxServlet = "javax.servlet" % "servlet-api" % "2.5"
  val javaxServlet = "javax.servlet" % "javax.servlet-api" % "3.1.0"
  val jsr311Api = "javax.ws.rs" % "jsr311-api" % "1.1.1"
  val reflections = "org.reflections" % "reflections" % "0.9.9-RC1"
  val jerseyMultipart = "com.sun.jersey.contribs" % "jersey-multipart" % "1.13"
  val jerseyServlet = "com.sun.jersey" % "jersey-servlet" % "1.13"
  val jerseyClient = "com.sun.jersey" % "jersey-client" % "1.13"
  val jerseyCore = "com.sun.jersey" % "jersey-core" % "1.13"
  val jerseyServer = "com.sun.jersey" % "jersey-server" % "1.13"
  val jerseyBundle = "com.sun.jersey" % "jersey-bundle" % "1.13"
  val glassfishJerseyContainer = "org.glassfish.jersey.containers" % "jersey-container-servlet-core" % "2.1"
  val glassfishMediaMultipart = "org.glassfish.jersey.media" % "jersey-media-multipart" % "2.1"
  val apacheAuthzServer = "org.apache.oltu.oauth2" % "org.apache.oltu.oauth2.authzserver" % "0.31"
  val apacheOauth2Client = "org.apache.oltu.oauth2" % "org.apache.oltu.oauth2.client" % "0.31"
  val jettyServlets = "org.eclipse.jetty" % "jetty-servlets" % "9.2.1.v20140609"

  val dropwizardCore = "com.yammer.dropwizard" % "dropwizard-core" % "0.6.2"
  
  val mockitoCore = "org.mockito" % "mockito-core" % "1.9.5" % "test"
  val scalatest = "org.scalatest" %% "scalatest" % "2.2.0"% "test"
  val junit = "junit" % "junit" % "4.8.1" % "test"

  val logbackClassis = "ch.qos.logback" % "logback-classic" % "1.1.2" % "provided"
}

