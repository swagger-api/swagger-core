# Migrating to Swagger Core 3.0

## Overview

Swagger Core 3.0 is a major release that migrates the runtime from Jackson 2 to Jackson 3
and raises the minimum Java version to 17. Users upgrading from 2.x must update dependencies,
ensure Java 17+, and update any custom `ObjectMapperProcessor` implementations or Jackson
serializer/deserializer classes.

## Compatibility

| | Swagger Core 2.x | Swagger Core 3.0 |
|---|---|---|
| Java | 8+ | **17+** |
| OpenAPI | 3.x (OAS 3.0 and 3.1) | 3.x (OAS 3.0 and 3.1) |
| JAX-RS namespace | `javax.*` and `jakarta.*` | `jakarta.*` only |

## Dependency changes

### Maven coordinates

Jackson core/databind/dataformat artifacts moved from `com.fasterxml.jackson` to `tools.jackson`.
Jackson annotations remain on the original `com.fasterxml.jackson.core` groupId.

| Artifact | 2.x groupId | 3.0 groupId | 3.0 version |
|---|---|---|---|
| `jackson-core` | `com.fasterxml.jackson.core` | `tools.jackson.core` | 3.2.2 |
| `jackson-databind` | `com.fasterxml.jackson.core` | `tools.jackson.core` | 3.2.2 |
| `jackson-dataformat-yaml` | `com.fasterxml.jackson.dataformat` | `tools.jackson.dataformat` | 3.2.2 |
| `jackson-annotations` | `com.fasterxml.jackson.core` | `com.fasterxml.jackson.core` **(unchanged)** | 2.22 |

### SnakeYAML

`org.yaml:snakeyaml` is removed. YAML support is now provided by
`org.snakeyaml:snakeyaml-engine`, pulled in transitively through
`tools.jackson.dataformat:jackson-dataformat-yaml`. No explicit dependency is needed.

## Breaking changes

### Java 17 required

**Impact:** High

Swagger Core 3.0 requires Java 17 or higher. Java 8 is no longer supported.

**Migration:** Update your Java runtime and build tooling to Java 17.

```xml
<!-- Maven compiler plugin -->
<maven.compiler.release>17</maven.compiler.release>
```

---

### Jackson 3 — new groupId

**Impact:** High

Jackson core, databind, and dataformat artifacts moved from `com.fasterxml.jackson`
to the `tools.jackson` groupId.

**2.x (Maven):**

```xml
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.17.0</version>
</dependency>
```

**3.0 (Maven):**

```xml
<dependency>
  <groupId>tools.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>3.2.2</version>
</dependency>
```

**2.x (Gradle):**

```kotlin
implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
```

**3.0 (Gradle):**

```kotlin
implementation("tools.jackson.core:jackson-databind:3.2.2")
```

**Migration:** Update all `com.fasterxml.jackson.core`, `com.fasterxml.jackson.databind`,
and `com.fasterxml.jackson.dataformat` dependencies to `tools.jackson.*`.
Keep `com.fasterxml.jackson.core:jackson-annotations` unchanged — it stays on the original
groupId at version 2.22.

---

### Module renames

**Impact:** High

Two modules were renamed in 3.0:

| 2.x artifact ID | 3.0 artifact ID |
|---|---|
| `swagger-jaxrs2` | `swagger-rest` |
| `swagger-jaxrs2-servlet-initializer-v2` | `swagger-rest-servlet-initializer` |

**2.x (Maven):**

```xml
<dependency>
  <groupId>io.swagger.core.v3</groupId>
  <artifactId>swagger-jaxrs2</artifactId>
  <version>2.2.55</version>
</dependency>
<dependency>
  <groupId>io.swagger.core.v3</groupId>
  <artifactId>swagger-jaxrs2-servlet-initializer-v2</artifactId>
  <version>2.2.55</version>
</dependency>
```

**3.0 (Maven):**

```xml
<dependency>
  <groupId>io.swagger.core.v3</groupId>
  <artifactId>swagger-rest</artifactId>
  <version>3.0.0</version>
</dependency>
<dependency>
  <groupId>io.swagger.core.v3</groupId>
  <artifactId>swagger-rest-servlet-initializer</artifactId>
  <version>3.0.0</version>
</dependency>
```

**2.x (Gradle):**

```kotlin
implementation("io.swagger.core.v3:swagger-jaxrs2:2.2.55")
implementation("io.swagger.core.v3:swagger-jaxrs2-servlet-initializer-v2:2.2.55")
```

**3.0 (Gradle):**

```kotlin
implementation("io.swagger.core.v3:swagger-rest:3.0.0")
implementation("io.swagger.core.v3:swagger-rest-servlet-initializer:3.0.0")
```

