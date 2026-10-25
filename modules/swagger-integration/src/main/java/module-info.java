/** JPMS descriptor; preserves the existing Automatic-Module-Name. */
module io.swagger.v3.oas.integration {
    requires transitive io.swagger.v3.core;
    requires io.github.classgraph;
    requires transitive org.slf4j;

    exports io.swagger.v3.oas.integration;
    exports io.swagger.v3.oas.integration.api;

    // Jackson introspects model and configuration members on the module path.
    opens io.swagger.v3.oas.integration to tools.jackson.databind;
    opens io.swagger.v3.oas.integration.api to tools.jackson.databind;

    uses io.swagger.v3.oas.integration.api.OpenAPIConfigBuilder;
}
