# Swagger Core library

## Overview
This is a project to build the swagger-core library, which is required for the Wordnik 
implementation of the Swagger spec.  You can find out more about both the spec and the
framework at http://swagger.wordnik.com.  For more information about Wordnik's APIs, please
visit http://developer.wordnik.com.

The spec is documented here:  [swagger-spec](/wordnik/swagger-core/wiki)

## Version history

v1.01 Jan-31 2012

<li>- Transitioned build to Maven</li>

<li>- Removed jaxrs dependencies from swagger-core</li>

<li>- Moved sample server implementations into /samples directory</li>

<li>- Added support for alternate API listing paths</li>

<li>- Made .{format} optional</li>

<li>- Added crazy scala versioning into artifact names (Scala 2.8.1 => ${artifact}_2.8.1)

<li>- Added test and integration test through default maven integration-test lifecycle</li>

v1.0 Aug-10 2011

<li>- Initial release of swagger</li>

Pre-release versions will be suffixed with SNAPSHOT and RC appropriately.  If you want the
release version, please grab it by tag (i.e. v1.0 for the release)

### Prerequisites
You need the following installed and available in your $PATH:

<li>- Java 1.6 or greater (http://java.oracle.com)

<li>- Apache maven 3.0.3 or greater (http://maven.apache.org/)

<li>- Scala 2.8.1 (http://www.scala-lang.org).

### To build from source (currently 1.02-SNAPSHOT)
<pre>
# first time building locally
mvn -N
</pre>

Subsequent builds
<pre>
mvn install
</pre>

This will build the modules and sample apps.  

## Sample Apps
There are a number of sample apps in the 

[java-jaxrs](/wordnik/swagger-core/tree/master/samples/java-jaxrs/README.md) Java-based swagger server with JAX-RS

[scala-jaxrs](/wordnik/swagger-core/tree/master/samples/scala-jaxrs/README.md) Scala-based swagger server with JAX-RS

[java-alt-resource-listing](/wordnik/swagger-core/tree/master/samples/java-alt-resource-listing/README.md) 
Scala-based swagger server with JAX-RS with an alternate resource listing scheme

[scala-alt-resource-listing](/wordnik/swagger-core/tree/master/samples/scala-alt-resource-listing/README.md) 
Scala-based swagger server with JAX-RS with an alternate resource listing scheme

[scala-jaxrs-apm](/wordnik/swagger-core/tree/master/samples/scala-jaxrs-apm/README.md) 
Scala-based swagger server using wordnik-oss utils for Application Performance Monitoring (APM)

To run a sample app:

<pre>
# run scala-jaxrs sample app
cd samples/scala-jaxrs

mvn jetty:run
</pre>

Of course if you don't want to build locally you can grab artifacts from maven central:

http://repo1.maven.org/maven2/com/wordnik/