**Migration:** Update both artifact IDs in your build file.

---

### swagger-java17-support module removed

**Impact:** None (test-only module, no user action required)

`swagger-java17-support` was a test-only module in 2.x. It contained no production code and
was never a user dependency. Remove it from your build if present.

---

### Jakarta REST and Servlet API versions

**Impact:** High

Swagger Core 3.0 requires the Jakarta EE 10 namespace (`jakarta.*`). The legacy `javax.*` namespace
is not supported. This also means the minimum required API versions change:

| API | 2.x compatible | 3.0 required |
|---|---|---|
| Jakarta REST (JAX-RS) | `jakarta.ws.rs-api` 2.x (`javax.ws.rs`) | `jakarta.ws.rs-api` **3.1.0** (`jakarta.ws.rs`) |
| Jakarta Servlet | `jakarta.servlet-api` 4.x (`javax.servlet`) | `jakarta.servlet-api` **6.0.0** (`jakarta.servlet`) |

**2.x (Maven):**

```xml
<dependency>
  <groupId>jakarta.ws.rs</groupId>
  <artifactId>jakarta.ws.rs-api</artifactId>
  <version>2.1.6</version>
</dependency>
<dependency>
  <groupId>jakarta.servlet</groupId>
  <artifactId>jakarta.servlet-api</artifactId>
  <version>4.0.4</version>
</dependency>
```

**3.0 (Maven):**

```xml
<dependency>
  <groupId>jakarta.ws.rs</groupId>
  <artifactId>jakarta.ws.rs-api</artifactId>
  <version>3.1.0</version>
</dependency>
<dependency>
  <groupId>jakarta.servlet</groupId>
  <artifactId>jakarta.servlet-api</artifactId>
  <version>6.0.0</version>
</dependency>
```

**Migration:** Update both API versions. Using older versions (2.x REST / 4.x Servlet) with
Swagger Core 3.0 will produce compile errors or `ClassNotFoundException` at runtime due to the
namespace change.

---

### ObjectMapperProcessor — method signatures changed

**Impact:** Medium (affects only custom `ObjectMapperProcessor` implementations)

Methods now return `ObjectMapper` instead of `void`. Jackson 3 `ObjectMapper` is immutable —
configuration must happen through its builder API. The processor receives and must return
the mapper.

**2.x:**

```java
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyProcessor implements ObjectMapperProcessor {
    @Override
    public void processJsonObjectMapper(ObjectMapper mapper) {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }
}
```

**3.0:**

```java
import tools.jackson.databind.ObjectMapper;

public class MyProcessor implements ObjectMapperProcessor {
    @Override
    public ObjectMapper processJsonObjectMapper(ObjectMapper mapper) {
        return mapper.rebuild()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .build();
    }
}
```

**Migration:**

1. Update import: `com.fasterxml.jackson.databind.ObjectMapper` → `tools.jackson.databind.ObjectMapper`
2. Change method return type from `void` to `ObjectMapper`
3. Return the (possibly rebuilt) mapper

---

### ObjectMapperFactory — unsupported TokenStreamFactory throws IllegalArgumentException

**Impact:** Low (affects only code passing a custom `TokenStreamFactory` to `ObjectMapperFactory.create()`)

`ObjectMapperFactory.create(TokenStreamFactory, boolean)` now throws `IllegalArgumentException`
when passed a `TokenStreamFactory` that is neither `JsonFactory` nor `YAMLFactory`.

**Migration:** Pass only `JsonFactory` or `YAMLFactory` instances. For JSON use
`ObjectMapperFactory.createJson()` and for YAML use `ObjectMapperFactory.createYaml()`.

---

### Custom serializers and deserializers — class and signature changes

**Impact:** Medium (affects only code extending Jackson serializer or deserializer classes directly)

Jackson 3 renames the base classes and changes method signatures. The changes are not source-compatible
with Jackson 2 — renaming the base class alone will not compile.

#### Class renames

| Jackson 2 class | Jackson 3 class |
|---|---|
| `JsonSerializer<T>` | `ValueSerializer<T>` |
| `JsonDeserializer<T>` | `ValueDeserializer<T>` |
| `BeanSerializerModifier` | `ValueSerializerModifier` |
| `BeanDeserializerModifier` | `ValueDeserializerModifier` |

#### Method signature changes

`serialize` — the provider parameter type and the thrown exception type both change:

| | Jackson 2 | Jackson 3 |
|---|---|---|
| Provider parameter | `SerializerProvider` | `SerializationContext` |
| Thrown exception | `IOException` | `JacksonException` |

