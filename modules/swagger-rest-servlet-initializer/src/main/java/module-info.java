/** JPMS descriptor; preserves the existing Automatic-Module-Name. */
module io.swagger.v3.rest.integration.servlet {
    requires io.swagger.v3.rest;
    requires transitive jakarta.servlet;

    exports io.swagger.v3.rest.integration.servlet;

    provides jakarta.servlet.ServletContainerInitializer
            with io.swagger.v3.rest.integration.servlet.SwaggerServletInitializer;
}
