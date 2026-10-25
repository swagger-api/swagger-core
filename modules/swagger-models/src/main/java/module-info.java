/** JPMS descriptor; preserves the existing Automatic-Module-Name. */
module io.swagger.v3.oas.models {
    requires transitive com.fasterxml.jackson.annotation;

    exports io.swagger.v3.oas.models;
    exports io.swagger.v3.oas.models.annotations;
    exports io.swagger.v3.oas.models.callbacks;
    exports io.swagger.v3.oas.models.examples;
    exports io.swagger.v3.oas.models.headers;
    exports io.swagger.v3.oas.models.info;
    exports io.swagger.v3.oas.models.links;
    exports io.swagger.v3.oas.models.media;
    exports io.swagger.v3.oas.models.parameters;
    exports io.swagger.v3.oas.models.responses;
    exports io.swagger.v3.oas.models.security;
    exports io.swagger.v3.oas.models.servers;
    exports io.swagger.v3.oas.models.tags;

    // Jackson introspects model and configuration members on the module path.
    opens io.swagger.v3.oas.models to tools.jackson.databind;
    opens io.swagger.v3.oas.models.annotations to tools.jackson.databind;
    opens io.swagger.v3.oas.models.callbacks to tools.jackson.databind;
    opens io.swagger.v3.oas.models.examples to tools.jackson.databind;
    opens io.swagger.v3.oas.models.headers to tools.jackson.databind;
    opens io.swagger.v3.oas.models.info to tools.jackson.databind;
    opens io.swagger.v3.oas.models.links to tools.jackson.databind;
    opens io.swagger.v3.oas.models.media to tools.jackson.databind;
    opens io.swagger.v3.oas.models.parameters to tools.jackson.databind;
    opens io.swagger.v3.oas.models.responses to tools.jackson.databind;
    opens io.swagger.v3.oas.models.security to tools.jackson.databind;
    opens io.swagger.v3.oas.models.servers to tools.jackson.databind;
    opens io.swagger.v3.oas.models.tags to tools.jackson.databind;
}
