package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.resources.BasicFieldsResource;
import io.swagger.v3.jaxrs2.resources.CompleteFieldsResource;
import io.swagger.v3.jaxrs2.resources.DeprecatedFieldsResource;
import io.swagger.v3.jaxrs2.resources.DuplicatedOperationIdResource;
import io.swagger.v3.jaxrs2.resources.DuplicatedSecurityResource;
import io.swagger.v3.jaxrs2.resources.ExternalDocsReference;
import io.swagger.v3.jaxrs2.resources.ResponseContentWithArrayResource;
import io.swagger.v3.jaxrs2.resources.ResponsesResource;
import io.swagger.v3.jaxrs2.resources.SecurityResource;
import io.swagger.v3.jaxrs2.resources.SimpleCallbackResource;
import io.swagger.v3.jaxrs2.resources.SimpleMethods;
import io.swagger.v3.jaxrs2.resources.TagsResource;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Test for the Reader Class
 */
public class ReaderTest {
    private static final String EXAMPLE_TAG = "Example Tag";
    private static final String SECOND_TAG = "Second Tag";
    private static final String OPERATION_SUMMARY = "Operation Summary";
    private static final String OPERATION_DESCRIPTION = "Operation Description";
    private static final String CALLBACK_POST_OPERATION_DESCRIPTION = "payload data will be sent";
    private static final String CALLBACK_GET_OPERATION_DESCRIPTION = "payload data will be received";
    private static final String RESPONSE_CODE_200 = "200";
    private static final String RESPONSE_DESCRIPTION = "voila!";
    private static final String EXTERNAL_DOCS_DESCRIPTION = "External documentation description";
    private static final String EXTERNAL_DOCS_URL = "http://url.com";
    private static final String PARAMETER_IN = "path";
    private static final String PARAMETER_NAME = "subscriptionId";
    private static final String PARAMETER_DESCRIPTION = "parameter description";
    private static final String CALLBACK_SUBSCRIPTION_ID = "subscription";
    private static final String CALLBACK_URL = "http://$request.query.url";
    private static final String SECURITY_KEY = "security_key";
    private static final String SCOPE_VALUE1 = "write:pets";
    private static final String SCOPE_VALUE2 = "read:pets";
    private static final String PATH_REF = "/";
    private static final String PATH_1_REF = "/1";
    private static final String PATH_2_REF = "/path";
    private static final String SCHEMA_TYPE = "string";
    private static final String SCHEMA_FORMAT = "uuid";
    private static final String SCHEMA_DESCRIPTION = "the generated UUID";

    private static final int RESPONSES_NUMBER = 2;
    private static final int TAG_NUMBER = 2;
    private static final int SECURITY_SCHEMAS = 2;
    private static final int PARAMETER_NUMBER = 1;
    private static final int SECURITY_REQUIREMENT_NUMBER = 1;
    private static final int SCOPE_NUMBER = 2;
    private static final int PATHS_NUMBER = 1;

