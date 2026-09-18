# Swagger Core <img src="https://raw.githubusercontent.com/swagger-api/swagger.io/wordpress/images/assets/SW-logo-clr.png" height="50" align="right">

![Build Test Deploy](https://github.com/swagger-api/swagger-core/workflows/Build%20Test%20Deploy%20master/badge.svg?branch=3.0.0)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.swagger.core.v3/swagger-project/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/io.swagger.core.v3/swagger-project)

Swagger Core is a Java implementation of the OpenAPI Specification.
Current version supports *Jakarta RESTful Web Services* (`jakarta` namespace).

## What's new in 3.0

Swagger Core 3.0 is a major release that migrates to Jackson 3 and requires Java 17.

Key changes:

- **Java 17** — Java 17 or higher is now required (previously Java 8+)
- **Jackson 3** — runtime dependency moved to `tools.jackson` groupId (version 3.2.2)
- **Jakarta only** — `javax.*` namespace removed; only `jakarta.*` is supported
- **SnakeYAML removed** — replaced by `org.snakeyaml:snakeyaml-engine` via Jackson
- **ObjectMapperProcessor API** — method signatures changed to return `ObjectMapper`

Swagger Core 3.0 contains backward-incompatible changes.
If you are upgrading from Swagger Core 2.x, see the [Migration Guide](MIGRATION.md).

## Compatibility

| Swagger Core | OpenAPI Spec | Java | Status |
|---|---|---|---|
| 3.0.x (**current**) | 3.x (OAS 3.0 and 3.1) | 17+ | Supported |
| 2.x | 3.x (OAS 3.0 and 3.1) | 8+ | See [master branch](https://github.com/swagger-api/swagger-core/tree/master) |
| 1.x | 2.0 | 8+ | See [1.5 branch](https://github.com/swagger-api/swagger-core/tree/1.5) |

## Getting started

### Maven

```xml
<dependency>
  <groupId>io.swagger.core.v3</groupId>
  <artifactId>swagger-core</artifactId>
  <version>3.0.0</version>
</dependency>
```

### Gradle

```kotlin
implementation("io.swagger.core.v3:swagger-core:3.0.0")
```

## Modules

| Module | Description |
|---|---|
| `swagger-annotations` | `@Schema`, `@Operation`, `@Parameter`, and other OpenAPI annotations |
| `swagger-models` | OpenAPI 3.x model POJOs (`Schema`, `OpenAPI`, `Operation`, …) |
| `swagger-core` | Core annotation processing, model conversion, schema resolution |
| `swagger-integration` | OpenAPI integration layer and scanner SPI |
| `swagger-rest` | REST support utilities |
| `swagger-rest-servlet-initializer` | Servlet-based initialization |
| `swagger-maven-plugin` | Maven plugin for spec generation at build time |
| `swagger-gradle-plugin` | Gradle plugin for spec generation at build time |

## Usage

### Basic usage

```java
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.Schema;

Map<String, Schema> schemas = ModelConverters.getInstance().readAll(MyModel.class);
```

See the [wiki](https://github.com/swagger-api/swagger-core/wiki) for full integration guides.

## Maven BOM

The `swagger-bom` artifact is a Bill of Materials that manages all Swagger Core artifact versions.
Import it once and omit versions on all individual Swagger dependencies.

> **Note:** Maven and Gradle build plugins (`swagger-maven-plugin`, `swagger-gradle-plugin`)
> are intentionally **excluded** from the BOM.
> Plugins are applied via `<build><plugins>` or `plugins {}`, not via `<dependencyManagement>`,
> so including them in the BOM would be misleading and could conflict with the plugin
> management section of a consumer's build.

### Maven

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>io.swagger.core.v3</groupId>
      <artifactId>swagger-bom</artifactId>
      <version>3.0.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<!-- Then declare Swagger dependencies without explicit versions -->
<dependencies>
  <dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-annotations</artifactId>
  </dependency>
  <dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-models</artifactId>
  </dependency>
  <dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-core</artifactId>
  </dependency>
  <dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-integration</artifactId>
  </dependency>
  <dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-rest</artifactId>
  </dependency>
  <dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-rest-servlet-initializer</artifactId>
  </dependency>
</dependencies>
```

### Gradle

```kotlin
dependencies {
    implementation(platform("io.swagger.core.v3:swagger-bom:3.0.0"))

    // no version needed
    implementation("io.swagger.core.v3:swagger-annotations")
    implementation("io.swagger.core.v3:swagger-core")
}
```

## Building from source

Requirements:

- Java 17+
- Apache Maven 3.0.4+

```bash
# first-time setup
mvn -N

# build all modules
mvn install
```

Artifacts are also available from Maven Central:
`https://repo1.maven.org/maven2/io/swagger/core/`

## Documentation

- [Getting Started](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Getting-started)
- [Migration Guide: 2.x → 3.0](MIGRATION.md)
- [Wiki](https://github.com/swagger-api/swagger-core/wiki)
- [CHANGELOG](CHANGELOG.md)

## Sample Apps

The samples are in [a separate repository](https://github.com/swagger-api/swagger-samples/tree/2.0)
and contain various integrations and configurations.

## Security contact

Please disclose any security-related issues or vulnerabilities by emailing
[security@swagger.io](mailto:security@swagger.io), instead of using the public issue tracker.
