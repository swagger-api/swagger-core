package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.ext.AbstractSwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtensions;
import io.swagger.models.Operation;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.resources.SimpleResourceWithVendorAnnotation;
import io.swagger.resources.SimpleResourceWithVendorAnnotation.VendorFunnyAnnotation;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Scanner example for custom operation decorator extension.
 */
public class SimpleScannerWithDecoratorExtensionTest {

    private static final String RESPONSE_DESCRIPTION = "Some vendor error description";

    private static final String RESPONSE_STATUS_401 = "401";

    private static final SwaggerExtension customExtension = new AbstractSwaggerExtension() {

        @Override
        public void decorateOperation(final Operation operation, final Method method, final Iterator<SwaggerExtension> chain) {
            method.getDeclaredAnnotations();
            final VendorFunnyAnnotation myFunyError = method.getAnnotation(SimpleResourceWithVendorAnnotation.VendorFunnyAnnotation.class);
            if (myFunyError != null) {
                /*
                 * Extend swagger model by new error response description, with additional data received from vendor
                 * based annotation. This example overwrite existing response from swagger annotation, but it is only
                 * for demo.
                 */
                final Response value = new Response();
                value.setDescription(RESPONSE_DESCRIPTION);
                operation.getResponses().put(RESPONSE_STATUS_401, value);
            }
        }
    };

    private Swagger getSwagger(final Class<?> cls) {
        return new Reader(new Swagger()).read(cls);
    }

    private Operation getGet(final Swagger swagger, final String path) {
        return swagger.getPaths().get(path).getGet();
    }

    @BeforeMethod()
    public void addCustomExtension() {
        SwaggerExtensions.getExtensions().add(customExtension);
    }

    @AfterMethod()
    public void removeCustomExtension() {
        SwaggerExtensions.getExtensions().remove(customExtension);
    }

    /**
     * Test for method annotated with vendor annotation which could be used for swagger documentation.
     */
    @Test(description = "scan a simple resource with custom decorator")
    public void scanSimpleResourceWithDecorator() {
        final Swagger swagger = getSwagger(SimpleResourceWithVendorAnnotation.class);

        assertEquals(swagger.getPaths().size(), 2);

        final Operation get = getGet(swagger, "/{id}");
        assertNotNull(get);
        assertEquals(get.getParameters().size(), 2);

        final Response response = get.getResponses().get(RESPONSE_STATUS_401);
        assertNotNull(response);
        assertEquals(response.getDescription(), RESPONSE_DESCRIPTION);
    }

    /**
     * Test for method annotated without vendor annotation.
     */
    @Test(description = "scan a simple resource without custom decorator")
    public void scanSimpleResourceWithoutDecorator() {
        final Swagger swagger = getSwagger(SimpleResourceWithVendorAnnotation.class);

        assertEquals(swagger.getPaths().size(), 2);

        final Operation get = getGet(swagger, "/{id}/value");
        assertNotNull(get);
        assertEquals(get.getParameters().size(), 0);

        final Response response = get.getResponses().get(RESPONSE_STATUS_401);
        assertNull(response);
    }

}
