# Swagger Core library

## Overview
This is a project to build the swagger-core library, which is required for the Wordnik 
implementation of the Swagger spec.  You can find out more about both the spec and the
framework at http://swagger.wordnik.com.  For more information about Wordnik's APIs, please
visit http://developer.wordnik.com.  

### Prerequisites
You need the following installed and available in your $PATH:

<li>- Java 1.6 or greater (http://java.oracle.com)

<li>- Apache ant 1.7 or greater (http://ant.apache.org/)

<li>- Scala 2.8.1 or greater (http://www.scala-lang.org)

### To build
If you don't have the Apache Ivy dependency manager installed, run this build script:

<pre>
ant -f install-ivy.xml
</pre>

This will copy the ivy ant lib into your antlib directory.  Now you can build the artifact:

<pre>
ant
</pre>

This will create the swagger-core library in your build folder.  You can optionally deploy the
artifact to your local ivy repo so it's available to the swagger sample app:

<pre>
ant deploy
</pre>
