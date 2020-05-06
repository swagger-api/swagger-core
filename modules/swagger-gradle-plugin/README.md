# swagger-gradle-plugin

**`swagger-gradle-plugin` supports gradle 3.2 and higher.**

## Installation
### Gradle 3.2 and higher

```
plugins {
  id "io.swagger.core.v3.swagger-gradle-plugin" version "2.1.3-SNAPSHOT"
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
    classpath "io.swagger.core.v3:swagger-gradle-plugin:2.1.3-SNAPSHOT"
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
