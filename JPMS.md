# Java modules

The six runtime libraries include native `module-info.class` descriptors on the
Java 17 baseline. Their names match the existing `Automatic-Module-Name` entries:

| Artifact | Module name |
| --- | --- |
| `swagger-annotations` | `io.swagger.v3.oas.annotations` |
| `swagger-models` | `io.swagger.v3.oas.models` |
| `swagger-core` | `io.swagger.v3.core` |
| `swagger-integration` | `io.swagger.v3.oas.integration` |
| `swagger-rest` | `io.swagger.v3.rest` |
| `swagger-rest-servlet-initializer` | `io.swagger.v3.rest.integration.servlet` |

The libraries can still be used on the classpath. The Maven and Gradle plugins
continue to run in their build tools' classloaders.

## Application descriptors

A REST application can declare:

```java
module example.app {
    requires io.swagger.v3.rest;

    opens example.app.models to tools.jackson.databind, io.swagger.v3.core;
    opens example.app.resources to io.swagger.v3.rest, tools.jackson.databind;
}
```

Use your application's actual package names. Open packages containing models
and resource classes to the libraries that inspect them reflectively. Custom
reader, scanner and configuration implementations instantiated by the integration
layer must also be accessible to `io.swagger.v3.oas.integration`.

`swagger-rest` declares the Servlet API as `requires static transitive`, so it
does not force Servlet API resolution for applications that only use the Reader.
Servlet integration needs `jakarta.servlet` resolved at runtime. Applications
using the initializer can require `io.swagger.v3.rest.integration.servlet`, which
requires the Servlet API and provides `ServletContainerInitializer`.

Named-module implementations of `ModelConverter`, `OpenAPIConfigBuilder` and
`OpenAPIExtension` should declare their providers with `provides ... with ...`.
The consuming Swagger modules declare the corresponding `uses` directives.
Existing `META-INF/services` registrations remain available to classpath users.

## Verification

Run the existing tests and the packaged module-path consumer with:

```sh
./mvnw verify -Pjpms-it
```

The opt-in `swagger-jpms-integration-test` module is not installed or deployed.
It launches a separate Java process against packaged JARs and checks explicit
descriptors, all four service contracts, REST resource discovery, and JSON/YAML
round trips for OpenAPI 3.0 and 3.1, including typed-map schema references.
Existing unit and servlet integration tests retain their classpath environment.
CI also runs the module-path consumer on Java 17, 21 and 25.

The packaged consumer can additionally be linked into a runtime image:

```sh
jlink --module-path "modules/swagger-jpms-integration-test/target/swagger-jpms-integration-test-3.0.0-SNAPSHOT.jar:modules/swagger-jpms-integration-test/target/lib" \
  --add-modules io.swagger.v3.jpms.test --output target/jpms-runtime
target/jpms-runtime/bin/java --module io.swagger.v3.jpms.test/io.swagger.v3.jpms.test.ModulePathSmoke
```

On Windows, use `;` as the module-path separator. Adjust the JAR version to the
version being built, and choose an output directory that does not already exist.
