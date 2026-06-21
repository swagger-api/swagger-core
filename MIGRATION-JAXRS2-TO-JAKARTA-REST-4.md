# Migration Guide: JAX-RS 2 (Jakarta EE 10) to Jakarta REST 4.0 (Jakarta EE 11)

This guide helps you migrate Swagger Core from Jakarta EE 10 (JAX-RS 2.1.6) to Jakarta EE 11 (Jakarta REST 4.0).

## Overview

Swagger Core has migrated from Jakarta EE 10 to Jakarta EE 11. This migration affects:

- All `javax.ws.rs.*` imports → `jakarta.ws.rs.*`
- Module renames: `swagger-jaxrs2` → `swagger-jakarta-rest`
- Updated dependencies for Jakarta EE 11 compatibility

All core modules (`swagger-annotations`, `swagger-models`, `swagger-core`, `swagger-integration`) now use Jakarta namespace directly in their source code.

**Group ID**: `io.github.vpelikh`

## Breaking Changes

1. All `javax.ws.rs.*` imports must change to `jakarta.ws.rs.*`
2. Java 17 required (already the baseline)
3. Servlet API upgrade (4.0 → 6.0 compatibility)
4. Jackson dependency changes:
   - `jackson-jaxrs-json-provider` → `jackson-jakarta-rs-json-provider`
   - `jackson-module-jaxb-annotations` → `jackson-module-jakarta-xmlbind-annotations`
5. Module rename: all `swagger-jaxrs2` → `swagger-jakarta-rest` artifacts
6. BOM changes: javax artifacts removed; only Jakarta artifacts managed
7. Jersey upgrade: 2.x → 4.0.2
8. Property rename: `${jersey2-version}` → `${jersey-version}`
9. JAXB API upgrade: 2.3.3 → 4.0.5
10. Validation API upgrade: 2.0.2 → 3.1.1
11. Annotation API: now explicitly managed (3.0.0)
12. Jetty upgrade: 9.x → 12.1.10

## Migration Steps

### Step 1: Update dependency management (BOM import)

```xml
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
```

### Step 2: Replace artifact coordinates

Update your Maven dependencies to use the new artifact names:

```xml
<dependencies>
  <!-- Base artifacts (now use jakarta namespace in source) -->
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
    <artifactId>swagger-core</artifactId>
  </dependency>
  <dependency>
    <groupId>io.github.vpelikh</groupId>
    <artifactId>swagger-integration</artifactId>
  </dependency>
  <!-- Jakarta REST 4.0 artifacts -->
  <dependency>
    <groupId>io.github.vpelikh</groupId>
    <artifactId>swagger-jakarta-rest</artifactId>
  </dependency>
  <dependency>
    <groupId>io.github.vpelikh</groupId>
    <artifactId>swagger-jakarta-rest-servlet-initializer</artifactId>
  </dependency>
  <dependency>
    <groupId>io.github.vpelikh</groupId>
    <artifactId>swagger-jakarta-rest-servlet-initializer-v2</artifactId>
  </dependency>
</dependencies>
```

**Gradle Kotlin DSL:**

```kotlin
dependencies {
    implementation(platform("io.github.vpelikh:swagger-bom:${swaggerOpenapiv3Version}"))

    implementation("io.github.vpelikh:swagger-annotations")
    implementation("io.github.vpelikh:swagger-models")
    implementation("io.github.vpelikh:swagger-core")
    implementation("io.github.vpelikh:swagger-integration")
    implementation("io.github.vpelikh:swagger-jakarta-rest")
    implementation("io.github.vpelikh:swagger-jakarta-rest-servlet-initializer")
    implementation("io.github.vpelikh:swagger-jakarta-rest-servlet-initializer-v2")
}
```

### Step 3: Replace imports in your code

Replace all Jakarta EE 10 imports with Jakarta EE 11:

```java
// Before (JAX-RS 2)
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

// After (Jakarta REST 4)
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
```

Also replace servlet imports:

```java
// Before
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

// After
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
```

### Step 4: Update Jackson dependencies

```xml
<dependencies>
  <!-- Old (JAX-RS 2) -->
  <dependency>
    <groupId>com.fasterxml.jackson.jaxrs</groupId>
    <artifactId>jackson-jaxrs-json-provider</artifactId>
    <version>2.17.0</version>
  </dependency>
  <dependency>
    <groupId>com.fasterxml.jackson.module</groupId>
    <artifactId>jackson-module-jaxb-annotations</artifactId>
    <version>2.17.0</version>
  </dependency>

  <!-- New (Jakarta REST 4) -->
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
</dependencies>
```

**Gradle Kotlin DSL:**

```kotlin
dependencies {
    implementation("tools.jackson.jakarta.rs:jackson-jakarta-rs-json-provider:3.1.4")
    implementation("tools.jackson.module:jackson-module-jakarta-xmlbind-annotations:3.1.4")
}
```

### Step 5: Upgrade other Jakarta EE dependencies

Update your dependency management for Jakarta EE 11:

| Dependency | Version |
|------------|---------|
| Jakarta REST API | 4.0.0 |
| Servlet API | 6.1.0 |
| JAXB API | 4.0.5 |
| Validation API | 3.1.1 |
| Annotation API | 3.0.0 |
| Jersey | 4.0.2 |
|| Jetty | 12.1.10 |

