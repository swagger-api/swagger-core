package io.swagger.jaxrs2;

import io.swagger.jaxrs2.resources.*;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.media.Content;
import io.swagger.oas.models.responses.ApiResponse;
import io.swagger.oas.models.responses.ApiResponses;
import org.testng.annotations.Test;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.testng.Assert.*;


public class ReaderTest {

    public static final String EXAMPLE_TAG = "Example tag";
    private static final String OPERATION_SUMMARY = "Operation Summary";
    private static final String OPERATION_DESCRIPTION = "Operation Description";
    public static final String APPLICATION_JSON = "application/json";
    public static final String RESPONSE_CODE_200 = "200";
    public static final String RESPONSE_CODE_DEFAULT = "default";

    @Test(description = "scan methods")
    public void scanMethods() {
        Method[] methods = SimpleMethods.class.getMethods();
        Reader reader = new Reader(new OpenAPI());
        for (final Method method : methods) {
            if (isValidRestPath(method)) {
                Operation operation = reader.parseMethod(method);
                assertNotNull(operation);
            }
        }
    }

    @Test(description = "Get a Summary and Description")
    public void getSummaryAndDescription() {
        Method[] methods = BasicFieldsResource.class.getMethods();
        Reader reader = new Reader(new OpenAPI());
        Operation operation = reader.parseMethod(methods[0]);
        assertNotNull(operation);
        assertEquals(OPERATION_SUMMARY, operation.getSummary());
        assertEquals(OPERATION_DESCRIPTION, operation.getDescription());
    }

    @Test(description = "Deprecated Method")
    public void deprecatedMethod() {
        Method[] methods = DeprecatedFieldsResource.class.getMethods();
        Reader reader = new Reader(new OpenAPI());

        Operation deprecatedOperation = reader.parseMethod(methods[0]);
        assertNotNull(deprecatedOperation);
        assertTrue(deprecatedOperation.getDeprecated());
    }

    @Test(description = "Get tags")
    public void getTags() {
        Method[] methods = TagsResource.class.getMethods();
        Reader reader = new Reader(new OpenAPI());
        Operation operation = reader.parseMethod(methods[0]);
        assertNotNull(operation);
        assertEquals(1, operation.getTags().size());
        assertEquals(EXAMPLE_TAG, operation.getTags().get(0));
    }

    @Test(description = "Responses")
    public void responses() {
        Method[] methods = ApiExampleResource.class.getMethods();
        Reader reader = new Reader(new OpenAPI());

        Operation responseOperation = reader.parseMethod(methods[0]);
        assertNotNull(responseOperation);
        ApiResponses responses = responseOperation.getResponses();
        assertEquals(2, responses.size());

        ApiResponse apiResponse = responses.get(RESPONSE_CODE_200);
        assertNotNull(apiResponse);
        assertEquals("voila!", apiResponse.getDescription());

        Content content = apiResponse.getContent();
        assertNotNull(content);
        assertNotNull(content.get(APPLICATION_JSON));

        apiResponse = responses.get(RESPONSE_CODE_DEFAULT);
        assertNotNull(apiResponse);
        assertEquals("boo", apiResponse.getDescription());

        content = apiResponse.getContent();
        assertNotNull(content);
        assertNotNull(content.get("*/*"));
    }

    @Test(description = "Callbacks")
    public void callbacks() {
        Method[] methods = SimpleCallbackResource.class.getMethods();
        Reader reader = new Reader(new OpenAPI());

    }

    private Boolean isValidRestPath(Method method) {
        for (Class<? extends Annotation> item : Arrays.asList(GET.class, PUT.class, POST.class, DELETE.class,
                OPTIONS.class, HEAD.class)) {
            if (method.getAnnotation(item) != null) {
                return true;
            }
        }
        return false;
    }
}
