# Migration Guide: Upstream swagger-api 2.2.x → io.github.vpelikh 4.0.0

This guide covers migrating from `swagger-api/swagger-core` 2.2.x (javax namespace, `io.swagger.core.v3`) to `io.github.vpelikh/swagger-core` **4.0.0** (Jakarta namespace, Jakarta EE 11).

**Target version**: `4.0.0-SNAPSHOT`

---

## Table of Contents

1. [Quick Reference](#quick-reference)
2. [Why This Fork Exists](#why-this-fork-exists)
3. [Step-by-Step Migration](#step-by-step-migration)
4. [Dependency Breakpoints](#dependency-breakpoints)
5. [Compatibility Matrix](#compatibility-matrix)
6. [Maven / Gradle Configuration](#maven--gradle-configuration)
7. [FAQ](#faq)

---

## Quick Reference

| Aspect | Upstream 2.2.x | This Fork 4.0.0 |
|--------|----------------|-----------------|
| Group ID | `io.swagger.core.v3` | `io.github.vpelikh` |
| REST module | `swagger-jaxrs2` | `swagger-jakarta-rest` |
| Namespace | `javax.ws.rs.*` | `jakarta.ws.rs.*` |
| Jakarta EE target | EE 9/10 (via Eclipse Transformer) | EE 11 (direct) |
| Java minimum | 11 | 17 |
| Jackson group | `com.fasterxml.jackson` | `tools.jackson` |
| Eclipse Transformer | As-needed for `-jakarta` artifacts | Removed entirely |
|| Java package | `io.swagger.v3.jaxrs2` | `io.swagger.v3.jakartarest` |

---

## Why This Fork Exists

`io.github.vpelikh/swagger-core` is a fork of `swagger-api/swagger-core` that:

- Migrated all modules from **Java EE 8** (JAX-RS 2, javax namespace) to **Jakarta EE 11** (Jakarta REST 4, jakarta namespace)
- **Removed** the Eclipse Transformer-based `-jakarta` suffix artifacts — source code uses jakarta namespace directly
- **Renamed** `swagger-jaxrs2` → `swagger-jakarta-rest` (and related modules)
- **Updated** all Jakarta EE dependencies to EE 11-compatible versions
- **Published** under different Maven coordinates: `io.github.vpelikh`

---

## Step-by-Step Migration

### 1. Update Maven / Gradle Coordinates

**Group ID** must change everywhere you reference swagger-core artifacts:

```xml
<!-- Old (upstream) -->
<groupId>io.swagger.core.v3</groupId>

<!-- New (this fork) -->
<groupId>io.github.vpelikh</groupId>
```

**Module artifact IDs** must be updated:

| Upstream Artifact | Fork Artifact |
|------------------|---------------|
| `swagger-jaxrs2` | `swagger-jakarta-rest` |
| `swagger-jaxrs2-servlet-initializer` | `swagger-jakarta-rest-servlet-initializer` |
| `swagger-jaxrs2-servlet-initializer-v2` | `swagger-jakarta-rest-servlet-initializer` |
| `swagger-annotations` | `swagger-annotations` (same) |
| `swagger-models` | `swagger-models` (same) |
| `swagger-core` | `swagger-core` (same) |
| `swagger-integration` | `swagger-integration` (same) |
| `swagger-bom` | `swagger-bom` (same) |

> **Note**: Core artifacts (`swagger-annotations`, `swagger-models`, `swagger-core`, `swagger-integration`) retained their names but now use the jakarta namespace in their source code. The BOM manages all versions.

### 2. Replace All `javax.*` Imports with `jakarta.*`

**JAX-RS / Jakarta REST:**
```java
// Before (upstream 2.2.x)
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

// After (this fork 4.0.0)
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
```

**Servlet:**
```java
// Before
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

// After
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
```

**CDI / Inject (if using DI):**
```java
// Before
import javax.inject.Inject;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

// After
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
```

**XML Bind:**
```java
// Before
import javax.xml.bind.annotation.XmlRootElement;

// After
import jakarta.xml.bind.annotation.XmlRootElement;
```

**Validation:**
```java
// Before
import javax.validation.constraints.NotNull;

// After
import jakarta.validation.constraints.NotNull;
```

**Annotation:**
```java
// Before
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

// After
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
```

### 3. Update Jackson Dependencies

Jackson migrated from the `com.fasterxml.jackson` group (2.x) to the `tools.jackson` group (3.x). Jakarta REST providers also changed:

```xml
<!-- Old (upstream 2.2.x) -->
<dependency>
    <groupId>com.fasterxml.jackson.jaxrs</groupId>
    <artifactId>jackson-jaxrs-json-provider</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.module</groupId>
    <artifactId>jackson-module-jaxb-annotations</artifactId>
</dependency>

<!-- New (this fork 4.0.0) -->
<dependency>
    <groupId>tools.jackson.jakarta.rs</groupId>
    <artifactId>jackson-jakarta-rs-json-provider</artifactId>
    <version>3.1.4</version>
</dependency>
<dependency>
    <groupId>tools.jackson.module</groupId>
    <artifactId>jackson-module-jakarta-xmlbind-annotations</artifactId>
    <version>3.1.4</version>
</dependency>
```

### 4. Update Application Server / Runtime

You must deploy on a **Jakarta EE 11-compatible** (or at minimum Jakarta EE 10-compatible) runtime:

| Runtime | Compatibility | Notes |
|---------|-------------|-------|
| Jetty 12 (EE11) | ✅ Full | Use `jetty-ee11` profile |
| Tomcat 11 | ✅ Full | Servlet 6.0, Jakarta EE 11 |
| Tomcat 10 | ✅ Jakarta REST | Servlet 5.0, Jakarta EE 9/10 |
| WildFly 32+ | ✅ Full | Jakarta EE 11 |
| GlassFish 7+ | ✅ Full | Jakarta EE 10/11 |
| Spring Boot 4.x | ✅ | With Jakarta support enabled |
| Java EE 8 containers | ❌ | Must migrate to Jakarta runtime |

### 5. Remove Eclipse Transformer Artifacts

If you were using `-jakarta` suffix artifacts from the upstream Eclipse Transformer:

| Eclipse Transformer Artifact | Replace With |
|-----------------------------|-------------|
| `swagger-annotations-jakarta` | `swagger-annotations` |
| `swagger-core-jakarta` | `swagger-core` |
| `swagger-models-jakarta` | `swagger-models` |
| `swagger-integration-jakarta` | `swagger-integration` |
| `swagger-jaxrs2-jakarta` | `swagger-jakarta-rest` |

The base artifacts now use the jakarta namespace directly — no transformer needed.

### 6. Update Build Properties

| Property | Upstream | This Fork |
|----------|----------|-----------|
| `${jersey2-version}` | Jersey 2.x (javax) | `${jersey-version}` → 4.0.2 |

---
### 7. Update Java Package Imports

The internal Java package for Jakarta REST modules has been renamed from `io.swagger.v3.jaxrs2` to `io.swagger.v3.jakartarest` to match the module artifact rename (`swagger-jaxrs2` → `swagger-jakarta-rest`).

**Import changes required:**
```java
// Before (upstream 2.2.x)
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.OpenApiServlet;
import io.swagger.v3.jaxrs2.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.SwaggerSerializers;
import io.swagger.v3.jaxrs2.SwaggerLoader;
// ... and other classes from io.swagger.v3.jaxrs2 package

// After (this fork 4.0.0)
import io.swagger.v3.jakartarest.Reader;
import io.swagger.v3.jakartarest.OpenApiServlet;
import io.swagger.v3.jakartarest.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jakartarest.SwaggerSerializers;
import io.swagger.v3.jakartarest.SwaggerLoader;
// ... and other classes from io.swagger.v3.jakartarest package
```

This affects all classes in the Jakarta REST module packages. Update all import statements in your code and tests to use the new `io.swagger.v3.jakartarest` package.


## Dependency Breakpoints

### 4.0.0 — Initial fork release (Jakarta EE 11, Breaking)

- **Group ID changed**: `io.swagger.core.v3` → `io.github.vpelikh`
- **Java package rename**: `io.swagger.v3.jaxrs2` → `io.swagger.v3.jakartarest` (internal package for Jakarta REST modules)

- **Jakarta namespace**: all `javax.ws.rs.*` → `jakarta.ws.rs.*`
- **Jakarta EE versions**: upgraded all Jakarta dependencies to EE 11
- **Module renames**: `swagger-jaxrs2` → `swagger-jakarta-rest`
- **Jackson provider**: `jackson-jaxrs` → `jackson-jakarta-rs` (migrated to `tools.jackson` group)
- **Eclipse Transformer**: removed entirely
- **Java requirement**: Java 17 minimum (up from 11)
- **Maven property**: `${jersey2-version}` → `${jersey-version}`

### Upstream 2.2.x Reference

Upstream `swagger-api/swagger-core` 2.2.x tracks the upstream release line. This fork branches from that line. Refer to the [upstream changelog](https://github.com/swagger-api/swagger-core/releases) for 2.2.x details.

---

## Compatibility Matrix

### Java

| Version | Java Minimum |
|---------|--------------|
| 4.0.x | 17 |
| 2.2.x (upstream) | 11 |

### Application Servers / Runtimes

| Runtime | Compatible with 4.0.x | Notes |
|---------|----------------------|-------|
| Jakarta EE 11 containers | ✅ Full | WildFly 32+, Payara 7+, TomEE 10+ |
| Jakarta EE 10 containers | ✅ Full | Jetty 12, Tomcat 11, GlassFish 7 |
| Spring Boot 4.x | ✅ | With Jakarta support |
| Servlet 6.0 containers | ✅ | Jetty 12, Tomcat 11 |
| Servlet 5.0 containers | ✅ | Jetty 11, Tomcat 10 |
| javax / JAX-RS 2 containers | ❌ | Must migrate to Jakarta namespaces |

---

## Maven / Gradle Configuration

### Maven (Bill of Materials)

```xml
<properties>
  <swagger-openapiv3.version>4.0.0</swagger-openapiv3.version>
</properties>

<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>io.github.vpelikh</groupId>
      <artifactId>swagger-bom</artifactId>
      <version>${swagger-openapiv3.version}</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<dependencies>
  <!-- Jakarta REST 4 extension -->
  <dependency>
    <groupId>io.github.vpelikh</groupId>
    <artifactId>swagger-jakarta-rest</artifactId>
  </dependency>
  <!-- Core modules (versions managed by BOM) -->
  <dependency>
    <groupId>io.github.vpelikh</groupId>
    <artifactId>swagger-core</artifactId>
  </dependency>
  <dependency>
    <groupId>io.github.vpelikh</groupId>
    <artifactId>swagger-annotations</artifactId>
  </dependency>
  <dependency>
    <groupId>io.github.vpelikh</groupId>
    <artifactId>swagger-models</artifactId>
  </dependency>
  <dependency>
    <groupId>io.github.vpelikh</groupId>
    <artifactId>swagger-integration</artifactId>
  </dependency>
</dependencies>
```

### Gradle Kotlin DSL

```kotlin
val swaggerOpenapiv3Version by extra("4.0.0")

dependencies {
    implementation(platform("io.github.vpelikh:swagger-bom:$swaggerOpenapiv3Version"))

    implementation("io.github.vpelikh:swagger-jakarta-rest")
    implementation("io.github.vpelikh:swagger-core")
    implementation("io.github.vpelikh:swagger-annotations")
    implementation("io.github.vpelikh:swagger-models")
    implementation("io.github.vpelikh:swagger-integration")
}
```

---

## FAQ

### How do I check which version of Swagger Core I'm using?

For Maven:
```bash
mvn dependency:tree | grep swagger
```

For Gradle:
```bash
./gradlew dependencies | grep swagger
```

### I get `ClassNotFoundException` for `javax.ws.rs`

You are using code compiled against the javax namespace with Jakarta-only dependencies. Migrate all imports from `javax.ws.rs.*` to `jakarta.ws.rs.*`. See the [step-by-step migration](#2-replace-all-javax-imports-with-jakarta) above.

### Can I use javax and jakarta artifacts together?

No. You must migrate all swagger-core dependencies to the same namespace (jakarta). Mixing javax and jakarta swagger artifacts will produce class conflicts.

### Do I need the `-jakarta` suffix artifacts anymore?

No. The Eclipse Transformer approach has been removed. Use the base artifacts directly — they now use the jakarta namespace in source code.

### I was using `swagger-annotations-jakarta`. What do I use now?

Replace with the base `swagger-annotations` artifact (no suffix). It now uses the jakarta namespace directly.

### What Java version is required?

Java 17 or higher.

### I'm on upstream 2.2.x and not ready to migrate. Can I stay?

Yes. Upstream `swagger-api/swagger-core` 2.2.x is still maintained. This fork is for projects targeting Jakarta EE 11.

### How do I report a migration issue?

Open an issue at [github.com/vpelikh/swagger-core/issues](https://github.com/vpelikh/swagger-core/issues) with:
- Your Swagger Core version
- Your application server / runtime version
- The error message and stack trace
- A minimal reproduction if possible