### Step 6: Build and test

```bash
mvn clean verify
```

## New Dependencies

### Old vs New Maven Coordinates

| Old (javax) | New (Jakarta) |
|-------------|---------------|
| `swagger-jaxrs2` | `swagger-jakarta-rest` |
| `swagger-jaxrs2-servlet-initializer` | `swagger-jakarta-rest-servlet-initializer` |
| `swagger-jaxrs2-servlet-initializer-v2` | `swagger-jakarta-rest-servlet-initializer-v2` |

### Full Module Mapping

| Old Artifact | New Artifact | Notes |
|-------------|--------------|-------|
| `swagger-annotations` (javax) | `swagger-annotations` | Now uses jakarta namespace in source |
| `swagger-models` (javax) | `swagger-models` | Now uses jakarta namespace in source |
| `swagger-core` (javax) | `swagger-core` | Now uses jakarta namespace in source |
| `swagger-integration` (javax) | `swagger-integration` | Now uses jakarta namespace in source |
| `swagger-jaxrs2` | `swagger-jakarta-rest` | Renamed module |
| `swagger-jaxrs2-servlet-initializer` | `swagger-jakarta-rest-servlet-initializer` | Renamed module |
| `swagger-jaxrs2-servlet-initializer-v2` | `swagger-jakarta-rest-servlet-initializer-v2` | Renamed module |
| `swagger-*-jakarta` (Eclipse Transformer) | (removed) | Direct migration replaces transformer |

## FAQ

### I get `ClassNotFoundException` for `javax.ws.rs`

You need to update all imports from `javax.ws.rs.*` to `jakarta.ws.rs.*` in your code and dependencies.

### My application still uses javax namespace

You cannot mix javax and jakarta namespaces. You must migrate all dependencies and code at once.

### I was using `swagger-annotations-jakarta` from the Eclipse Transformer project

Use the base `swagger-annotations` artifact directly. It now uses the jakarta namespace in source, so the `-jakarta` suffix is no longer needed.

### Do I need the `-jakarta` suffix artifacts anymore?

No. The Eclipse Transformer approach has been completely removed. Use the base artifacts directly.

### What Java version do I need?

Java 17 or higher is required.

### Do I need to update CDI imports?

Yes, CDI imports have been fully migrated to `jakarta.*` namespace:
- `javax.inject.Inject` → `jakarta.inject.Inject`
- `javax.enterprise.context.ApplicationScoped` → `jakarta.enterprise.context.ApplicationScoped`
- `javax.enterprise.inject.Produces` → `jakarta.enterprise.inject.Produces`
- `javax.enterprise.inject.spi.Extension` → `jakarta.enterprise.inject.spi.Extension`

The project now uses **Weld 6.0.4.Final** (Jakarta CDI 4.x). No javax CDI imports remain.

## Maven/Gradle Configuration Examples

### Maven

```xml
<properties>
  <swagger-openapiv3.version>3.0.1</swagger-openapiv3.version>
  <jakarta.ws-version>4.0.0</jakarta.ws-version>
  <servlet-api-version>6.1.0</servlet-api-version>
  <jakarta.xml.bind-api-version>4.0.5</jakarta.xml.bind-api-version>
  <validation-api-version>3.1.1</validation-api-version>
  <jakarta.annotation-api-version>3.0.0</jakarta.annotation-api-version>
  <jersey-version>4.0.2</jersey-version>
  <jetty-version>12.1.10</jetty-version>
  <jackson-version>3.1.4</jackson-version>
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
  <dependency>
    <groupId>io.github.vpelikh</groupId>
    <artifactId>swagger-jakarta-rest</artifactId>
  </dependency>

  <!-- Jakarta REST API -->
  <dependency>
    <groupId>jakarta.ws.rs</groupId>
    <artifactId>jakarta.ws.rs-api</artifactId>
    <version>${jakarta.ws-version}</version>
  </dependency>

  <!-- Servlet API -->
  <dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>${servlet-api-version}</version>
  </dependency>

  <!-- Jackson (Jakarta) -->
  <dependency>
    <groupId>tools.jackson.jakarta.rs</groupId>
    <artifactId>jackson-jakarta-rs-json-provider</artifactId>
    <version>${jackson-version}</version>
  </dependency>
</dependencies>
```

### Gradle Kotlin DSL

```kotlin
val swaggerOpenapiv3Version by extra("3.0.1")
val jacksonVersion by extra("3.1.4")

dependencies {
    implementation(platform("io.github.vpelikh:swagger-bom:$swaggerOpenapiv3Version"))

    implementation("io.github.vpelikh:swagger-jakarta-rest")

    implementation("jakarta.ws.rs:jakarta.ws.rs-api:4.0.0")
    implementation("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("tools.jackson.jakarta.rs:jackson-jakarta-rs-json-provider:$jacksonVersion")
}
```

## Summary

The migration from JAX-RS 2 to Jakarta REST 4.0 requires:

1. Update all imports from `javax.*` to `jakarta.*`
2. Use the new artifact names (`swagger-jakarta-rest` instead of `swagger-jaxrs2`)
3. Update Jackson to Jakarta variants
4. Upgrade to Jakarta EE 11 compatible versions of all dependencies

The base Swagger artifacts now use jakarta namespace directly, so you no longer need the `-jakarta` suffixed artifacts from the Eclipse Transformer project.
