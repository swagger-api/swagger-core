package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.ext.AbstractOpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtensions;
import io.swagger.v3.jaxrs2.resources.SimpleResourceWithVendorAnnotation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Iterator;

public class DecoratorExtensionTest {

    private static final String RESPONSE_DESCRIPTION = "Some vendor error description";

    private static final String RESPONSE_STATUS_401 = "401";

    private static final OpenAPIExtension customExtension = new AbstractOpenAPIExtension() {

        @Override
        public void decorateOperation(final Operation operation, final Method method, final Iterator<OpenAPIExtension> chain) {
            method.getDeclaredAnnotations();
            final SimpleResourceWithVendorAnnotation.VendorFunnyAnnotation myFunyError = method.getAnnotation(SimpleResourceWithVendorAnnotation.VendorFunnyAnnotation.class);
            if (myFunyError != null) {
                /*
                 * Extend swagger model by new error response description, with additional data received from vendor
                 * based annotation. This example overwrite existing response from swagger annotation, but it is only
                 * for demo.
                 */
                final ApiResponse value = new ApiResponse();
                value.setDescription(RESPONSE_DESCRIPTION);
                operation.getResponses().put(RESPONSE_STATUS_401, value);
            }
        }
    };

    private OpenAPI getOpenAPI(final Class<?> cls) {
        return new Reader(new OpenAPI()).read(cls);
    }

    private Operation getGet(final OpenAPI swagger, final String path) {
        return swagger.getPaths().get(path).getGet();
    }

    @BeforeMethod()
    public void addCustomExtension() {
        OpenAPIExtensions.getExtensions().add(customExtension);
    }

    @AfterMethod()
    public void removeCustomExtension() {
        OpenAPIExtensions.getExtensions().remove(customExtension);
    }

    /**
     * Test for method annotated with vendor annotation which could be used for swagger documentation.
     */
    @Test(description = "scan a simple resource with custom decorator")
    public void scanSimpleResourceWithDecorator() {
        final OpenAPI openAPI = getOpenAPI(SimpleResourceWithVendorAnnotation.class);

        Assert.assertEquals(openAPI.getPaths().size(), 2);

        final Operation get = getGet(openAPI, "/{id}");
        Assert.assertNotNull(get);
        Assert.assertEquals(get.getParameters().size(), 2);

        final ApiResponse response = get.getResponses().get(RESPONSE_STATUS_401);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getDescription(), RESPONSE_DESCRIPTION);
    }

    /**
     * Test for method annotated without vendor annotation.
     */
    @Test(description = "scan a simple resource without custom decorator")
    public void scanSimpleResourceWithoutDecorator() {
        final OpenAPI openAPI = getOpenAPI(SimpleResourceWithVendorAnnotation.class);

        Assert.assertEquals(openAPI.getPaths().size(), 2);

        final Operation get = getGet(openAPI, "/{id}/value");
        Assert.assertNotNull(get);

        final ApiResponse response = get.getResponses().get(RESPONSE_STATUS_401);
        Assert.assertNull(response);
    }
}
