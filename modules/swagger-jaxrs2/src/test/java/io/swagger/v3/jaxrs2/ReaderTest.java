package io.swagger.v3.jaxrs2;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.core.filter.OpenAPISpecFilter;
import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.model.ApiDescription;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.resources.SingleExampleResource;
import io.swagger.v3.jaxrs2.resources.BasicFieldsResource;
import io.swagger.v3.jaxrs2.resources.BookStoreTicket2646;
import io.swagger.v3.jaxrs2.resources.ClassPathParentResource;
import io.swagger.v3.jaxrs2.resources.ClassPathSubResource;
import io.swagger.v3.jaxrs2.resources.CompleteFieldsResource;
import io.swagger.v3.jaxrs2.resources.DeprecatedFieldsResource;
import io.swagger.v3.jaxrs2.resources.DuplicatedOperationIdResource;
import io.swagger.v3.jaxrs2.resources.DuplicatedOperationMethodNameResource;
import io.swagger.v3.jaxrs2.resources.DuplicatedSecurityResource;
import io.swagger.v3.jaxrs2.resources.EnhancedResponsesResource;
import io.swagger.v3.jaxrs2.resources.ExternalDocsReference;
import io.swagger.v3.jaxrs2.resources.MyClass;
import io.swagger.v3.jaxrs2.resources.MyOtherClass;
import io.swagger.v3.jaxrs2.resources.RefCallbackResource;
import io.swagger.v3.jaxrs2.resources.RefExamplesResource;
import io.swagger.v3.jaxrs2.resources.RefHeaderResource;
import io.swagger.v3.jaxrs2.resources.RefLinksResource;
import io.swagger.v3.jaxrs2.resources.RefParameter3029Resource;
import io.swagger.v3.jaxrs2.resources.RefParameter3074Resource;
import io.swagger.v3.jaxrs2.resources.RefParameterResource;
import io.swagger.v3.jaxrs2.resources.RefRequestBodyResource;
import io.swagger.v3.jaxrs2.resources.RefResponsesResource;
import io.swagger.v3.jaxrs2.resources.RefSecurityResource;
import io.swagger.v3.jaxrs2.resources.ResourceWithSubResource;
import io.swagger.v3.jaxrs2.resources.ResponseContentWithArrayResource;
import io.swagger.v3.jaxrs2.resources.ResponsesResource;
import io.swagger.v3.jaxrs2.resources.SecurityResource;
import io.swagger.v3.jaxrs2.resources.ServersResource;
import io.swagger.v3.jaxrs2.resources.SimpleCallbackResource;
import io.swagger.v3.jaxrs2.resources.SimpleExamplesResource;
import io.swagger.v3.jaxrs2.resources.SimpleMethods;
import io.swagger.v3.jaxrs2.resources.SimpleParameterResource;
import io.swagger.v3.jaxrs2.resources.SimpleRequestBodyResource;
import io.swagger.v3.jaxrs2.resources.SimpleResponsesResource;
import io.swagger.v3.jaxrs2.resources.SubResourceHead;
import io.swagger.v3.jaxrs2.resources.TagsResource;
import io.swagger.v3.jaxrs2.resources.Test2607;
import io.swagger.v3.jaxrs2.resources.TestResource;
import io.swagger.v3.jaxrs2.resources.Ticket2340Resource;
import io.swagger.v3.jaxrs2.resources.Ticket2644ConcreteImplementation;
import io.swagger.v3.jaxrs2.resources.Ticket2763Resource;
import io.swagger.v3.jaxrs2.resources.Ticket2793Resource;
import io.swagger.v3.jaxrs2.resources.Ticket2794Resource;
import io.swagger.v3.jaxrs2.resources.Ticket2806Resource;
import io.swagger.v3.jaxrs2.resources.Ticket2818Resource;
import io.swagger.v3.jaxrs2.resources.Ticket2848Resource;
import io.swagger.v3.jaxrs2.resources.Ticket3015Resource;
import io.swagger.v3.jaxrs2.resources.UserAnnotationResource;
import io.swagger.v3.jaxrs2.resources.extensions.ExtensionsResource;
import io.swagger.v3.jaxrs2.resources.extensions.OperationExtensionsResource;
import io.swagger.v3.jaxrs2.resources.extensions.ParameterExtensionsResource;
import io.swagger.v3.jaxrs2.resources.extensions.RequestBodyExtensionsResource;
import io.swagger.v3.jaxrs2.resources.rs.ProcessTokenRestService;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
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
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

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
                Operation operation = reader.parseMethod(method, null, null);
                assertNotNull(operation);
            }
        }
    }

    @Test(description = "Get a Summary and Description")
    public void testGetSummaryAndDescription() {
        Reader reader = new Reader(new OpenAPI());
        Method[] methods = BasicFieldsResource.class.getMethods();
        Operation operation = reader.parseMethod(methods[0], null, null);
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
        Operation thirdOperation = paths.get(PATH_REF).getPost();
        assertNotNull(firstOperation);
        assertNotNull(secondOperation);
        assertNotNull(thirdOperation);
        assertNotEquals(firstOperation.getOperationId(), secondOperation.getOperationId());
        assertNotEquals(firstOperation.getOperationId(), thirdOperation.getOperationId());
        assertNotEquals(secondOperation.getOperationId(), thirdOperation.getOperationId());
    }

    @Test(description = "Get a Duplicated Operation Id with same id as method name")
    public void testResolveDuplicatedOperationIdMethodName() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(DuplicatedOperationMethodNameResource.class);

        Paths paths = openAPI.getPaths();
        assertNotNull(paths);
        Operation firstOperation = paths.get("/1").getGet();
        Operation secondOperation = paths.get("/2").getGet();
        Operation secondPostOperation = paths.get("/2").getPost();
        Operation thirdPostOperation = paths.get("/3").getPost();
        assertNotNull(firstOperation);
        assertNotNull(secondOperation);
        assertNotNull(secondPostOperation);
        assertNotNull(thirdPostOperation);
        assertNotEquals(firstOperation.getOperationId(), secondOperation.getOperationId());
        assertNotEquals(secondOperation.getOperationId(), secondPostOperation.getOperationId());
        assertNotEquals(secondPostOperation.getOperationId(), thirdPostOperation.getOperationId());
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
        Operation deprecatedOperation = reader.parseMethod(methods[0], null, null);
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
                (method -> method.getName().equals("getResponses"))).findFirst().get(), null, null);
        assertNotNull(responseOperation);

        ApiResponses responses = responseOperation.getResponses();
        assertEquals(RESPONSES_NUMBER, responses.size());

        ApiResponse apiResponse = responses.get(RESPONSE_CODE_200);
        assertNotNull(apiResponse);
        assertEquals(RESPONSE_DESCRIPTION, apiResponse.getDescription());
    }

    @Test(description = "More Responses")
    public void testMoreResponses() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(EnhancedResponsesResource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex output\n" +
                "        object\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: voila!\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SampleResponseSchema'\n" +
                "        \"404\":\n" +
                "          description: not found!\n" +
                "        \"400\":\n" +
                "          description: boo\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/GenericError'\n" +
                "      deprecated: true\n" +
                "components:\n" +
                "  schemas:\n" +
                "    GenericError:\n" +
                "      type: object\n" +
                "    SampleResponseSchema:\n" +
                "      type: object\n";

        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
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
                "        \"200\":\n" +
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
                "        \"200\":\n" +
                "          description: bean answer\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
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
                "        \"200\":\n" +
                "          description: bean answer\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
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
                "        \"200\":\n" +
                "          description: bean answer\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                oneOf:\n" +
                "                - $ref: '#/components/schemas/MultipleSub1Bean'\n" +
                "                - $ref: '#/components/schemas/MultipleSub2Bean'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    SampleResponseSchema:\n" +
                "      type: object\n" +
                "    GenericError:\n" +
                "      type: object\n" +
                "    MultipleSub1Bean:\n" +
                "      type: object\n" +
                "      description: MultipleSub1Bean\n" +
                "      allOf:\n" +
                "      - $ref: '#/components/schemas/MultipleBaseBean'\n" +
                "      - type: object\n" +
                "        properties:\n" +
                "          c:\n" +
                "            type: integer\n" +
                "            format: int32\n" +
                "    MultipleSub2Bean:\n" +
                "      type: object\n" +
                "      description: MultipleSub2Bean\n" +
                "      allOf:\n" +
                "      - $ref: '#/components/schemas/MultipleBaseBean'\n" +
                "      - type: object\n" +
                "        properties:\n" +
                "          d:\n" +
                "            type: integer\n" +
                "            format: int32\n" +
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
                "      description: MultipleBaseBean";
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
                (method -> method.getName().equals("getSecurity"))).findFirst().get(), null, null);
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
        Operation callbackOperation = reader.parseMethod(methods[0], null, null);
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

        assertEquals(openAPI.getComponents().getSchemas().get("User").getRequired().get(0), "issue3438");
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

    @Test(description = "test user annotation")
    public void testUserAnnotation() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(UserAnnotationResource.class);
        Paths paths = openAPI.getPaths();
        assertEquals(paths.size(), 1);
        PathItem pathItem = paths.get("/test/status");
        assertNotNull(pathItem);
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        assertTrue(operation.getTags().contains("test"));
        assertTrue(operation.getResponses().getDefault().getContent().keySet().contains("application/json"));
        Schema schema = operation.getResponses().getDefault().getContent().values().iterator().next().getSchema();
        assertNotNull(schema);
        assertEquals(schema.getType(), "string");

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

    @Test(description = "test ticket #2644 annotated interface")
    public void test2644() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(Ticket2644ConcreteImplementation.class);
        Paths paths = openAPI.getPaths();
        assertEquals(paths.size(), 1);
        PathItem pathItem = paths.get("/resources");
        assertNotNull(pathItem);
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        assertTrue(operation.getResponses().getDefault().getContent().keySet().contains("*/*"));

    }

    @Test(description = "Scan subresource per #2632")
    public void testSubResourceHasTheRightApiPath() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ClassPathParentResource.class);
        assertNotNull(openAPI);
        assertNotNull(openAPI.getPaths().get("/v1/parent"));
        assertNotNull(openAPI.getPaths().get("/v1/parent/{id}"));
        assertEquals(openAPI.getPaths().size(), 2);

        OpenAPI subResourceApi = new Reader(new OpenAPI()).read(ClassPathSubResource.class);
        assertNotNull(subResourceApi);
        assertNotNull(subResourceApi.getPaths().get("/subresource"));
        assertNotNull(subResourceApi.getPaths().get("/subresource/{id}"));
        assertEquals(subResourceApi.getPaths().size(), 2);
    }

    @Test(description = "Resolve Model with XML Properties starting with is prefix per #2635")
    public void testModelResolverXMLPropertiesName() {
        final MyClass myClass = new MyClass();
        myClass.populate("isotonicDrink value", "softDrink value",
                "isoDrink value", "isotonicDrinkOnlyXmlElement value");

        Map<String, Schema> schemas = resolveJaxb(MyClass.class);
        assertNull(schemas.get("MyClass").getProperties().get("isotonicDrink"));
        assertNotNull(schemas.get("MyClass").getProperties().get("beerDrink"));
        assertNotNull(schemas.get("MyClass").getProperties().get("saltDrink"));

        // No JsonProperty or ApiModelProperty, keep original name
        assertNull(schemas.get("MyClass").getProperties().get("beerDrinkXmlElement"));
        assertNotNull(schemas.get("MyClass").getProperties().get("isotonicDrinkOnlyXmlElement"));

    }

    @Test(description = "Maintain Property names per #2635")
    public void testMaintainPropertyNames() {
        final MyOtherClass myOtherClass = new MyOtherClass();
        myOtherClass.populate("myPropertyName value");

        Map<String, Schema> schemas = resolveJaxb(MyOtherClass.class);
        assertNotNull(schemas.get("MyOtherClass").getProperties().get("MyPrOperTyName"));

    }

    private Map<String, Schema> resolveJaxb(Type type) {

        List<ModelConverter> converters = new CopyOnWriteArrayList<ModelConverter> ();

        ObjectMapper mapper = JaxbObjectMapperFactory.getMapper();
        converters.add(new ModelResolver(mapper));

        ModelConverterContextImpl context = new ModelConverterContextImpl(
                converters);

        Schema resolve = context.resolve(new AnnotatedType().type(type));
        Map<String, Schema> schemas = new HashMap<String, Schema>();
        for (Map.Entry<String, Schema> entry : context.getDefinedModels()
                .entrySet()) {
            if (entry.getValue().equals(resolve)) {
                schemas.put(entry.getKey(), entry.getValue());
            }
        }
        return schemas;
    }

    @Test(description = "Responses with array schema")
    public void testTicket2763() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket2763Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /array:\n" +
                "    get:\n" +
                "      operationId: getArrayResponses\n" +
                "      responses:\n" +
                "        default:\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: array\n" +
                "                items:\n" +
                "                  $ref: https://openebench.bsc.es/monitor/tool/tool.json\n" +
                "  /schema:\n" +
                "    get:\n" +
                "      operationId: getSchemaResponses\n" +
                "      responses:\n" +
                "        default:\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: https://openebench.bsc.es/monitor/tool/tool.json";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Responses with array schema")
    public void testTicket2340() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket2340Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/test:\n" +
                "    post:\n" +
                "      operationId: getAnimal\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/Animal'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: string\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Animal:\n" +
                "      required:\n" +
                "      - type\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        type:\n" +
                "          type: string\n" +
                "      discriminator:\n" +
                "        propertyName: type\n" +
                "    Cat:\n" +
                "      type: object\n" +
                "      allOf:\n" +
                "      - $ref: '#/components/schemas/Animal'\n" +
                "      - type: object\n" +
                "        properties:\n" +
                "          lives:\n" +
                "            type: integer\n" +
                "            format: int32\n" +
                "    Dog:\n" +
                "      type: object\n" +
                "      allOf:\n" +
                "      - $ref: '#/components/schemas/Animal'\n" +
                "      - type: object\n" +
                "        properties:\n" +
                "          barkVolume:\n" +
                "            type: number\n" +
                "            format: double\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "array schema example")
    public void testTicket2806() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket2806Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test:\n" +
                "    get:\n" +
                "      operationId: getTest\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/Test'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Test:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        stringArray:\n" +
                "          maxItems: 4\n" +
                "          minItems: 2\n" +
                "          uniqueItems: true\n" +
                "          type: array\n" +
                "          description: Array desc\n" +
                "          example:\n" +
                "          - aaa\n" +
                "          - bbb\n" +
                "          items:\n" +
                "            type: string\n" +
                "            description: Hello, World!\n" +
                "            example: Lorem ipsum dolor set\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "NotNull parameters")
    public void testTicket2794() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket2794Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /notnullparameter:\n" +
                "    get:\n" +
                "      operationId: getBooks\n" +
                "      parameters:\n" +
                "      - name: page\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json: {}\n" +
                "  /notnullparameter/newnotnull:\n" +
                "    post:\n" +
                "      operationId: insertnotnull\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/Book'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /notnullparameter/new_reqBody_required:\n" +
                "    post:\n" +
                "      operationId: insert\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/Book'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Book:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        foo:\n" +
                "          type: string\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    /*
    TODO: in a scenario like the one in ticket 2793, currently no NPE is thrown
    but map is still not supported. When solved, update expected yaml in test case accordingly
     */
    @Test(description = "no NPE resolving map")
    public void testTicket2793() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket2793Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /distances:\n" +
                "    get:\n" +
                "      operationId: getDistances\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/DistancesResponse'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    DistancesResponse:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        empty:\n" +
                "          type: boolean\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "test ticket #2818 @Parameter annotation")
    public void test2818() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(Ticket2818Resource.class);
        Paths paths = openAPI.getPaths();
        assertEquals(paths.size(), 1);
        PathItem pathItem = paths.get("/bookstore/{id}");
        assertNotNull(pathItem);
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        assertEquals(operation.getParameters().get(0).getSchema().getType(), "integer");
        assertEquals(operation.getParameters().get(0).getSchema().getFormat(), "int32");

    }

    @Test(description = "Responses with ref")
    public void testResponseWithRef() {
        Components components = new Components();
        components.addResponses("invalidJWT", new ApiResponse().description("when JWT token invalid/expired"));
        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);

        Reader reader = new Reader(oas);

        OpenAPI openAPI = reader.read(RefResponsesResource.class);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex output\n" +
                "        object\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      responses:\n" +
                "        \"200\":\n" +
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
                "        \"401\":\n" +
                "          $ref: '#/components/responses/invalidJWT'\n" +
                "      deprecated: true\n" +
                "components:\n" +
                "  schemas:\n" +
                "    GenericError:\n" +
                "      type: object\n" +
                "    SampleResponseSchema:\n" +
                "      type: object\n" +
                "  responses:\n" +
                "    invalidJWT:\n" +
                "      description: when JWT token invalid/expired";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Responses with filter")
    public void testResponseWithFilter() {
        Components components = new Components();
        components.addResponses("invalidJWT", new ApiResponse().description("when JWT token invalid/expired"));
        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);
        Reader reader = new Reader(oas);

        OpenAPI openAPI = reader.read(SimpleResponsesResource.class);


        OpenAPISpecFilter filterImpl = new RefResponseFilter();
        SpecFilter f = new SpecFilter();
        openAPI = f.filter(openAPI, filterImpl, null, null, null);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex output\n" +
                "        object\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      responses:\n" +
                "        \"200\":\n" +
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
                "        \"401\":\n" +
                "          $ref: '#/components/responses/invalidJWT'\n" +
                "      deprecated: true\n" +
                "components:\n" +
                "  schemas:\n" +
                "    GenericError:\n" +
                "      type: object\n" +
                "    SampleResponseSchema:\n" +
                "      type: object\n" +
                "  responses:\n" +
                "    invalidJWT:\n" +
                "      description: when JWT token invalid/expired";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    class RefResponseFilter extends AbstractSpecFilter {

        @Override
        public Optional<Operation> filterOperation(Operation operation, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
            if ("getWithPayloadResponse".equals(operation.getOperationId())) {
                final ApiResponses apiResponses = (operation.getResponses() == null) ? new ApiResponses() : operation.getResponses();
                apiResponses.addApiResponse("401", new ApiResponse().$ref("#/components/responses/invalidJWT"));
                operation.setResponses(apiResponses);
                return Optional.of(operation);
            }
            return super.filterOperation(operation, api, params, cookies, headers);
        }
    }

    @Test(description = "array schema required property")
    public void testTicket2848() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket2848Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      operationId: getter\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/Town'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Town:\n" +
                "      required:\n" +
                "      - streets\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        streets:\n" +
                "          minItems: 1\n" +
                "          uniqueItems: true\n" +
                "          type: array\n" +
                "          items:\n" +
                "            type: string\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "RequestBody with ref")
    public void testRequestBodyWithRef() {
        Components components = new Components();
        components.addRequestBodies("User", new RequestBody().description("Test RequestBody"));
        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);

        Reader reader = new Reader(oas);
        OpenAPI openAPI = reader.read(RefRequestBodyResource.class);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with a payload complex input object\n" +
                "      operationId: sendPayload\n" +
                "      requestBody:\n" +
                "        $ref: '#/components/requestBodies/User'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "      deprecated: true\n" +
                "components:\n" +
                "  schemas:\n" +
                "    User:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        username:\n" +
                "          type: string\n" +
                "        firstName:\n" +
                "          type: string\n" +
                "        lastName:\n" +
                "          type: string\n" +
                "        email:\n" +
                "          type: string\n" +
                "        password:\n" +
                "          type: string\n" +
                "        phone:\n" +
                "          type: string\n" +
                "        userStatus:\n" +
                "          type: integer\n" +
                "          description: User Status\n" +
                "          format: int32\n" +
                "      xml:\n" +
                "        name: User\n" +
                "  requestBodies:\n" +
                "    User:\n" +
                "      description: Test RequestBody\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "RequestBody with filter")
    public void testRequestBodyWithFilter() {
        Components components = new Components();
        components.addRequestBodies("User", new RequestBody());
        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);

        Reader reader = new Reader(oas);
        OpenAPI openAPI = reader.read(SimpleRequestBodyResource.class);

        OpenAPISpecFilter filterImpl = new RefRequestBodyFilter();
        SpecFilter f = new SpecFilter();
        openAPI = f.filter(openAPI, filterImpl, null, null, null);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with a payload complex input object\n" +
                "      operationId: sendPayload\n" +
                "      requestBody:\n" +
                "        $ref: '#/components/requestBodies/User'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "      deprecated: true\n" +
                "components:\n" +
                "  schemas:\n" +
                "    User:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        username:\n" +
                "          type: string\n" +
                "        firstName:\n" +
                "          type: string\n" +
                "        lastName:\n" +
                "          type: string\n" +
                "        email:\n" +
                "          type: string\n" +
                "        password:\n" +
                "          type: string\n" +
                "        phone:\n" +
                "          type: string\n" +
                "        userStatus:\n" +
                "          type: integer\n" +
                "          description: User Status\n" +
                "          format: int32\n" +
                "      xml:\n" +
                "        name: User\n" +
                "  requestBodies:\n" +
                "    User: {}\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    class RefRequestBodyFilter extends AbstractSpecFilter {
        @Override
        public Optional<Operation> filterOperation(Operation operation, ApiDescription api, Map<String, List<String>> params,
                                                   Map<String, String> cookies, Map<String, List<String>> headers) {
            if ("sendPayload".equals(operation.getOperationId())) {
                final RequestBody requestBody = new RequestBody();
                requestBody.set$ref("#/components/requestBodies/User");
                operation.setRequestBody(requestBody);
                return Optional.of(operation);
            }
            return super.filterOperation(operation, api, params, cookies, headers);
        }
    }

    @Test(description = "Parameter with ref")
    public void testParameterWithRef() {
        Components components = new Components();
        components.addParameters("id", new Parameter()
                .description("Id Description")
                .schema(new IntegerSchema())
                .in(ParameterIn.QUERY.toString())
                .example(1)
                .required(true));
        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);

        Reader reader = new Reader(oas);
        OpenAPI openAPI = reader.read(RefParameterResource.class);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with a payload complex input object\n" +
                "      operationId: sendPayload\n" +
                "      parameters:\n" +
                "      - $ref: '#/components/parameters/id'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "      deprecated: true\n" +
                "components:\n" +
                "  parameters: \n" +
                "    id:\n" +
                "      in: query\n" +
                "      description: Id Description\n" +
                "      required: true\n" +
                "      schema:\n" +
                "        type: integer\n" +
                "        format: int32\n" +
                "      example: 1\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Responses with filter")
    public void testParameterWithFilter() {
        Components components = new Components();
        components.addParameters("id", new Parameter()
                .description("Id Description")
                .schema(new IntegerSchema())
                .in(ParameterIn.QUERY.toString())
                .example(1)
                .required(true));

        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);

        Reader reader = new Reader(oas);
        OpenAPI openAPI = reader.read(SimpleParameterResource.class);

        OpenAPISpecFilter filterImpl = new RefParameterFilter();
        SpecFilter f = new SpecFilter();
        openAPI = f.filter(openAPI, filterImpl, null, null, null);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with a payload complex input object\n" +
                "      operationId: sendPayload\n" +
                "      parameters:\n" +
                "      - $ref: '#/components/parameters/id'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "      deprecated: true\n" +
                "components:\n" +
                "  parameters: \n" +
                "    id:\n" +
                "      in: query\n" +
                "      description: Id Description\n" +
                "      required: true\n" +
                "      schema:\n" +
                "        type: integer\n" +
                "        format: int32\n" +
                "      example: 1\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    class RefParameterFilter extends AbstractSpecFilter {
        @Override
        public Optional<Operation> filterOperation(Operation operation, ApiDescription api, Map<String, List<String>> params,
                                                   Map<String, String> cookies, Map<String, List<String>> headers) {
            if ("sendPayload".equals(operation.getOperationId())) {
                final Parameter parameter = new Parameter();
                parameter.set$ref("#/components/parameters/id");
                operation.getParameters().clear();
                operation.addParametersItem(parameter);
                return Optional.of(operation);
            }
            return super.filterOperation(operation, api, params, cookies, headers);
        }
    }

    @Test(description = "Example with ref")
    public void testExampleWithRef() {
        Components components = new Components();
        components.addExamples("Id", new Example().description("Id Example").summary("Id Example").value("1"));

        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);

        Reader reader = new Reader(oas);
        OpenAPI openAPI = reader.read(RefExamplesResource.class);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /example:\n" +
                "    post:\n" +
                "      description: subscribes a client to updates relevant to the requestor's account\n" +
                "      operationId: subscribe\n" +
                "      parameters:\n" +
                "      - name: subscriptionId\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        style: simple\n" +
                "        schema:\n" +
                "          type: string\n" +
                "          description: Schema\n" +
                "          example: Subscription example\n" +
                "        examples:\n" +
                "          subscriptionId_1:\n" +
                "            summary: Subscription number 12345\n" +
                "            description: subscriptionId_1\n" +
                "            value: 12345\n" +
                "            externalValue: Subscription external value 1\n" +
                "            $ref: '#/components/examples/Id'\n" +
                "        example: example\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              type: integer\n" +
                "              format: int32\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    SubscriptionResponse:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        subscriptionId:\n" +
                "          type: string\n" +
                "  examples:\n" +
                "    Id:\n" +
                "      summary: Id Example\n" +
                "      description: Id Example\n" +
                "      value: \"1\"\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Example with Ref Filter")
    public void testExampleWithFilter() {
        Components components = new Components();
        components.addExamples("Id", new Example().description("Id Example").summary("Id Example").value("1"));

        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);

        Reader reader = new Reader(oas);
        OpenAPI openAPI = reader.read(SimpleExamplesResource.class);

        OpenAPISpecFilter filterImpl = new RefExampleFilter();
        SpecFilter f = new SpecFilter();
        openAPI = f.filter(openAPI, filterImpl, null, null, null);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /example:\n" +
                "    post:\n" +
                "      description: subscribes a client to updates relevant to the requestor's account\n" +
                "      operationId: subscribe\n" +
                "      parameters:\n" +
                "      - example:\n" +
                "          $ref: '#/components/examples/Id'\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              type: integer\n" +
                "              format: int32\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    SubscriptionResponse:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        subscriptionId:\n" +
                "          type: string\n" +
                "  examples:\n" +
                "    Id:\n" +
                "      summary: Id Example\n" +
                "      description: Id Example\n" +
                "      value: \"1\"\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    class RefExampleFilter extends AbstractSpecFilter {
        @Override
        public Optional<Operation> filterOperation(Operation operation, ApiDescription api, Map<String, List<String>> params,
                                                   Map<String, String> cookies, Map<String, List<String>> headers) {
            if ("subscribe".equals(operation.getOperationId())) {
                final Parameter parameter = new Parameter();
                parameter.setExample(new Example().$ref("#/components/examples/Id"));
                operation.getParameters().clear();
                operation.addParametersItem(parameter);
                return Optional.of(operation);
            }
            return super.filterOperation(operation, api, params, cookies, headers);
        }
    }

    @Test(description = "Header with Ref")
    public void testHeaderWithRef() {
        Components components = new Components();
        components.addHeaders("Header", new Header().description("Header Description"));

        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);

        Reader reader = new Reader(oas);
        OpenAPI openAPI = reader.read(RefHeaderResource.class);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /path:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex output\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: voila!\n" +
                "          headers:\n" +
                "            Rate-Limit-Limit:\n" +
                "              description: The number of allowed requests in the current period\n" +
                "              $ref: '#/components/headers/Header'\n" +
                "              style: simple\n" +
                "              schema:\n" +
                "                type: integer\n" +
                "      deprecated: true\n" +
                "components:\n" +
                "  headers:\n" +
                "    Header:\n" +
                "      description: Header Description\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "SecurityScheme with REf")
    public void testSecuritySchemeWithRef() {
        Components components = new Components();
        components.addSecuritySchemes("Security", new SecurityScheme().description("Security Example").
                name("Security").type(SecurityScheme.Type.OAUTH2).$ref("myOauth2Security").in(SecurityScheme.In.HEADER));

        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);

        Reader reader = new Reader(oas);
        OpenAPI openAPI = reader.read(RefSecurityResource.class);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      description: description\n" +
                "      operationId: Operation Id\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "      security:\n" +
                "      - security_key:\n" +
                "        - write:pets\n" +
                "        - read:pets\n" +
                "components:\n" +
                "  securitySchemes:\n" +
                "    Security:\n" +
                "      type: oauth2\n" +
                "      description: Security Example\n" +
                "    myOauth2Security:\n" +
                "      type: oauth2\n" +
                "      description: myOauthSecurity Description\n" +
                "      $ref: '#/components/securitySchemes/Security'\n" +
                "      in: header\n" +
                "      flows:\n" +
                "        implicit:\n" +
                "          authorizationUrl: http://x.com\n" +
                "          scopes:\n" +
                "            write:pets: modify pets in your account\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Link with Ref")
    public void testLinkWithRef() {
        Components components = new Components();
        components.addLinks("Link", new Link().description("Link Description").operationId("id"));
        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);

        Reader reader = new Reader(oas);
        OpenAPI openAPI = reader.read(RefLinksResource.class);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /links:\n" +
                "    get:\n" +
                "      operationId: getUserWithAddress\n" +
                "      parameters:\n" +
                "      - name: userId\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: test description\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/User'\n" +
                "          links:\n" +
                "            address:\n" +
                "              operationId: getAddress\n" +
                "              parameters:\n" +
                "                userId: $request.query.userId\n" +
                "              $ref: '#/components/links/Link'\n" +
                "components:\n" +
                "  links:\n" +
                "    Link:\n" +
                "      operationId: id\n" +
                "      description: Link Description\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Callback with Ref")
    public void testCallbackWithRef() {
        Components components = new Components();
        components.addCallbacks("Callback", new Callback().addPathItem("/post", new PathItem().description("Post Path Item")));
        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);

        Reader reader = new Reader(oas);
        OpenAPI openAPI = reader.read(RefCallbackResource.class);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /simplecallback:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      operationId: getWithNoParameters\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: voila!\n" +
                "      callbacks:\n" +
                "        testCallback1:\n" +
                "          $ref: '#/components/callbacks/Callback'\n" +
                "components:\n" +
                "  callbacks:\n" +
                "    Callback:\n" +
                "      /post:\n" +
                "        description: Post Path Item\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test
    public void testTicket3015() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket3015Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/test:\n" +
                "    get:\n" +
                "      operationId: schemaImpl\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: OK\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                type: string\n" +
                "                format: uri\n" +
                "        \"400\":\n" +
                "          description: Bad Request\n" +
                "        \"500\":\n" +
                "          description: Internal Server Error\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        PrimitiveType.customExcludedClasses().add(URI.class.getName());
        openAPI = reader.read(Ticket3015Resource.class);
        yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/test:\n" +
                "    get:\n" +
                "      operationId: schemaImpl_1\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: OK\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                type: object\n" +
                "                properties:\n" +
                "                  scheme:\n" +
                "                    type: string\n" +
                "                  fragment:\n" +
                "                    type: string\n" +
                "                  authority:\n" +
                "                    type: string\n" +
                "                  userInfo:\n" +
                "                    type: string\n" +
                "                  host:\n" +
                "                    type: string\n" +
                "                  port:\n" +
                "                    type: integer\n" +
                "                    format: int32\n" +
                "                  path:\n" +
                "                    type: string\n" +
                "                  query:\n" +
                "                    type: string\n" +
                "                  schemeSpecificPart:\n" +
                "                    type: string\n" +
                "                  rawSchemeSpecificPart:\n" +
                "                    type: string\n" +
                "                  rawAuthority:\n" +
                "                    type: string\n" +
                "                  rawUserInfo:\n" +
                "                    type: string\n" +
                "                  rawPath:\n" +
                "                    type: string\n" +
                "                  rawQuery:\n" +
                "                    type: string\n" +
                "                  rawFragment:\n" +
                "                    type: string\n" +
                "                  absolute:\n" +
                "                    type: boolean\n" +
                "                  opaque:\n" +
                "                    type: boolean\n" +
                "        \"400\":\n" +
                "          description: Bad Request\n" +
                "        \"500\":\n" +
                "          description: Internal Server Error\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        PrimitiveType.customExcludedClasses().remove(URI.class.getName());
    }


    @Test(description = "Parameter with ref")
    public void testTicket3029() {
        Components components = new Components();
        components.addParameters("id", new Parameter()
                .description("Id Description")
                .schema(new IntegerSchema())
                .in(ParameterIn.QUERY.toString())
                .example(1)
                .required(true));
        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);

        Reader reader = new Reader(oas);
        OpenAPI openAPI = reader.read(RefParameter3029Resource.class);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /2:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      operationId: sendPayload2\n" +
                "      parameters:\n" +
                "      - $ref: '#/components/parameters/id'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /1:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      operationId: sendPayload1\n" +
                "      parameters:\n" +
                "      - $ref: '#/components/parameters/id'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  parameters:\n" +
                "    id:\n" +
                "      in: query\n" +
                "      description: Id Description\n" +
                "      required: true\n" +
                "      schema:\n" +
                "        type: integer\n" +
                "        format: int32\n" +
                "      example: 1\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "response generic subclass")
    public void testTicket3082() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ProcessTokenRestService.class);

        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /token:\n" +
                "    post:\n" +
                "      operationId: create\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/ProcessTokenDTO'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/ProcessTokenDTO'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    ProcessTokenDTO:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        guid:\n" +
                "          type: string\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Filter class return type")
    public void testTicket3074() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI oasResult = reader.read(RefParameter3074Resource.class);
        SerializationMatchers.assertEqualsToYaml(oasResult, RefParameter3074Resource.EXPECTED_YAML_WITH_WRAPPER);

        ModelConverters.getInstance().addClassToSkip("io.swagger.v3.jaxrs2.resources.RefParameter3074Resource$Wrapper");

        reader = new Reader(new OpenAPI());
        oasResult = reader.read(RefParameter3074Resource.class);
        SerializationMatchers.assertEqualsToYaml(oasResult, RefParameter3074Resource.EXPECTED_YAML_WITHOUT_WRAPPER);
    }

    @Test(description = "Single Example")
    public void testSingleExample() {

        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(SingleExampleResource.class);

        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test1:\n" +
                "    post:\n" +
                "      operationId: test1\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "            example:\n" +
                "              foo: foo\n" +
                "              bar: bar\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test2:\n" +
                "    post:\n" +
                "      operationId: test2\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "            example:\n" +
                "              foo: foo\n" +
                "              bar: bar\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    User:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        username:\n" +
                "          type: string\n" +
                "        firstName:\n" +
                "          type: string\n" +
                "        lastName:\n" +
                "          type: string\n" +
                "        email:\n" +
                "          type: string\n" +
                "        password:\n" +
                "          type: string\n" +
                "        phone:\n" +
                "          type: string\n" +
                "        userStatus:\n" +
                "          type: integer\n" +
                "          description: User Status\n" +
                "          format: int32\n" +
                "      xml:\n" +
                "        name: User\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

}
