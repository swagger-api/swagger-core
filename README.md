# Swagger Core library

[![Build Status](https://travis-ci.org/swagger-api/swagger-core.svg?branch=develop_2.0)](https://travis-ci.org/swagger-api/swagger-core)

The goal of Swagger™ is to define a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection. When properly defined via Swagger, a consumer can understand and interact with the remote service with a minimal amount of implementation logic. Similar to what interfaces have done for lower-level programming, Swagger removes the guesswork in calling the service.

Swagger-core is the Java implementation of Swagger. Current version supports *JAX-RS*. Future milestone releases will add support for *plain Servlets* and *Play Framework*.

Check out [Swagger-Spec](https://github.com/swagger-api/swagger-spec) for additional information about the Swagger project, including additional libraries with support for SpringMVC, other languages and more. 

## See the Wiki!
The [github wiki](https://github.com/swagger-api/swagger-core/wiki) contains documentation, samples, etc.  Start there.

## Support
The following methods are available to obtain support for Swagger:

- [The Swagger Google Group](https://groups.google.com/forum/#!forum/swagger-swaggersocket) - This would normally be your first stop to get support for Swagger. Here you can find previously asked question, and ask new ones. When asking a question, please provide as much information as you can regarding the environment you use (development language, library, versions.
- The [Issues tab](https://github.com/swagger-api/swagger-core/issues?state=open) - Please open feature requests and bugs here. If you're not sure you encountered a bug, or if it's a general usage question, please use the Google Group mentioned above.
- IRC! you can find us on [freenode](http://webchat.freenode.net/?channels=swagger) in the channel #Swagger. You can talk with us directly there.


## Get started with Swagger!
See the guide on [getting started with swagger](https://github.com/swagger-api/swagger-core/wiki/Swagger-Core-JAX-RS-Project-Setup) to get started with adding swagger to your API.

## Compatibility
The Swagger Specification has undergone 3 revisions since initial creation in 2010.  The swagger-core project has the following compatibilities with the swagger specification:

Swagger core Version      | Release Date | Swagger Spec compatibility | Notes | Status
------------------------- | ------------ | -------------------------- | ----- | ----
1.5.0-M2                  | 2015-03-30   | 2.0           | [master](https://github.com/swagger-api/swagger-core) |
1.3.12                    | 2014-12-23   | 1.2           | [tag v1.3.12](https://github.com/swagger-api/swagger-core/tree/v1.3.12)
1.2.4                     | 2013-06-19   | 1.1           | [tag swagger-project_2.10.0-1.2.4](https://github.com/swagger-api/swagger-core/tree/swagger-project_2.10.0-1.2.4)
1.0.0                     | 2011-10-16   | 1.0           | [tag v1.0](https://github.com/swagger-api/swagger-core/tree/v1.0)

## Overview
This is a project to build the swagger-core library, which is required for the Reverb implementation of the Swagger spec. 

### Change History
If you're interested in the change history of swagger and the swagger-core framework, see [here](https://github.com/swagger-api/swagger-core/wiki/Changelog).


### Prerequisites
You need the following installed and available in your $PATH:

<li>- Java 6 (http://java.oracle.com)

<li>- Apache maven 3.0.4 or greater (http://maven.apache.org/)

<li>- Jackson 2.4.2 or greater

### To build from source (currently 1.5.0-M2)
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
There are a number of sample apps in the [samples](https://github.com/swagger-api/swagger-core/tree/develop_2.0/samples) folder.

```

License
-------

Copyright 2015 Reverb Technologies, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at [apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
