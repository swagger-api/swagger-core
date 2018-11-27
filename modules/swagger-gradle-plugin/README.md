# swagger-gradle-plugin

## Installation
### Gradle 2.1 and higher

```
plugins {
  id "io.swagger.core.v3.swagger-gradle-plugin" version "2.0.7-SNAPSHOT"
}
```
### Gradle 1.x and 2.0

```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.io.swagger.core.v3.swagger-gradle-plugin:2.0.7-SNAPSHOT"
  }
}

apply plugin: "io.swagger.core.v3.swagger-gradle-plugin"
```

## Tasks
### resolve

* Resolves project openAPI specification and saves the result in JSON, YAML or both formats.
All parameters except `outputFileName`, `outputFormat`, `classpath`, `skip`, `encoding` and `outputPath` correspond
to `swagger` [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties) with same name.

#### Example Usage

```

resolve {
    outputFileName = 'PetStoreAPI'
    outputFormat = 'JSON'
    prettyPrint = 'TRUE'
    classpath = sourceSets.main.runtimeClasspath
    resourcePackages = ['io.test']
    outputPath = 'test'
}
```

#### Parameters
Parameter | Description | Required | Default
--------- | ----------- | --------- | -------
`classpath`|classpath for resources to scan (swagger and deps already included in classpath)|true|
`outputPath`|output path where file(s) are saved|true|
`outputFileName`|file name (no extension)|false|`openapi`
`outputFormat`|file format (`JSON`, `YAML`, `JSONANDYAML`|false|`JSON`
`skip`|if `TRUE` skip execution|false|`FALSE`
`encoding`|encoding of output file(s)|false|
`resourcePackages`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`resourceClasses`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`prettyPrint`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|`TRUE`
`openApiFile`|openapi file to be merged with resolved specification, see [config](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`filterClass`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`readerClass`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`scannerClass`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`readAllResources`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`ignoredRoutes`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`objectMapperProcessorClass`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`modelConverterClasses`|see [configuration property](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#configuration-properties)|false|
`contextId`|see [Context](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Integration-and-Configuration#context)|false|

***

