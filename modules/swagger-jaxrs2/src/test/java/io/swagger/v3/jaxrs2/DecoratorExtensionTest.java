package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.ext.AbstractOpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtensions;
import io.swagger.v3.jaxrs2.resources.SimpleResourceWithVendorAnnotation;
import io.swagger.v3.jaxrs2.resources.generics.inherited.ApiGenericOperationAnnotation;
import io.swagger.v3.jaxrs2.resources.generics.inherited.ConcreteResource;
import io.swagger.v3.jaxrs2.resources.generics.inherited.IAbstractGenericBase;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class DecoratorExtensionTest {

    private static final String RESPONSE_DESCRIPTION = "Some vendor error description";
    private static final String OPERATION_ID_POSTFIX = "Index";

    private static final String RESPONSE_STATUS_401 = "401";
    private static final String RESPONSE_STATUS_200 = "200";

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

        @Override
        public void decorateOperation(final Class<?> clazz, final Operation operation, final Method method, final Iterator<OpenAPIExtension> chain) {
            final IAbstractGenericBase compResource = getResourceInstance(clazz, method);
        
            if (compResource != null) {
                operation.setSummary(compResource.getPluralName());
                operation.setOperationId(compResource.getSingularName() + OPERATION_ID_POSTFIX);

                operation.getResponses()
                    .addApiResponse(
                        RESPONSE_STATUS_200,
                        new ApiResponse()
                            .description(compResource.getPluralName())
                            .content(
                                new Content()
                                    .addMediaType(
                                        "",
                                        new MediaType()
                                            .schema(new ArraySchema().$ref(compResource.getDTOClazz().getSimpleName())))));
            }
        }


        private IAbstractGenericBase getResourceInstance(Class<?> clazz, Method method) {
            try {
                final ApiGenericOperationAnnotation annotation = method.getAnnotation(ApiGenericOperationAnnotation.class);

                if (annotation != null) {
                    if (IAbstractGenericBase.class.isAssignableFrom(clazz)) {
                        return IAbstractGenericBase.class.cast(clazz.getConstructor().newInstance());
                    }
                }

            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException 
                    | IllegalArgumentException | InvocationTargetException e) {
                // eat exception and return default (null)
            }

            return null;
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

        assertEquals(openAPI.getPaths().size(), 2);

        final Operation get = getGet(openAPI, "/{id}");
        assertNotNull(get);
        assertEquals(get.getParameters().size(), 2);

        final ApiResponse response = get.getResponses().get(RESPONSE_STATUS_401);
        assertNotNull(response);
        assertEquals(response.getDescription(), RESPONSE_DESCRIPTION);
    }

    /**
     * Test for method annotated without vendor annotation.
     */
    @Test(description = "scan a simple resource without custom decorator")
    public void scanSimpleResourceWithoutDecorator() {
        final OpenAPI openAPI = getOpenAPI(SimpleResourceWithVendorAnnotation.class);

        assertEquals(openAPI.getPaths().size(), 2);

        final Operation get = getGet(openAPI, "/{id}/value");
        assertNotNull(get);

        final ApiResponse response = get.getResponses().get(RESPONSE_STATUS_401);
        assertNull(response);
    }

    /**
     * Test for method annotated with vendor annotation.
     */
    @Test(description = "scan a generic resource with custom decorator")
    public void scanGenericResourceWithDecorator() {
        final ConcreteResource res = new ConcreteResource();
        final OpenAPI openAPI = getOpenAPI(ConcreteResource.class);

        assertEquals(openAPI.getPaths().size(), 1);

        final Operation get = getGet(openAPI, "/path");
        assertNotNull(get);
        assertEquals(get.getSummary(), res.getPluralName());
        assertEquals(get.getOperationId(), res.getSingularName() + OPERATION_ID_POSTFIX);

        final ApiResponse response = get.getResponses().get(RESPONSE_STATUS_200);
        assertNotNull(response);
        assertEquals(response.getDescription(), res.getPluralName());
    }
}
