module io.swagger.v3.jpms.test {
    requires io.swagger.v3.rest;
    requires io.swagger.v3.rest.integration.servlet;

    opens io.swagger.v3.jpms.test to io.swagger.v3.core, io.swagger.v3.rest, tools.jackson.databind;

    uses jakarta.servlet.ServletContainerInitializer;
    provides io.swagger.v3.core.converter.ModelConverter
            with io.swagger.v3.jpms.test.ModulePathSmoke.ProbeConverter;
    provides io.swagger.v3.oas.integration.api.OpenAPIConfigBuilder
            with io.swagger.v3.jpms.test.ModulePathSmoke.ProbeConfigBuilder;
    provides io.swagger.v3.rest.ext.OpenAPIExtension
            with io.swagger.v3.jpms.test.ModulePathSmoke.ProbeExtension;
}