`deserialize` — the thrown exception type changes; `DeserializationContext` keeps its name but moves to `tools.jackson.databind`:

| | Jackson 2 | Jackson 3 |
|---|---|---|
| Thrown exception | `IOException` | `JacksonException` |

**2.x:**

```java
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;

public class MySerializer extends JsonSerializer<MyType> {
    @Override
    public void serialize(MyType value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        // ...
    }
}
```

**3.0:**

```java
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.SerializationContext;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JacksonException;

public class MySerializer extends ValueSerializer<MyType> {
    @Override
    public void serialize(MyType value, JsonGenerator gen, SerializationContext provider)
            throws JacksonException {
        // ...
    }
}
```

**Migration:**

1. Rename the base class (`JsonSerializer` → `ValueSerializer`, etc.) and update imports to `tools.jackson.*`
2. Replace `SerializerProvider` with `SerializationContext` in `serialize` method signatures
3. Replace `IOException` with `JacksonException` in serializer and deserializer method signatures
4. Update `DeserializationContext` imports from `com.fasterxml.jackson.databind` to `tools.jackson.databind`

---

### Mapper defaults — custom ObjectMapper instances

**Impact:** Low (affects only `ObjectMapper` instances created outside of `ObjectMapperFactory`)

Swagger Core 3.0 explicitly sets the following defaults in `ObjectMapperFactory` to preserve
Jackson 2 behavioral compatibility:

| Setting | Value | Reason |
|---|---|---|
| `WRITE_DATES_AS_TIMESTAMPS` | `false` | Dates serialized as ISO-8601 strings |
| `WRITE_DURATIONS_AS_TIMESTAMPS` | `true` | Jackson 2 compat |
| `WRITE_UTC_AS_OFFSET` | `true` | Jackson 2 compat |
| `FAIL_ON_EMPTY_BEANS` | `false` | Jackson 2 default |
| `FAIL_ON_UNKNOWN_PROPERTIES` | `false` | Jackson 2 default |
| `FAIL_ON_NULL_FOR_PRIMITIVES` | `false` | Jackson 2 default |
| `STRIP_TRAILING_BIGDECIMAL_ZEROES` | `true` | Preserve numeric output |
| `SORT_PROPERTIES_ALPHABETICALLY` | `false` | Preserve field order |
| `WRITE_BIGDECIMAL_AS_PLAIN` | `true` | No scientific notation |
| `FAIL_ON_ORDER_MAP_BY_INCOMPARABLE_KEY` | `true` | Fail fast on bad Map keys |
| Property inclusion | `NON_NULL` | Omit null fields |

No behavioral changes to JSON or YAML output are expected when using the built-in mappers.

