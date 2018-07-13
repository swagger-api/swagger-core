This is a better version for Swagger Core with the addition of many features for those who would like to directly build the OpenAPI object instead of letting Swagger handle things automatically. Some options should also be valid for those using the automatic way.

By directly manipulating the OpenAPI object and this version, you have lots of customization that the standard versions can only dream of. For example:

1. You can add addition models to your OpenAPI with OpenAPIBuilderToolbox.add(api, SomeClass.class). 

2. If you only care about the documentation, not code generation and sometimes you want the Schema to have the fullname of the class (including package name), you can just set OpenAPIBuilderOptions.USE_FULLNAME to true.

3. For generic class like ClassA<T>, the generated API is sometimes ugly lile ClassAObject, and if you want to ignore that generic part to have only ClassA, you can just set OpenAPIBuilderOptions.OMIT_GENERIC to true.
 
4. For Enum, sometimes the default toString() and name() may differ, so if you want to ensure that name() is always used, you can just set OpenAPIBuilderOptions.USE_ENUMNAME to true.

5. For Enum, sometimes you have only 1 enum, but you have to use it lots of times, isn't it much better if we can declare it as a Schema and just put a reference for it instead of inlining all the time? You can achieve that easily by setting the OpenAPIBuilderOptions.RECYCLE_ENUM to true.

6. You know, you can specify parent with Schema(allOf = {...}), but then in the generated OpenAPI, all the properties from parent will be repeated again, which is a bit annoying. Therefore, how about we exclude all of those which have been declared in parent schema? Just set OpenAPIBuilderOptions.HIDE_PARENTS to true.

7. Last, but not least, sometimes, you do not want to use the default visibility, right? Normally, you want to also see the private fields, ignore setter and getter, but how can we do it in the old version? Use annotation? Oh my god, imagine that I have a bit more than 1000 classes in my project and you seriously expect me to put the damn annotations on all of them? Fat chance :V !!! Here, I expose the default object mapper in ObjectMapperFactory.defaultMapper and you can use it to control a lot more than you think.

Bonus: If you want to build the OpenAPI object directly, you can use Reflections to achieve much better results compared to letting Swagger do everything on its own.

Lam Gia Thuan

A super handsome programmer who feels frustrated with Swagger Core's lack of functionalities :) 

**NOTE:** If you're looking for swagger 1.5.X and OpenApi 2.0, please refer to [1.5 branch](https://github.com/swagger-api/swagger-core/tree/1.5)

# Swagger Core library

[![Build Status](https://travis-ci.org/swagger-api/swagger-core.svg?branch=master)](https://travis-ci.org/swagger-api/swagger-core)

The goal of Swagger™ is to define a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection. When properly defined via Swagger, a consumer can understand and interact with the remote service with a minimal amount of implementation logic. Similar to what interfaces have done for lower-level programming, Swagger removes the guesswork in calling the service.

Swagger-core is the Java implementation of Swagger. Current version supports *JAX-RS2*.

Check out the [OpenAPI Specification](https://github.com/OAI/OpenAPI-Specification) for additional information about the Swagger project.

## Get started with Swagger!
See the guide on [getting started with swagger](https://github.com/frantuma/swagger-core/wiki/Swagger-2.X---Getting-started) to get started with adding swagger to your API.

## See the Wiki!
The [github wiki](https://github.com/swagger-api/swagger-core/wiki) contains documentation, samples, contributions, etc. Start there.

## Support
The following methods are available to obtain support for Swagger:

- [The Swagger Google Group](https://groups.google.com/forum/#!forum/swagger-swaggersocket) - This would normally be your first stop to get support for Swagger. Here you can find previously asked question, and ask new ones. When asking a question, please provide as much information as you can regarding the environment you use (development language, library, versions.
- The [Issues tab](https://github.com/swagger-api/swagger-core/issues?state=open) - Please open feature requests and bugs here. If you're not sure you encountered a bug, or if it's a general usage question, please use the Google Group mentioned above.
- IRC! you can find us on [freenode](http://webchat.freenode.net/?channels=swagger) in the channel #Swagger. You can talk with us directly there.


## Compatibility
The OpenAPI Specification has undergone several revisions since initial creation in 2010.  The swagger-core project has the following compatibilities with the OpenAPI Specification:

Swagger core Version      | Release Date | OpenAPI Spec compatibility | Notes | Status
------------------------- | ------------ | -------------------------- | ----- | ----
2.0.2  (**current stable**)| 2018-05-24  | 3.0           | [tag v2.0.2](https://github.com/swagger-api/swagger-core/tree/v2.0.2) | Supported
2.0.1                     | 2018-04-16   | 3.0           | [tag v2.0.1](https://github.com/swagger-api/swagger-core/tree/v2.0.1) | Supported
1.5.19 (**current stable**)| 2018-04-16  | 2.0           | [tag v1.5.19](https://github.com/swagger-api/swagger-core/tree/v1.5.19) | Supported
2.0.0                     | 2018-03-20   | 3.0           | [tag v2.0.0](https://github.com/swagger-api/swagger-core/tree/v2.0.0) | Supported
2.0.0-rc4                 | 2018-01-22   | 3.0           | [tag v2.0.0-rc4](https://github.com/swagger-api/swagger-core/tree/v2.0.0-rc4) | Supported
2.0.0-rc3                 | 2017-11-21   | 3.0           | [tag v2.0.0-rc3](https://github.com/swagger-api/swagger-core/tree/v2.0.0-rc3) | Supported
2.0.0-rc2                 | 2017-09-29   | 3.0           | [tag v2.0.0-rc2](https://github.com/swagger-api/swagger-core/tree/v2.0.0-rc2) | Supported
2.0.0-rc1                 | 2017-08-17   | 3.0           | [tag v2.0.0-rc1](https://github.com/swagger-api/swagger-core/tree/v2.0.0-rc1) | Supported
1.5.18                    | 2018-01-22  | 2.0           | [tag v1.5.18](https://github.com/swagger-api/swagger-core/tree/v1.5.18) | Supported
1.5.17                    | 2017-11-21  | 2.0           | [tag v1.5.17](https://github.com/swagger-api/swagger-core/tree/v1.5.17) | Supported
1.5.16                    | 2017-07-15   | 2.0           | [tag v1.5.16](https://github.com/swagger-api/swagger-core/tree/v1.5.16) | Supported
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


### To build from source (currently 2.0.3-SNAPSHOT)
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
Copyright 2018 SmartBear Software

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at [apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