    @Test(description = "test a simple resource class")
    public void testSimpleReadClass() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(BasicFieldsResource.class);
        Paths paths = openAPI.getPaths();
        assertEquals(paths.size(), 6);
        PathItem pathItem = paths.get(PATH_1_REF);
        assertNotNull(pathItem);
        assertNull(pathItem.getPost());
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        assertEquals(OPERATION_SUMMARY, operation.getSummary());
        assertEquals(OPERATION_DESCRIPTION, operation.getDescription());
    }

    @Test(description = "scan methods")
    public void testCompleteReadClass() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(CompleteFieldsResource.class);
        Paths paths = openAPI.getPaths();
        assertEquals(PATHS_NUMBER, paths.size());
        PathItem pathItem = paths.get(PATH_REF);
        assertNotNull(pathItem);
        assertNull(pathItem.getPost());
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        assertEquals(OPERATION_SUMMARY, operation.getSummary());
        assertEquals(OPERATION_DESCRIPTION, operation.getDescription());

        assertEquals(TAG_NUMBER, operation.getTags().size());
        assertEquals(EXAMPLE_TAG, operation.getTags().get(0));
        assertEquals(SECOND_TAG, operation.getTags().get(1));

        ExternalDocumentation externalDocs = operation.getExternalDocs();
        assertEquals(EXTERNAL_DOCS_DESCRIPTION, externalDocs.getDescription());
        assertEquals(EXTERNAL_DOCS_URL, externalDocs.getUrl());
    }

    @Test(description = "scan methods")
    public void testScanMethods() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(SimpleMethods.class);
        Method[] methods = SimpleMethods.class.getMethods();
        for (final Method method : methods) {
            if (isValidRestPath(method)) {
                Operation operation = reader.parseMethod(method, null);
                assertNotNull(operation);
            }
        }
    }

    @Test(description = "Get a Summary and Description")
    public void testGetSummaryAndDescription() {
        Reader reader = new Reader(new OpenAPI());
        Method[] methods = BasicFieldsResource.class.getMethods();
        Operation operation = reader.parseMethod(methods[0], null);
        assertNotNull(operation);
        assertEquals(OPERATION_SUMMARY, operation.getSummary());
        assertEquals(OPERATION_DESCRIPTION, operation.getDescription());
    }

    @Test(description = "Get a Duplicated Operation Id")
    public void testResolveDuplicatedOperationId() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(DuplicatedOperationIdResource.class);

        Paths paths = openAPI.getPaths();
        assertNotNull(paths);
        Operation firstOperation = paths.get(PATH_REF).getGet();
        Operation secondOperation = paths.get(PATH_2_REF).getGet();
        assertNotNull(firstOperation);
        assertNotNull(secondOperation);
        assertNotEquals(firstOperation.getOperationId(), secondOperation.getOperationId());
    }

    @Test(description = "Test a Set of classes")
    public void testSetOfClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(SecurityResource.class);
        classes.add(DuplicatedSecurityResource.class);

        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(classes);
        assertNotNull(openAPI);
        Components components = openAPI.getComponents();
        assertNotNull(components);
        Map<String, SecurityScheme> securitySchemes = components.getSecuritySchemes();
        assertNotNull(securitySchemes);
        assertEquals(SECURITY_SCHEMAS, securitySchemes.size());
    }

    @Test(description = "Deprecated Method")
    public void testDeprecatedMethod() {
        Reader reader = new Reader(new OpenAPI());
        Method[] methods = DeprecatedFieldsResource.class.getMethods();
        Operation deprecatedOperation = reader.parseMethod(methods[0], null);
        assertNotNull(deprecatedOperation);
        assertTrue(deprecatedOperation.getDeprecated());
    }

    @Test(description = "Get tags")
    public void testGetTags() {
        Reader reader = new Reader(new OpenAPI());
        Method[] methods = TagsResource.class.getMethods();
        Operation operation = reader.parseMethod(methods[0], null);
        assertNotNull(operation);
        assertEquals(TAG_NUMBER, operation.getTags().size());
        assertEquals(EXAMPLE_TAG, operation.getTags().get(0));
        assertEquals(SECOND_TAG, operation.getTags().get(1));
    }

    @Test(description = "Responses")
    public void testGetResponses() {
        Reader reader = new Reader(new OpenAPI());

        Method[] methods = ResponsesResource.class.getMethods();

        Operation responseOperation = reader.parseMethod(methods[0], null);
        assertNotNull(responseOperation);

        ApiResponses responses = responseOperation.getResponses();
        assertEquals(RESPONSES_NUMBER, responses.size());

        ApiResponse apiResponse = responses.get(RESPONSE_CODE_200);
        assertNotNull(apiResponse);
        assertEquals(RESPONSE_DESCRIPTION, apiResponse.getDescription());
    }

    @Test(description = "External Docs")
    public void testGetExternalDocs() {
        Reader reader = new Reader(new OpenAPI());

        Method[] methods = ExternalDocsReference.class.getMethods();
        Operation externalDocsOperation = reader.parseMethod(methods[0], null);
        assertNotNull(externalDocsOperation);
        ExternalDocumentation externalDocs = externalDocsOperation.getExternalDocs();
        assertEquals(EXTERNAL_DOCS_DESCRIPTION, externalDocs.getDescription());
        assertEquals(EXTERNAL_DOCS_URL, externalDocs.getUrl());
    }

    @Test(description = "Security Requirement")
    public void testSecurityRequirement() {
        Reader reader = new Reader(new OpenAPI());
        Method[] methods = SecurityResource.class.getMethods();

        Operation securityOperation = reader.parseMethod(methods[0], null);
        assertNotNull(securityOperation);
        List<SecurityRequirement> securityRequirements = securityOperation.getSecurity();
        assertNotNull(securityRequirements);
        assertEquals(SECURITY_REQUIREMENT_NUMBER, securityRequirements.size());
        List<String> scopes = securityRequirements.get(0).get(SECURITY_KEY);
        assertNotNull(scopes);
        assertEquals(SCOPE_NUMBER, scopes.size());
        assertEquals(SCOPE_VALUE1, scopes.get(0));
        assertEquals(SCOPE_VALUE2, scopes.get(1));
    }

    @Test(description = "Callbacks")
    public void testGetCallbacks() {
        Reader reader = new Reader(new OpenAPI());
        Method[] methods = SimpleCallbackResource.class.getMethods();
        Operation callbackOperation = reader.parseMethod(methods[0], null);
        assertNotNull(callbackOperation);
        Map<String, Callback> callbacks = callbackOperation.getCallbacks();
        assertNotNull(callbacks);
        Callback callback = callbacks.get(CALLBACK_SUBSCRIPTION_ID);
        assertNotNull(callback);
        PathItem pathItem = callback.get(CALLBACK_URL);
        assertNotNull(pathItem);
        Operation postOperation = pathItem.getPost();
        assertNotNull(postOperation);
        assertEquals(CALLBACK_POST_OPERATION_DESCRIPTION, postOperation.getDescription());

        Operation getOperation = pathItem.getGet();
        assertNotNull(getOperation);
        assertEquals(CALLBACK_GET_OPERATION_DESCRIPTION, getOperation.getDescription());

        Operation putOperation = pathItem.getPut();
        assertNotNull(putOperation);
        assertEquals(CALLBACK_POST_OPERATION_DESCRIPTION, putOperation.getDescription());
    }

    @Test(description = "Get the Param of an operation")
    public void testSubscriptionIdParam() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(BasicFieldsResource.class);
        assertNotNull(openAPI);
        Paths openAPIPaths = openAPI.getPaths();
        assertNotNull(openAPIPaths);
        PathItem pathItem = openAPIPaths.get(PATH_1_REF);
        assertNotNull(pathItem);
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        List<Parameter> parameters = operation.getParameters();
        assertNotNull(parameters);
        assertEquals(PARAMETER_NUMBER, parameters.size());
        Parameter parameter = parameters.get(0);
        assertNotNull(parameter);
        assertEquals(PARAMETER_IN, parameter.getIn());
        assertEquals(PARAMETER_NAME, parameter.getName());
        assertEquals(PARAMETER_DESCRIPTION, parameter.getDescription());
        assertEquals(Boolean.TRUE, parameter.getRequired());
        assertEquals(Boolean.TRUE, parameter.getAllowEmptyValue());
        assertEquals(Boolean.TRUE, parameter.getAllowReserved());
        Schema schema = parameter.getSchema();
        assertNotNull(schema);
        assertEquals(SCHEMA_TYPE, schema.getType());
        assertEquals(SCHEMA_FORMAT, schema.getFormat());
        assertEquals(SCHEMA_DESCRIPTION, schema.getDescription());
        assertEquals(Boolean.TRUE, schema.getReadOnly());

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

    @Test
    public void testClassWithGenericType() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ClassWithGenericType.class);
        assertNotNull(openAPI);

        assertNotNull(openAPI.getComponents().getSchemas().get("IssueTemplateRet"));
        assertNotNull(openAPI.getComponents().getSchemas().get("B"));
        assertNotNull(openAPI.getComponents().getSchemas().get("B").getProperties().get("test"));
        assertEquals(((Schema)openAPI.getComponents().getSchemas().get("B").getProperties().get("test")).get$ref(), "#/components/schemas/IssueTemplateRet");

        //Yaml.prettyPrint(openAPI);
    }


    public static class A{
        public B b;
    }
    public static class IssueTemplate<T>{

        public T getTemplateTest(){return null;}
        public String getTemplateTestString(){return null;}
    }
    public static class B{
        public IssueTemplate<Ret> getTest(){return null;}
    }

    public static class Ret{
        public String c;

    }


    static class ClassWithGenericType {
        @Path("/test")
        @Produces("application/json")
        @Consumes("application/json")
        @GET
        @io.swagger.v3.oas.annotations.Operation(tags="/receiver/rest")
        //public void test1(@QueryParam("aa") String a) {
        public void test1(A a) {
        }
    }

    @Test(description = "test resource with array in response content")
    public void test2497() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ResponseContentWithArrayResource.class);

        Paths paths = openAPI.getPaths();
        assertEquals(paths.size(), 1);
        PathItem pathItem = paths.get("/user");
        assertNotNull(pathItem);
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        ArraySchema schema = (ArraySchema)operation.getResponses().get("200").getContent().values().iterator().next().getSchema();
        assertNotNull(schema);
        assertEquals(schema.getItems().get$ref(), "#/components/schemas/User");
    }
}
