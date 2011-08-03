# Swagger Core library

## Overview
This is a project to build the swagger-core library, which is required for the Wordnik 
implementation of the Swagger spec.  You can find out more about both the spec and the
framework at http://swagger.wordnik.com  

### Prerequisites
You need the following installed and available in your $PATH:

<li>- Java 1.6 or greater (http://java.oracle.com)

<li>- Apache ant 1.7 or greater (http://ant.apache.org/)

<li>- Scala 2.8.1 or greater (http://www.scala-lang.org)

### To build
If you don't have the Apache Ivy dependency manager installed, run this build script:

<pre>
ant -f install-ivy # only needed if you don't have apache ivy installed
ant dist
</pre>

