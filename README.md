# Swagger Core library

[![Build Status](https://travis-ci.org/wordnik/swagger-core.png)](https://travis-ci.org/wordnik/swagger-core)

The goal of Swagger™ is to define a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection. When properly defined via Swagger, a consumer can understand and interact with the remote service with a minimal amount of implementation logic. Similar to what interfaces have done for lower-level programming, Swager removes the guesswork in calling the service.

Swagger-core is the Java/Scala implementation of Swagger. It supports *JAX-RS*, *plain Servlets*, and *Play Framework*.

Check out [Swagger-Spec](https://github.com/wordnik/swagger-spec) for additional information about the Swagger project, including additional libraries with support for SpringMVC, other languages and more. 

## See the Wiki!
The [github wiki](https://github.com/wordnik/swagger-core/wiki) contains documentation, samples, etc.  Start there.

## Support
The following methods are available to obtain support for Swagger:

- [The Swagger Google Group](https://groups.google.com/forum/#!forum/swagger-swaggersocket) - This would normally be your first stop to get support for Swagger. Here you can find previously asked question, and ask new ones. When asking a question, please provide as much information as you can regarding the environment you use (development language, library, versions.
- The [Issues tab](https://github.com/wordnik/swagger-core/issues?state=open) - Please open feature requests and bugs here. If you're not sure you encountered a bug, or if it's a general usage question, please use the Google Group mentioned above.
- IRC! you can find us on [freenode](http://webchat.freenode.net/?channels=swagger) in the channel #Swagger. You can talk with us directly there.


## Get started with Swagger!
See the guide on [getting started with swagger](https://github.com/wordnik/swagger-core/wiki/Adding-Swagger-to-your-API) to get started with adding swagger to your API.


## Overview
This is a project to build the swagger-core library, which is required for the Wordnik 
implementation of the Swagger spec. For more information about Wordnik's APIs, please
visit http://developer.wordnik.com.


### Change History
If you're interested in the change history of swagger and the swagger-core framework, see [here](https://github.com/wordnik/swagger-core/wiki/Changelog).


### Prerequisites
You need the following installed and available in your $PATH:

<li>- Java 1.6 or greater (http://java.oracle.com)

<li>- Apache maven 3.0.4 or greater (http://maven.apache.org/)

### To build from source (currently 1.3.7)
```
# first time building locally
mvn -N
```

Subsequent builds:
```
mvn install
```

This will build the modules and sample apps.

Of course if you don't want to build locally you can grab artifacts from maven central:

`http://repo1.maven.org/maven2/com/wordnik/`

## Sample Apps
There are a number of sample apps in the `samples` folder:

[java-jaxrs](https://github.com/wordnik/swagger-core/tree/master/samples/java-jaxrs/README.md) Java-based swagger server with JAX-RS

[scala-jaxrs](https://github.com/wordnik/swagger-core/tree/master/samples/scala-jaxrs/README.md) Scala-based swagger server with JAX-RS

[scala-jaxrs-apm](https://github.com/wordnik/swagger-core/tree/master/samples/scala-jaxrs-apm/README.md) 
Scala-based swagger server using wordnik-oss utils for Application Performance Monitoring (APM)

To run a sample app after initial compile:

```
# run scala-jaxrs sample app
cd samples/scala-jaxrs

mvn jetty:run
```

And the [Play2](http://playframework.org) samples:

[java-play2](https://github.com/wordnik/swagger-core/tree/master/samples/java-play2) Java-based Play2 sample app

[scala-play2](https://github.com/wordnik/swagger-core/tree/master/samples/scala-play2) Scala-based Play2 sample app

To run the Play2 sample apps:

```
cd samples/java-play2

play run
```

License
-------

Copyright 2014 Reverb Technologies, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at [apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
