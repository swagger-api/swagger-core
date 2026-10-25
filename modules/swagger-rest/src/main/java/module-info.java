/** JPMS descriptor; preserves the existing Automatic-Module-Name. */
module io.swagger.v3.rest {
    requires transitive io.swagger.v3.oas.integration;
    requires transitive jakarta.ws.rs;
    requires static transitive jakarta.servlet;
    requires io.github.classgraph;
    requires transitive org.slf4j;

    exports io.swagger.v3.rest;
    exports io.swagger.v3.rest.ext;
    exports io.swagger.v3.rest.integration;
    exports io.swagger.v3.rest.integration.api;
    exports io.swagger.v3.rest.integration.resources;
    exports io.swagger.v3.rest.util;

    uses io.swagger.v3.rest.ext.OpenAPIExtension;
}
