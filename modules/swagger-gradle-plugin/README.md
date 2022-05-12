# swagger-gradle-plugin

**`swagger-gradle-plugin` supports gradle 3.2 and higher.**

## Jakarta namespace support

Since version 2.1.7 Swagger Core supports also Jakarta namespace, with a parallel set of artifacts with `-jakarta` suffix, providing the same functionality as the "standard" `javax` namespace ones.
Please check [Wiki](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Getting-started) for more details

To have gradle plugin use the `jakarta` namespace artifacts, you need to provide a value to parameter `buildClasspath`
In many cases it is sufficient to set it to the `classpath` value, like:

`buildClasspath = classpath`

Alternatively provide as value a classpath with the following dependencies (replacing the provided [`javax` ones](https://github.com/swagger-api/swagger-core/blob/master/modules/swagger-gradle-plugin/src/main/java/io/swagger/v3/plugins/gradle/SwaggerPlugin.java#L16-L25)):

* org.apache.commons:commons-lang3:3.7
* io.swagger.core.v3:swagger-jaxrs2-jakarta:2.1.7
* jakarta.ws.rs:jakarta.ws.rs-api:3.0.0
* jakarta.servlet:jakarta.servlet-api:5.0.0

## Installation


### Gradle 3.2 and higher

```
plugins {
  id "io.swagger.core.v3.swagger-gradle-plugin" version "2.2.0"
}
```
### Gradle 1.x and 2.0

**NOTE**: Since version `2.0.10` gradle 1.x and 2.x up to 3.1 are not supported.

with versions up to `2.0.9`:

```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "io.swagger.core.v3:swagger-gradle-plugin:2.2.0"
  }
}

apply plugin: "io.swagger.core.v3.swagger-gradle-plugin"
```

## Tasks
### resolve

* Resolves project openAPI specification and saves the result in JSON, YAML or both formats.
All parameters except `outputFileName`, `outputFormat`, `classpath`, `skip`, `encoding`, `outputDir` and `outputPath` correspond
to `swagger` [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties) with same name.

#### Example Usage

```

resolve {
    outputFileName = 'PetStoreAPI'
    outputFormat = 'JSON'
    prettyPrint = 'TRUE'
    classpath = sourceSets.main.runtimeClasspath
    resourcePackages = ['io.test']
    outputDir = file('test')
}
```

#### Parameters
Parameter | Description | Required | Default
--------- | ----------- | --------- | -------
`classpath`|classpath for resources to scan (swagger and deps already included in classpath)|true|
`outputDir`|output path where file(s) are saved|true|
`outputFileName`|file name (no extension)|false|`openapi`
`outputFormat`|file format (`JSON`, `YAML`, `JSONANDYAML`|false|`JSON`
`skip`|if `TRUE` skip execution|false|`FALSE`
`encoding`|encoding of output file(s)|false|
`resourcePackages`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`resourceClasses`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`prettyPrint`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|`TRUE`
`sortOutput`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|`FALSE`
`alwaysResolveAppPath`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|`FALSE`
`openApiFile`|openapi file to be merged with resolved specification, equivalent to [config](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties) openAPI|false|
`filterClass`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`readerClass`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`scannerClass`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`readAllResources`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`ignoredRoutes`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`objectMapperProcessorClass`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`modelConverterClasses`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`contextId`|see [Context](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#context)|false|
`outputPath`|**DEPRECATED** output path where file(s) are saved|false|


**Note** parameter `openApiFile` corresponds to [config](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties) openAPI. It points to a location of a file in YAML or JSON format representing the input spec that will be merged with the resolved spec. Typically used to add Info section, or any other meta data.
An example of such file:

```yaml
openapi: 3.0.1
info:
  version: '1.0'
  title: Swagger Pet Sample App Config File
  description: 'This is a sample server Petstore server.  You can find out more
    about Swagger at [http://swagger.io](http://swagger.io) or on [irc.freenode.net,
    #swagger](http://swagger.io/irc/).  For this sample, you can use the api key
    special-key to test the authorization filters.'
  termsOfService: http://swagger.io/terms/
  contact:
    email: apiteam@swagger.io
  license:
    name: Apache 2.0
    url: http://www.apache.org/licenses/LICENSE-2.0.html
```

Since version 2.1.6, `sortOutput` parameter is available, allowing to sort object properties and map keys alphabetically.
Since version 2.1.6, `objectMapperProcessorClass` allows to configure also the ObjectMapper instance used to serialize the resolved OpenAPI
Since version 2.1.9, `alwaysResolveAppPath` parameter is available, allowing to trigger resolving of Application Path from annotaion also not in runtime (e.g. using servlet in separate application, or in maven plugin at build time, etc)

