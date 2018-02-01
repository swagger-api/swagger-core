package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.resources.BasicFieldsResource;
import io.swagger.v3.jaxrs2.resources.BookStoreTicket2646;
import io.swagger.v3.jaxrs2.resources.CompleteFieldsResource;
import io.swagger.v3.jaxrs2.resources.DeprecatedFieldsResource;
import io.swagger.v3.jaxrs2.resources.DuplicatedOperationIdResource;
import io.swagger.v3.jaxrs2.resources.DuplicatedOperationMethodNameResource;
import io.swagger.v3.jaxrs2.resources.DuplicatedSecurityResource;
import io.swagger.v3.jaxrs2.resources.ExternalDocsReference;
import io.swagger.v3.jaxrs2.resources.ResourceWithSubResource;
import io.swagger.v3.jaxrs2.resources.ResponseContentWithArrayResource;
import io.swagger.v3.jaxrs2.resources.ResponsesResource;
import io.swagger.v3.jaxrs2.resources.SecurityResource;
import io.swagger.v3.jaxrs2.resources.ServersResource;
import io.swagger.v3.jaxrs2.resources.SimpleCallbackResource;
import io.swagger.v3.jaxrs2.resources.SimpleMethods;
import io.swagger.v3.jaxrs2.resources.SubResourceHead;
import io.swagger.v3.jaxrs2.resources.TagsResource;
import io.swagger.v3.jaxrs2.resources.Test2607;
import io.swagger.v3.jaxrs2.resources.TestResource;
import io.swagger.v3.jaxrs2.resources.extensions.ExtensionsResource;
import io.swagger.v3.jaxrs2.resources.extensions.OperationExtensionsResource;
import io.swagger.v3.jaxrs2.resources.extensions.ParameterExtensionsResource;
import io.swagger.v3.jaxrs2.resources.extensions.RequestBodyExtensionsResource;
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
import static org.testng.Assert.assertFalse;
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

    @Test(description = "Get a Duplicated Operation Id with same id as method name")
    public void testResolveDuplicatedOperationIdMethodName() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(DuplicatedOperationMethodNameResource.class);

        Paths paths = openAPI.getPaths();
        assertNotNull(paths);
        Operation firstOperation = paths.get("/1").getGet();
        Operation secondOperation = paths.get("/2").getGet();
        assertNotNull(firstOperation);
        assertNotNull(secondOperation);
        assertNotEquals(firstOperation.getOperationId(), secondOperation.getOperationId());
        Operation thirdOperation = paths.get("/3").getGet();
        Operation fourthOperation = paths.get("/4").getGet();
        assertNotNull(thirdOperation);
        assertNotNull(fourthOperation);
        assertNotEquals(thirdOperation.getOperationId(), fourthOperation.getOperationId());

    }

    @Test(description = "Test a Set of classes")
    public void testSetOfClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(SecurityResource.class);
        classes.add(DuplicatedSecurityResource.class);

        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(classes);
        assertNotNull(openAPI);
        assertEquals(openAPI.getPaths().get("/").getGet().getSecurity().size(), 2);
        assertEquals(openAPI.getPaths().get("/2").getGet().getSecurity().size(), 3);
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
        OpenAPI openAPI = reader.read(TagsResource.class);
        Operation operation = openAPI.getPaths().get("/").getGet();
        assertNotNull(operation);
        assertEquals(6, operation.getTags().size());
        assertEquals(operation.getTags().get(3), EXAMPLE_TAG);
        assertEquals(operation.getTags().get(1), SECOND_TAG);
        assertEquals(openAPI.getTags().get(1).getDescription(), "desc definition");
        assertEquals(openAPI.getTags().get(2).getExternalDocs().getDescription(), "docs desc");
    }

    @Test(description = "Get servers")
    public void testGetServers() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ServersResource.class);
        Operation operation = openAPI.getPaths().get("/").getGet();
        assertNotNull(operation);
        assertEquals(5, operation.getServers().size());
        assertEquals(operation.getServers().get(0).getUrl(), "http://class1");
        assertEquals(operation.getServers().get(1).getUrl(), "http://class2");
        assertEquals(operation.getServers().get(2).getUrl(), "http://method1");
        assertEquals(operation.getServers().get(3).getUrl(), "http://method2");
        assertEquals(operation.getServers().get(4).getUrl(), "http://op1");

        assertEquals(operation.getServers().get(0).getVariables().size(), 2);
        assertEquals(operation.getServers().get(0).getVariables().get("var1").getDescription(), "var 1");
        assertEquals(operation.getServers().get(0).getVariables().get("var1").getEnum().size(), 2);

        assertEquals(openAPI.getServers().get(0).getDescription(), "definition server 1");
    }

    @Test(description = "Responses")
    public void testGetResponses() {
        Reader reader = new Reader(new OpenAPI());

        Method[] methods = ResponsesResource.class.getMethods();

        Operation responseOperation = reader.parseMethod(Arrays.stream(methods).filter(
                (method -> method.getName().equals("getResponses"))).findFirst().get(), null);
        assertNotNull(responseOperation);

        ApiResponses responses = responseOperation.getResponses();
        assertEquals(RESPONSES_NUMBER, responses.size());

        ApiResponse apiResponse = responses.get(RESPONSE_CODE_200);
        assertNotNull(apiResponse);
        assertEquals(RESPONSE_DESCRIPTION, apiResponse.getDescription());
    }

    @Test(description = "Responses with composition")
    public void testGetResponsesWithComposition() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(ResponsesResource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex output\n" +
                "        object\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SampleResponseSchema'\n" +
                "        default:\n" +
                "          description: boo\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/GenericError'\n" +
                "      deprecated: true\n" +
                "  /allOf:\n" +
                "    get:\n" +
                "      summary: Test inheritance / polymorphism\n" +
                "      operationId: getAllOf\n" +
                "      parameters:\n" +
                "      - name: number\n" +
                "        in: query\n" +
                "        description: Test inheritance / polymorphism\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        example: 1\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: bean answer\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: string\n" +
                "                allOf:\n" +
                "                - $ref: '#/components/schemas/MultipleSub1Bean'\n" +
                "                - $ref: '#/components/schemas/MultipleSub2Bean'\n" +
                "  /anyOf:\n" +
                "    get:\n" +
                "      summary: Test inheritance / polymorphism\n" +
                "      operationId: getAnyOf\n" +
                "      parameters:\n" +
                "      - name: number\n" +
                "        in: query\n" +
                "        description: Test inheritance / polymorphism\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        example: 1\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: bean answer\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: string\n" +
                "                anyOf:\n" +
                "                - $ref: '#/components/schemas/MultipleSub1Bean'\n" +
                "                - $ref: '#/components/schemas/MultipleSub2Bean'\n" +
                "  /oneOf:\n" +
                "    get:\n" +
                "      summary: Test inheritance / polymorphism\n" +
                "      operationId: getOneOf\n" +
                "      parameters:\n" +
                "      - name: number\n" +
                "        in: query\n" +
                "        description: Test inheritance / polymorphism\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        example: 1\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: bean answer\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: string\n" +
                "                oneOf:\n" +
                "                - $ref: '#/components/schemas/MultipleSub1Bean'\n" +
                "                - $ref: '#/components/schemas/MultipleSub2Bean'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    MultipleSub2Bean:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        d:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "      description: MultipleSub2Bean\n" +
                "      allOf:\n" +
                "      - $ref: '#/components/schemas/MultipleBaseBean'\n" +
                "    GenericError:\n" +
                "      type: object\n" +
                "    MultipleBaseBean:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        beanType:\n" +
                "          type: string\n" +
                "        a:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        b:\n" +
                "          type: string\n" +
                "      description: MultipleBaseBean\n" +
                "    MultipleSub1Bean:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        c:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "      description: MultipleSub1Bean\n" +
                "      allOf:\n" +
                "      - $ref: '#/components/schemas/MultipleBaseBean'\n" +
                "    SampleResponseSchema:\n" +
                "      type: object";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "External Docs")
    public void testGetExternalDocs() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(ExternalDocsReference.class);
        Operation externalDocsOperation = openAPI.getPaths().get("/").getGet();

        ExternalDocumentation externalDocs = externalDocsOperation.getExternalDocs();
        assertEquals(externalDocs.getDescription(), "External documentation description in method");
        assertEquals(externalDocs.getUrl(), EXTERNAL_DOCS_URL);
        externalDocs = openAPI.getComponents().getSchemas().get("ExternalDocsSchema").getExternalDocs();
        assertEquals("External documentation description in schema", externalDocs.getDescription());
        assertEquals(externalDocs.getUrl(), EXTERNAL_DOCS_URL);
    }

    @Test(description = "OperationExtensions Tests")
    public void testOperationExtensions() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(OperationExtensionsResource.class);
        assertNotNull(openAPI);
        Map<String, Object> extensions = openAPI.getPaths().get("/").getGet().getExtensions();
        assertEquals(extensions.size(), 2);
        assertNotNull(extensions.get("x-operation"));
        assertNotNull(extensions.get("x-operation-extensions"));
    }

    @Test(description = "ParameterExtensions Tests")
    public void testParameterExtensions() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ParameterExtensionsResource.class);
        assertNotNull(openAPI);
        Map<String, Object> extensions = openAPI.getPaths().get("/").getGet().getParameters().get(0).getExtensions();
        assertNotNull(extensions);
        assertEquals(1, extensions.size());
        assertNotNull(extensions.get("x-parameter"));

    }

    @Test(description = "RequestBody Tests")
    public void testRequestBodyExtensions() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(RequestBodyExtensionsResource.class);
        assertNotNull(openAPI);
        Map<String, Object> extensions = openAPI.getPaths().get("/user").getGet().
                getRequestBody().getExtensions();
        assertNotNull(extensions);
        assertEquals(extensions.size(), 2);
        assertNotNull(extensions.get("x-extension"));
        assertNotNull(extensions.get("x-extension2"));
    }

    @Test(description = "Extensions Tests")
    public void testExtensions() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ExtensionsResource.class);
        assertNotNull(openAPI);
        SerializationMatchers.assertEqualsToYaml(openAPI, ExtensionsResource.YAML);
    }

    @Test(description = "Security Requirement")
    public void testSecurityRequirement() {
        Reader reader = new Reader(new OpenAPI());
        Method[] methods = SecurityResource.class.getDeclaredMethods();
        Operation securityOperation = reader.parseMethod(Arrays.stream(methods).filter(
                (method -> method.getName().equals("getSecurity"))).findFirst().get(), null);
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
        assertEquals(((Schema) openAPI.getComponents().getSchemas().get("B").getProperties().get("test")).get$ref(), "#/components/schemas/IssueTemplateRet");

        //Yaml.prettyPrint(openAPI);
    }

    public static class A {
        public B b;
    }

    public static class IssueTemplate<T> {

        public T getTemplateTest() {
            return null;
        }

        public String getTemplateTestString() {
            return null;
        }
    }

    public static class B {
        public IssueTemplate<Ret> getTest() {
            return null;
        }
    }

    public static class Ret {
        public String c;

    }

    static class ClassWithGenericType {
        @Path("/test")
        @Produces("application/json")
        @Consumes("application/json")
        @GET
        @io.swagger.v3.oas.annotations.Operation(tags = "/receiver/rest")
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
        ArraySchema schema = (ArraySchema) operation.getResponses().get("200").getContent().values().iterator().next().getSchema();
        assertNotNull(schema);
        assertEquals(schema.getItems().get$ref(), "#/components/schemas/User");
    }

    @Test(description = "test resource with subresources")
    public void testResourceWithSubresources() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ResourceWithSubResource.class);

        Paths paths = openAPI.getPaths();
        assertEquals(paths.size(), 3);
        PathItem pathItem = paths.get("/employees/{id}");
        assertNotNull(pathItem);
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        ArraySchema arraySchema = (ArraySchema) operation.getResponses().get("200").getContent().values().iterator().next().getSchema();
        assertNotNull(arraySchema);
        assertEquals(arraySchema.getItems().get$ref(), "#/components/schemas/Pet");

        pathItem = paths.get("/employees/{id}/{id}");
        assertNotNull(pathItem);
        operation = pathItem.getGet();
        assertNotNull(operation);
        Schema schema = operation.getResponses().get("200").getContent().values().iterator().next().getSchema();
        assertNotNull(schema);
        assertEquals(schema.get$ref(), "#/components/schemas/Pet");

        pathItem = paths.get("/employees/noPath");
        assertNotNull(pathItem);
        operation = pathItem.getGet();
        assertNotNull(operation);
        schema = operation.getResponses().getDefault().getContent().values().iterator().next().getSchema();
        assertNotNull(schema);
        assertEquals(schema.getType(), "string");
    }

    @Test(description = "test another resource with subresources")
    public void testAnotherResourceWithSubresources() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(TestResource.class);

        Paths paths = openAPI.getPaths();
        assertEquals(paths.size(), 3);
        PathItem pathItem = paths.get("/test/status");
        assertNotNull(pathItem);
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        assertTrue(operation.getResponses().getDefault().getContent().keySet().contains("application/json"));
        Schema schema = operation.getResponses().getDefault().getContent().values().iterator().next().getSchema();
        assertNotNull(schema);
        assertEquals(schema.getType(), "string");


        pathItem = paths.get("/test/more/otherStatus");
        assertNotNull(pathItem);
        operation = pathItem.getGet();
        assertNotNull(operation);
        assertTrue(operation.getResponses().getDefault().getContent().keySet().contains("application/json"));
        assertFalse(operation.getResponses().getDefault().getContent().keySet().contains("application/xml"));
        schema = operation.getResponses().getDefault().getContent().values().iterator().next().getSchema();
        assertNotNull(schema);
        assertEquals(schema.getType(), "string");

        pathItem = paths.get("/test/evenmore/otherStatus");
        assertNotNull(pathItem);
        operation = pathItem.getGet();
        assertNotNull(operation);
        assertTrue(operation.getResponses().getDefault().getContent().keySet().contains("application/json"));
        assertFalse(operation.getResponses().getDefault().getContent().keySet().contains("application/xml"));
        schema = operation.getResponses().getDefault().getContent().values().iterator().next().getSchema();
        assertNotNull(schema);
        assertEquals(schema.getType(), "string");
        assertEquals(operation.getRequestBody().getContent().get("application/json").getSchema().get$ref(), "#/components/schemas/Pet");
    }

    @Test(description = "scan resource with class-based sub-resources")
    public void testResourceWithClassBasedSubresources() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(SubResourceHead.class);

        Paths paths = openAPI.getPaths();
        assertEquals(paths.size(), 3);
        PathItem pathItem = paths.get("/head/tail/hello");
        assertNotNull(pathItem);
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        assertTrue(operation.getResponses().getDefault().getContent().keySet().contains("*/*"));
        Schema schema = operation.getResponses().getDefault().getContent().values().iterator().next().getSchema();
        assertNotNull(schema);
        assertEquals(schema.getType(), "string");


        pathItem = paths.get("/head/tail/{string}");
        assertNotNull(pathItem);
        operation = pathItem.getGet();
        assertNotNull(operation);
        assertTrue(operation.getResponses().getDefault().getContent().keySet().contains("*/*"));
        schema = operation.getResponses().getDefault().getContent().values().iterator().next().getSchema();
        assertNotNull(schema);
        assertEquals(schema.getType(), "string");

        pathItem = paths.get("/head/noPath");
        assertNotNull(pathItem);
        operation = pathItem.getGet();
        assertNotNull(operation);
        assertTrue(operation.getResponses().getDefault().getContent().keySet().contains("*/*"));
        schema = operation.getResponses().getDefault().getContent().values().iterator().next().getSchema();
        assertNotNull(schema);
        assertEquals(schema.getType(), "string");
    }

    @Test(description = "test ticket #2607 resource with subresources")
    public void test2607() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(Test2607.class);

        Paths paths = openAPI.getPaths();
        assertEquals(paths.size(), 2);
        PathItem pathItem = paths.get("/swaggertest/name");
        assertNotNull(pathItem);
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        assertTrue(operation.getResponses().getDefault().getContent().keySet().contains("text/plain"));
        Schema schema = operation.getResponses().getDefault().getContent().values().iterator().next().getSchema();
        assertNotNull(schema);
        assertEquals(schema.getType(), "string");


        pathItem = paths.get("/swaggertest/subresource/version");
        assertNotNull(pathItem);
        operation = pathItem.getGet();
        assertNotNull(operation);
        assertTrue(operation.getResponses().getDefault().getContent().keySet().contains("text/plain"));
        schema = operation.getResponses().getDefault().getContent().values().iterator().next().getSchema();
        assertNotNull(schema);
        assertEquals(schema.getType(), "string");
    }

    @Test(description = "test ticket #2646 method annotated with @Produce")
    public void test2646() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(BookStoreTicket2646.class);
        Paths paths = openAPI.getPaths();
        assertEquals(paths.size(), 2);
        PathItem pathItem = paths.get("/bookstore");
        assertNotNull(pathItem);
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        assertTrue(operation.getResponses().getDefault().getContent().keySet().contains("application/json"));


        pathItem = paths.get("/bookstore/{id}");
        assertNotNull(pathItem);
        operation = pathItem.getDelete();
        assertNotNull(operation);
        assertTrue(operation.getResponses().getDefault().getContent().keySet().contains("*/*"));

    }
}
