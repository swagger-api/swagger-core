# Changelog

## 2.2.47-SNAPSHOT

### New Features

- **`swagger-bom`**: Added Maven Bill of Materials (BOM) module (`io.swagger.core.v3:swagger-bom`).
  The BOM manages **both** the `javax` artifact family and the Jakarta (`-jakarta` suffix) artifact family
  in a single import. Consumers can import the BOM once and omit explicit versions on all
  `io.swagger.core.v3` runtime dependencies.
  Covered artifacts: `swagger-annotations`, `swagger-models`, `swagger-core`, `swagger-integration`,
  `swagger-jaxrs2`, `swagger-jaxrs2-servlet-initializer`, `swagger-jaxrs2-servlet-initializer-v2`,
  `swagger-java17-support`, and their `-jakarta` counterparts.
  Build plugins are intentionally excluded from the BOM.

---

For earlier releases see the [GitHub Releases page](https://github.com/swagger-api/swagger-core/releases).
