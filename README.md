# Swagger Core library

[![Build Status](https://travis-ci.org/swagger-api/swagger-core.svg?branch=2.0)](https://travis-ci.org/swagger-api/swagger-core)

The goal of Swagger™ is to define a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection. When properly defined via Swagger, a consumer can understand and interact with the remote service with a minimal amount of implementation logic. Similar to what interfaces have done for lower-level programming, Swagger removes the guesswork in calling the service.

Swagger-core is the Java implementation of Swagger. Current version supports *JAX-RS2*.

Check out the [OpenAPI Specification](https://github.com/OAI/OpenAPI-Specification) for additional information about the Swagger project.

## Support
The following methods are available to obtain support for Swagger:

- [The Swagger Google Group](https://groups.google.com/forum/#!forum/swagger-swaggersocket) - This would normally be your first stop to get support for Swagger. Here you can find previously asked question, and ask new ones. When asking a question, please provide as much information as you can regarding the environment you use (development language, library, versions.
- The [Issues tab](https://github.com/swagger-api/swagger-core/issues?state=open) - Please open feature requests and bugs here. If you're not sure you encountered a bug, or if it's a general usage question, please use the Google Group mentioned above.
- IRC! you can find us on [freenode](http://webchat.freenode.net/?channels=swagger) in the channel #Swagger. You can talk with us directly there.

## Compatibility
The OpenAPI Specification has undergone several revisions since initial creation in 2010.  The swagger-core project has the following compatibilities with the OpenAPI Specification:

Swagger core Version      | Release Date | OpenAPI Spec compatibility | Notes | Status
------------------------- | ------------ | -------------------------- | ----- | ----
2.0.0-rc2                 | 2017-09-29   | 3.0           | [tag v2.0.0-rc2](https://github.com/swagger-api/swagger-core/tree/v2.0.0-rc2) | Under Development
2.0.0-rc1                 | 2017-08-17   | 3.0           | [tag v2.0.0-rc1](https://github.com/swagger-api/swagger-core/tree/v2.0.0-rc1) | Under Development
1.5.16 (**current stable**)| 2017-07-05  | 2.0           | [tag v1.5.16](https://github.com/swagger-api/swagger-core/tree/v1.5.16) | Supported
1.3.12                    | 2014-12-23   | 1.2           | [tag v1.3.12](https://github.com/swagger-api/swagger-core/tree/v1.3.12) | Deprecated
1.2.4                     | 2013-06-19   | 1.1           | [tag swagger-project_2.10.0-1.2.4](https://github.com/swagger-api/swagger-core/tree/swagger-project_2.10.0-1.2.4) | Deprecated
1.0.0                     | 2011-10-16   | 1.0           | [tag v1.0](https://github.com/swagger-api/swagger-core/tree/v1.0) | Deprecated


## Overview
This is a project to build the swagger-core library, which is required for the Swagger implementation of the OpenAPI Spec. 

### Change History
If you're interested in the change history of swagger and the swagger-core framework, see [here](https://github.com/swagger-api/swagger-core/releases).

### Prerequisites
You need the following installed and available in your $PATH:

* Java 8 (http://java.oracle.com)
* Apache maven 3.0.4 or greater (http://maven.apache.org/)
* Jackson 2.4.5 or greater


### To build from source (currently 2.0.0-SNAPSHOT)
```
# first time building locally
mvn -N
```

Subsequent builds:
```
mvn install
```

This will build the modules.

Of course if you don't want to build locally you can grab artifacts from maven central:

`http://repo1.maven.org/maven2/io/swagger/`

## Sample Apps
The samples have moved to [a new repository](https://github.com/swagger-api/swagger-samples/tree/2.0) and contain various integrations and configurations.


## License

```
Copyright 2016 SmartBear Software

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at [apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## v2.0.0-rc2 

### Notable Features:

- See also [rc1 release notes](https://github.com/swagger-api/swagger-core/releases/tag/v2.0.0-rc1)
- Second official release candidate of OpenAPI 3.0 support. swagger-core now produces OpenAPI 3.0 specs only. swagger-core 2.0 version is not backward compatible with previous 1.x versions.
- Available on Maven central, and the sources are in the 2.0 branch. PRs should be submitted against the 2.0 branch.
- `Example/Examples` support (#2416)
- New [2.0 Wiki](https://github.com/swagger-api/swagger-core/wiki)
- Enhanced security support (#2450)
- Enhanced request body support (#2401)
- Response header support (#2427)
- Enhanced support for Parameter, Schema and Content (#2404)
- Specification Filter (#2452)
- Reader listener support (#2464)
- Encoding support (#2463)
- Consumes/Produces full support(#2404)
- Annotation javadocs (#2394)
- Full sample code for [jersey2](https://github.com/swagger-api/swagger-samples/tree/2.0/java/java-jersey2) and [dropwizard](https://github.com/swagger-api/swagger-samples/tree/2.0/java/java-dropwizard)
- Updated dependencies
- Various bug fixes

### Limitations

- Resolve resource operations also when not annotated with `@Operation` _(swagger-jaxrs2)_ 
- Implement `subtypes` field in `Schema` annotation, with related resolver processing _(swagger-annotations / core)_ 
- Overriding full support _(swagger-core)_   
   * Schema annotation `type` + `format` full support _(swagger-core)_ 
- Reader sub-resources support _(swagger-jaxrs2)_  
- Reader extensions full support  _(swagger-jaxrs2)_ 
- Integration layer enhancements:config file location enhanced loading, additional loaders, etc. _(swagger-integration, swagger-jaxrs)_ 
- JsonIdentity support _(swagger-core)_ 
- `MatrixParam` annotation support

## v2.0.0-rc1 

### Notable Features:

- First official release candidate of OpenApi 3.0 support. swagger-core now produces OpenAPI 3.0 specs only. swagger-core 2.0 version is not backward compatible with previous 1.x versions.
- Available on Maven central, and the sources are in the 2.0 branch. PRs should be submitted against the 2.0 branch.
- Swagger JAX-RS 2 support
- Java 8
- Consistent integration mechanism

### Limitations

- Resolve resource operations also when not annotated with `@Operation` _(swagger-jaxrs2)_
- Implement `subtypes` field in `Schema` annotation, with related resolver processing _(swagger-annotations / core)_
- Overriding full support _(swagger-core)_
   * Schema annotation `type` + `format` full support _(swagger-core)_
   * Schema annotation `implementation` field full support  _(swagger-core)_
- Filter / Reader listener support _(swagger-jaxrs2)_
- Reader sub-resources support _(swagger-jaxrs2)_
- Reader extensions full support  _(swagger-jaxrs2)_
- Annotations javadocs _(swagger-annotations)_
- Annotations default values enhancement _(swagger-annotations / core)_
- Integration layer enhancements:config file location enhanced loading, additional loaders, etc. _(swagger-integration, swagger-jaxrs)_
- Consumes/Produces full support _(swagger-jaxrs2)_
- JsonIdentity support _(swagger-core)_
- `MatrixParam` annotation support
