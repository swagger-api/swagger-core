package io.swagger.v3.rest;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * Public names consumed by applications and reflective integrations after the REST migration.
 */
public class RestNamespaceContractTest {

    @DataProvider(name = "restApiClasses")
    public Object[][] restApiClasses() {
        return new Object[][] {
                {"io.swagger.v3.rest.Reader"},
                {"io.swagger.v3.rest.ext.OpenAPIExtension"},
                {"io.swagger.v3.rest.integration.SwaggerLoader"},
                {"io.swagger.v3.rest.integration.api.WebOpenApiContext"},
                {"io.swagger.v3.rest.integration.resources.OpenApiResource"},
                {"io.swagger.v3.rest.util.ReaderUtils"},
                {"io.swagger.v3.rest.integration.RestOpenApiContextBuilder"},
                {"io.swagger.v3.rest.integration.RestOpenApiContext"},
                {"io.swagger.v3.rest.integration.RestAnnotationScanner"},
                {"io.swagger.v3.rest.integration.RestApplicationScanner"},
                {"io.swagger.v3.rest.integration.RestApplicationAndAnnotationScanner"},
                {"io.swagger.v3.rest.integration.RestApplicationAndResourcePackagesAnnotationScanner"},
                {"io.swagger.v3.rest.integration.api.RestOpenApiScanner"}
        };
    }

    @Test(dataProvider = "restApiClasses")
    public void publicClassHasRestName(String className) throws ClassNotFoundException {
        Class<?> type = Class.forName(className, false, getClass().getClassLoader());
        assertNotNull(type);
    }
}
