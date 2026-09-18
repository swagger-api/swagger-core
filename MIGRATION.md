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

### Custom serializers and deserializers — class renames

**Impact:** Medium (affects only code extending Jackson serializer or deserializer classes directly)

Several Jackson classes were renamed in Jackson 3:

| Jackson 2 class | Jackson 3 class |
|---|---|
| `JsonSerializer<T>` | `ValueSerializer<T>` |
| `JsonDeserializer<T>` | `ValueDeserializer<T>` |
| `BeanSerializerModifier` | `ValueSerializerModifier` |
| `BeanDeserializerModifier` | `ValueDeserializerModifier` |

**2.x:**

```java
import com.fasterxml.jackson.databind.JsonSerializer;

public class MySerializer extends JsonSerializer<MyType> { ... }
```

**3.0:**

```java
import tools.jackson.databind.ValueSerializer;

public class MySerializer extends ValueSerializer<MyType> { ... }
```

**Migration:** Rename the base class and update imports. The method signatures are otherwise unchanged.

---

### Mapper defaults — custom ObjectMapper instances

**Impact:** Low (affects only `ObjectMapper` instances created outside of `ObjectMapperFactory`)

Swagger Core 3.0 explicitly preserves Jackson 2 default behaviors inside `ObjectMapperFactory`
(for example: `WRITE_DATES_AS_TIMESTAMPS` disabled, annotation introspection configured).
No behavioral changes to JSON or YAML output are expected when using the built-in mappers.

If you create `ObjectMapper` instances outside of `ObjectMapperFactory`, you must account for
Jackson 3's changed defaults. Refer to the
[Jackson 3 migration notes](https://github.com/FasterXML/jackson/wiki/Jackson-Release-3.0)
for the full list of default changes.

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

### 4. Update ObjectMapperProcessor implementations

If you implement `ObjectMapperProcessor`, update the import and change method return types
from `void` to `ObjectMapper` (see [Breaking changes](#objectmapperprocessor--method-signatures-changed)).

### 5. Rename Jackson serializer and deserializer classes

If you extend `JsonSerializer`, `JsonDeserializer`, `BeanSerializerModifier`, or
`BeanDeserializerModifier`, rename them to their Jackson 3 equivalents
(see [Breaking changes](#custom-serializers-and-deserializers--class-renames)).

### 6. Update Jackson imports across your codebase

Bulk-replace `com.fasterxml.jackson.databind` with `tools.jackson.databind` in your source files.
Keep `com.fasterxml.jackson.annotation` imports unchanged.

### 7. Run your test suite

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
    <artifactId>swagger-jaxrs2</artifactId>
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
- [ ] Jackson dependencies updated to `tools.jackson.*:3.2.2`
- [ ] `com.fasterxml.jackson.core:jackson-annotations` kept at 2.22 (unchanged)
- [ ] `org.yaml:snakeyaml` removed from dependencies
- [ ] `ObjectMapperProcessor` implementations updated: import and return type
- [ ] Custom Jackson serializer/deserializer classes renamed (`JsonSerializer` → `ValueSerializer`, etc.)
- [ ] Jackson databind imports updated: `com.fasterxml.jackson.databind` → `tools.jackson.databind`
- [ ] Test suite passing
- [ ] OpenAPI output verified
