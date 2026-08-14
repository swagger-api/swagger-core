# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]

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