If you create `ObjectMapper` instances outside of `ObjectMapperFactory`, you must account for
Jackson 3's changed defaults. Refer to the
[Jackson 3 migration notes](https://github.com/FasterXML/jackson/wiki/Jackson-Release-3.0)
for the full list of default changes.

---

### Mapper customization — cached mappers become stale

**Impact:** Medium (affects only code that caches mappers at application startup)

Jackson 3 ObjectMappers are immutable. Swagger Core 3.0 provides an API to customize mappers
through `ObjectMapperFactory` with automatic rebuild when customizers are registered.
However, **components that cache mappers at startup will not see customizers added later**.

**Problem scenario:**

```java
// BAD - cached at initialization
class MyComponent {
    private final ObjectMapper mapper = Json.mapper();  // captured at init
    
    void process() {
        mapper.readValue(...);  // won't see customizers added later!
    }
}

// Later in application flow
ObjectMapperFactory.addCustomizer((builder, target) -> {
    builder.addModule(new KotlinModule.Builder().build());
});
// MyComponent.mapper still doesn't have KotlinModule
```

**Solution:**

Obtain mappers at use time instead of caching them:

```java
// GOOD - fresh mapper every time
class MyComponent {
    void process() {
        ObjectMapper mapper = Json.mapper();  // refreshed, sees all customizers
        mapper.readValue(...);
    }
}
```

**Affected APIs:** `Json.mapper()`, `Yaml.mapper()`, `Json31.mapper()`, `Yaml31.mapper()`, `Json31.converterMapper()`

**Best practice:** Register all customizers at application startup, before any mapper is used.
This avoids rebuilds and ensures all components see the same mapper configuration.

**Migration:** If your code caches mappers:
1. Change from `final ObjectMapper mapper = Json.mapper()` to obtaining mapper at use time
2. Or implement a refresh mechanism that re-obtains the mapper when customizers change
3. Register customizers early (during application initialization)

**Components in swagger-rest that need attention:**
- `DefaultParameterExtension` - caches mapper at class init
- `OpenAPI31SpecFilter` - caches converter mapper through `OpenAPISchema2JsonSchema`

---

## Migration steps

### 1. Update Java version

Ensure your environment and build are using Java 17 or higher.

### 2. Update swagger-core dependencies

Update all `io.swagger.core.v3` artifacts to version 3.0.0.

### 3. Update Jackson dependencies

Replace `com.fasterxml.jackson.core`, `com.fasterxml.jackson.databind`, and
`com.fasterxml.jackson.dataformat` dependencies with their `tools.jackson.*` equivalents.
Keep `com.fasterxml.jackson.core:jackson-annotations` at version 2.22.
Remove any explicit `org.yaml:snakeyaml` dependency.

### 4. Remove swagger-java17-support dependency

If your project depended on `swagger-java17-support`, remove it. Record support is now built in.

### 5. Update Jakarta REST and Servlet API versions

Update `jakarta.ws.rs-api` to 3.1.0 and `jakarta.servlet-api` to 6.0.0.
See [Jakarta REST and Servlet API versions](#jakarta-rest-and-servlet-api-versions).

### 6. Update ObjectMapperProcessor implementations

If you implement `ObjectMapperProcessor`, update the import and change method return types
from `void` to `ObjectMapper` (see [Breaking changes](#objectmapperprocessor--method-signatures-changed)).

### 7. Rename Jackson serializer and deserializer classes

If you extend `JsonSerializer`, `JsonDeserializer`, `BeanSerializerModifier`, or
`BeanDeserializerModifier`, rename them to their Jackson 3 equivalents and update method signatures
(see [Breaking changes](#custom-serializers-and-deserializers--class-and-signature-changes)).

### 8. Update Jackson imports across your codebase

Three Jackson packages move to the `tools.jackson` groupId. Apply all three replacements:

| Replace | With |
|---|---|
| `com.fasterxml.jackson.core` | `tools.jackson.core` |
| `com.fasterxml.jackson.databind` | `tools.jackson.databind` |
| `com.fasterxml.jackson.dataformat` | `tools.jackson.dataformat` |

Keep `com.fasterxml.jackson.annotation` imports unchanged — annotations stay on the original groupId.


### 9. Run your test suite

Verify that OpenAPI output matches expectations, especially if you are using custom
serializers or a custom `ObjectMapper`.

---

## Before and after examples

### Example: Maven dependency block

**2.x:**

```xml
<dependencies>
  <dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-jaxrs2</artifactId>
    <version>2.2.55</version>
  </dependency>
  <dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-jaxrs2-servlet-initializer-v2</artifactId>
    <version>2.2.55</version>
  </dependency>
  <dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
  </dependency>
</dependencies>
```

**3.0:**

```xml
<dependencies>
  <dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-rest</artifactId>
    <version>3.0.0</version>
  </dependency>
  <dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-rest-servlet-initializer</artifactId>
    <version>3.0.0</version>
  </dependency>
  <dependency>
    <groupId>tools.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>3.2.2</version>
  </dependency>
</dependencies>
```

### Example: ObjectMapperProcessor implementation

See [ObjectMapperProcessor — method signatures changed](#objectmapperprocessor--method-signatures-changed) above.

---

## Migration checklist

- [ ] Java 17+ runtime and compiler configured
- [ ] `io.swagger.core.v3` dependencies updated to 3.0.0
- [ ] `swagger-jaxrs2` dependency renamed to `swagger-rest`
- [ ] `swagger-jaxrs2-servlet-initializer-v2` dependency renamed to `swagger-rest-servlet-initializer`
- [ ] `swagger-java17-support` dependency removed (Record support is now built in)
- [ ] Jakarta REST API updated to `jakarta.ws.rs-api:3.1.0`
- [ ] Jakarta Servlet API updated to `jakarta.servlet-api:6.0.0`
- [ ] Jackson dependencies updated to `tools.jackson.*:3.2.2`
- [ ] `com.fasterxml.jackson.core:jackson-annotations` kept at 2.22 (unchanged)
- [ ] `org.yaml:snakeyaml` removed from dependencies
- [ ] `ObjectMapperProcessor` implementations updated: import and return type
- [ ] Cached mappers reviewed: no `final ObjectMapper mapper = Json.mapper()` at class init (see [mapper customization](#mapper-customization--cached-mappers-become-stale))
- [ ] Custom Jackson serializer/deserializer classes and method signatures updated (see [class and signature changes](#custom-serializers-and-deserializers--class-and-signature-changes))
- [ ] Jackson databind imports updated: `com.fasterxml.jackson.databind` → `tools.jackson.databind`
- [ ] Test suite passing
- [ ] OpenAPI output verified
