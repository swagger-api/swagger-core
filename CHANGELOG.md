# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]

## [3.0.0] - 2026-10-28

First release of the 3.x line. The runtime moves from Jackson 2 to Jackson 3, the minimum
Java version is 17, and the REST API supports `jakarta.*` only. See
[MIGRATION.md](MIGRATION.md) for the upgrade guide.

### Changed (breaking)
- Java 17+ is required (previously Java 8+).
- Jackson 3.2.3. Core, databind and dataformat moved to the `tools.jackson` groupId.
  `jackson-annotations` stays on `com.fasterxml.jackson.core` (2.22).
  `Json.mapper()`, `Yaml.mapper()` and the other mapper accessors return
  `tools.jackson.databind.ObjectMapper`. `JacksonException` is unchecked and replaces
  `JsonProcessingException`. `ObjectMapperProcessor` method signatures and custom
  serializer/deserializer classes changed.
- Artifact IDs renamed. `*-jakarta`, `swagger-jaxrs2*` and
  `swagger-jaxrs2-servlet-initializer-v2*` are retired. New IDs: `swagger-annotations`,
  `swagger-models`, `swagger-core`, `swagger-integration`, `swagger-rest`,
  `swagger-rest-servlet-initializer`.
- REST API packages and types renamed: `io.swagger.v3.jaxrs2` to `io.swagger.v3.rest`,
  `Jaxrs*` to `Rest*`.
- `jakarta.*` only. `javax.*` support removed. Jakarta APIs moved from EE 10 to EE 11.
- Mapper defaults follow Jackson 3 (for example `FAIL_ON_NULL_FOR_PRIMITIVES`,
  `FAIL_ON_TRAILING_TOKENS`, `ONE_BASED_MONTHS`). Jackson 2 compatibility is opt-in via
  `ObjectMapperFactory.setJackson2Compatibility(true)`.

### Removed
- Dependencies: `commons-lang3`, `commons-io`, `snakeyaml` (replaced by
  `snakeyaml-engine`), `httpclient`, `ant`.
- Modules: `swagger-java17-support` and unused modules left over from before the Jakarta
  migration.

### Added
- API to register Jackson modules and customize the immutable Jackson 3 mappers through
  `ObjectMapperFactory`.
- Opt-in `oneOf` composition for `@JsonSubTypes` parents. Enable it with
  `-Djson-subtypes-oneof` or `ModelResolver.jsonSubTypesOneOf = true`. When enabled,
  a parent class with `@JsonSubTypes` gets a `oneOf` that lists its resolved subtypes,
  and the subtypes keep their `allOf` reference to the parent. An explicit
  `@Schema(oneOf = ...)` on the parent takes priority. The default is off, so the
  generated schemas do not change unless you enable it. Use it for abstract classes and
  interfaces. A concrete parent class is not listed in its own `oneOf`, so an instance of
  the parent itself does not match the schema. Note: the result has a cycle
  (`allOf` from subtype to parent, `oneOf` from parent to subtype). Some tools, such as
  swagger-ui and openapi-generator, may not handle this shape well.

### Fixed
- Respect an explicit `@JsonProperty` value when `ModelResolver` resolves the property
  name.
- Skip `resolveSubtypes` for sealed classes with `@Schema(oneOf)`.
- Deduplicate subtypes before composing `oneOf`.
- Keep `oneOf` when `WRAPPER_OBJECT` wrapping is applied.
- Prevent `examples` duplication in the `Schema` model.
- Make `ModelConverters.getInstance(boolean)` thread-safe and refresh mappers derived from
  replaced shared mappers.

## [2.2.54] - 2026-08-18

### Fixed
- Java 8 date/time types (`OffsetTime`, `Duration`, `LocalTime`) now map by default
  to the correct OpenAPI Formats Registry strings (`"time"`, `"duration"`, `"time-local"`)
  instead of an unusable expanded object. (#5172)
- `LocalDateTime` deserialization from an existing OpenAPI spec now correctly
  round-trips through the new `TimeSchema`/`DurationSchema`/`DateTimeLocalSchema`/
  `TimeLocalSchema` classes instead of falling back to a generic `StringSchema`.

### Added
- `PrimitiveType.enableJava8Formats()` — opt-in to map `LocalDateTime` to the
  registry-compliant `"date-time-local"` format (default remains `"date-time"`
  for backward compatibility).

### Deprecated
- `PrimitiveType.enablePartialTime()` — prefer the new default `"time-local"`
  mapping for `LocalTime`; kept for callers who specifically need the
  non-registry `"partial-time"` format.
