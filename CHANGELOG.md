# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]

### Added
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
