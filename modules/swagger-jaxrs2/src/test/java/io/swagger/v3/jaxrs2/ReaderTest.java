package io.swagger.v3.jaxrs2;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.core.filter.OpenAPISpecFilter;
import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.model.ApiDescription;
import io.swagger.v3.core.util.Configuration;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.petstore31.PetResource;
import io.swagger.v3.jaxrs2.petstore31.TagResource;
import io.swagger.v3.jaxrs2.resources.ArraySchemaImplementationResource;
import io.swagger.v3.jaxrs2.resources.DefaultResponseResource;
import io.swagger.v3.jaxrs2.resources.Misc31Resource;
import io.swagger.v3.jaxrs2.resources.ParameterMaximumValueResource;
import io.swagger.v3.jaxrs2.resources.ResponseReturnTypeResource;
import io.swagger.v3.jaxrs2.resources.SchemaAdditionalPropertiesBooleanResource;
import io.swagger.v3.jaxrs2.resources.SchemaAdditionalPropertiesResource;
import io.swagger.v3.jaxrs2.resources.SchemaPropertiesResource;
import io.swagger.v3.jaxrs2.resources.SiblingPropResource;
import io.swagger.v3.jaxrs2.resources.SiblingsResource;
import io.swagger.v3.jaxrs2.resources.SiblingsResourceRequestBody;
import io.swagger.v3.jaxrs2.resources.SiblingsResourceRequestBodyMultiple;
import io.swagger.v3.jaxrs2.resources.SiblingsResourceResponse;
import io.swagger.v3.jaxrs2.resources.SiblingsResourceSimple;
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
import io.swagger.v3.jaxrs2.resources.Ticket3587Resource;
import io.swagger.v3.jaxrs2.resources.Ticket3731BisResource;
import io.swagger.v3.jaxrs2.resources.Ticket3731Resource;
import io.swagger.v3.jaxrs2.resources.Ticket4065Resource;
import io.swagger.v3.jaxrs2.resources.Ticket4341Resource;
import io.swagger.v3.jaxrs2.resources.Ticket4412Resource;
import io.swagger.v3.jaxrs2.resources.Ticket4446Resource;
import io.swagger.v3.jaxrs2.resources.Ticket4483Resource;
import io.swagger.v3.jaxrs2.resources.Ticket4804CustomClass;
import io.swagger.v3.jaxrs2.resources.Ticket4804NotBlankResource;
import io.swagger.v3.jaxrs2.resources.Ticket4804ProcessorResource;
import io.swagger.v3.jaxrs2.resources.Ticket4804Resource;
import io.swagger.v3.jaxrs2.resources.Ticket4850Resource;
import io.swagger.v3.jaxrs2.resources.Ticket4859Resource;
import io.swagger.v3.jaxrs2.resources.Ticket4878Resource;
import io.swagger.v3.jaxrs2.resources.Ticket4879Resource;
import io.swagger.v3.jaxrs2.resources.UploadResource;
import io.swagger.v3.jaxrs2.resources.UrlEncodedResourceWithEncodings;
import io.swagger.v3.jaxrs2.resources.UserAnnotationResource;
import io.swagger.v3.jaxrs2.resources.WebHookResource;
import io.swagger.v3.jaxrs2.resources.extensions.ExtensionsResource;
import io.swagger.v3.jaxrs2.resources.extensions.OperationExtensionsResource;
import io.swagger.v3.jaxrs2.resources.extensions.ParameterExtensionsResource;
import io.swagger.v3.jaxrs2.resources.extensions.RequestBodyExtensionsResource;
import io.swagger.v3.jaxrs2.resources.generics.ticket2144.ItemResource;
import io.swagger.v3.jaxrs2.resources.generics.ticket3149.MainResource;
import io.swagger.v3.jaxrs2.resources.generics.ticket3426.Ticket3426Resource;
import io.swagger.v3.jaxrs2.resources.generics.ticket3694.Ticket3694Resource;
import io.swagger.v3.jaxrs2.resources.generics.ticket3694.Ticket3694ResourceExtendedType;
import io.swagger.v3.jaxrs2.resources.generics.ticket3694.Ticket3694ResourceSimple;
import io.swagger.v3.jaxrs2.resources.generics.ticket3694.Ticket3694ResourceSimpleSameReturn;
import io.swagger.v3.jaxrs2.resources.rs.ProcessTokenRestService;
import io.swagger.v3.jaxrs2.resources.ticket3624.Service;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
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
import java.util.concurrent.CompletableFuture;
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
                "                $ref: \"#/components/schemas/SampleResponseSchema\"\n" +
                "        \"404\":\n" +
                "          description: not found!\n" +
                "        \"400\":\n" +
                "          description: boo\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/GenericError\"\n" +
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
                "                $ref: \"#/components/schemas/SampleResponseSchema\"\n" +
                "        default:\n" +
                "          description: boo\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/GenericError\"\n" +
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
                "                - $ref: \"#/components/schemas/MultipleSub1Bean\"\n" +
                "                - $ref: \"#/components/schemas/MultipleSub2Bean\"\n" +
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
                "                - $ref: \"#/components/schemas/MultipleSub1Bean\"\n" +
                "                - $ref: \"#/components/schemas/MultipleSub2Bean\"\n" +
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
                "                - $ref: \"#/components/schemas/MultipleSub1Bean\"\n" +
                "                - $ref: \"#/components/schemas/MultipleSub2Bean\"\n" +
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
                "      - $ref: \"#/components/schemas/MultipleBaseBean\"\n" +
                "      - type: object\n" +
                "        properties:\n" +
                "          c:\n" +
                "            type: integer\n" +
                "            format: int32\n" +
                "    MultipleSub2Bean:\n" +
                "      type: object\n" +
                "      description: MultipleSub2Bean\n" +
                "      allOf:\n" +
                "      - $ref: \"#/components/schemas/MultipleBaseBean\"\n" +
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

    @Test
    public void testClassWithCompletableFuture() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ClassWithCompletableFuture.class);
        assertNotNull(openAPI);

        assertEquals(
            openAPI.getPaths()
                    .get("/myApi")
                    .getGet()
                    .getResponses()
                    .get("default")
                    .getContent()
                    .get("application/json")
                    .getSchema()
                    .get$ref(),
                "#/components/schemas/Ret"
        );
    }

    static class ClassWithCompletableFuture {
        @Path("/myApi")
        @Produces("application/json")
        @Consumes("application/json")
        @GET
        public CompletableFuture<Ret> myApi(A a) {
            return CompletableFuture.completedFuture(new Ret());
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

    @Test(description = "array required property resolved from ArraySchema.arraySchema.requiredMode")
    public void test4341() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(Ticket4341Resource.class);
        Schema userSchema = openAPI.getComponents().getSchemas().get("User");
        List<String> required = userSchema.getRequired();

        assertTrue(required.contains("requiredArray"));
        assertFalse(required.contains("notRequiredArray"));
        assertFalse(required.contains("notRequiredArrayWithNotNull"));
        assertTrue(required.contains("autoRequiredWithNotNull"));
        assertFalse(required.contains("autoNotRequired"));

        assertTrue(
                required.contains("requiredArrayArraySchemaOnly"),
                "arraySchema.requiredMode=REQUIRED should make the array property required " +
                        "even when items schema is not explicitly provided"
        );

        assertFalse(
                required.contains("requiredItemsOnlyArray"),
                "schema(requiredMode=REQUIRED) on items must not make the array property required; " +
                        "requiredness is controlled by arraySchema.requiredMode"
        );
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
                "              $ref: \"#/components/schemas/Animal\"\n" +
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
                "      - $ref: \"#/components/schemas/Animal\"\n" +
                "      - type: object\n" +
                "        properties:\n" +
                "          lives:\n" +
                "            type: integer\n" +
                "            format: int32\n" +
                "    Dog:\n" +
                "      type: object\n" +
                "      allOf:\n" +
                "      - $ref: \"#/components/schemas/Animal\"\n" +
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
                "                $ref: \"#/components/schemas/Test\"\n" +
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
                "              $ref: \"#/components/schemas/Book\"\n" +
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
                "              $ref: \"#/components/schemas/Book\"\n" +
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
                "                $ref: \"#/components/schemas/DistancesResponse\"\n" +
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
                "                $ref: \"#/components/schemas/SampleResponseSchema\"\n" +
                "        default:\n" +
                "          description: boo\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/GenericError\"\n" +
                "        \"401\":\n" +
                "          $ref: \"#/components/responses/invalidJWT\"\n" +
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
                "                $ref: \"#/components/schemas/SampleResponseSchema\"\n" +
                "        default:\n" +
                "          description: boo\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/GenericError\"\n" +
                "        \"401\":\n" +
                "          $ref: \"#/components/responses/invalidJWT\"\n" +
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
                "                $ref: \"#/components/schemas/Town\"\n" +
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
                "        $ref: \"#/components/requestBodies/User\"\n" +
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
                "        $ref: \"#/components/requestBodies/User\"\n" +
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
                "      - $ref: \"#/components/parameters/id\"\n" +
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
                "      - $ref: \"#/components/parameters/id\"\n" +
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
                "            $ref: \"#/components/examples/Id\"\n" +
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
                "                $ref: \"#/components/schemas/SubscriptionResponse\"\n" +
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
                "          $ref: \"#/components/examples/Id\"\n" +
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
                "                $ref: \"#/components/schemas/SubscriptionResponse\"\n" +
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
                "              $ref: \"#/components/headers/Header\"\n" +
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
                "      $ref: \"#/components/securitySchemes/Security\"\n" +
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
                "                $ref: \"#/components/schemas/User\"\n" +
                "          links:\n" +
                "            address:\n" +
                "              operationId: getAddress\n" +
                "              parameters:\n" +
                "                userId: $request.query.userId\n" +
                "              $ref: \"#/components/links/Link\"\n" +
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
                "          $ref: \"#/components/callbacks/Callback\"\n" +
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
                "      - $ref: \"#/components/parameters/id\"\n" +
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
                "      - $ref: \"#/components/parameters/id\"\n" +
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
                "              $ref: \"#/components/schemas/ProcessTokenDTO\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/ProcessTokenDTO\"\n" +
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
                "              $ref: \"#/components/schemas/User\"\n" +
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
                "              $ref: \"#/components/schemas/User\"\n" +
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

    @Test
    public void testTicket3092() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(UploadResource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /upload:\n" +
                "    post:\n" +
                "      operationId: uploadWithBean\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          multipart/form-data:\n" +
                "            schema:\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                name:\n" +
                "                  type: string\n" +
                "                picture:\n" +
                "                  $ref: \"#/components/schemas/picture\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json: {}\n" +
                "  /upload/requestbody:\n" +
                "    post:\n" +
                "      operationId: uploadWithBeanAndRequestBody\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          multipart/form-data:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/UploadRequest\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json: {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    picture:\n" +
                "      type: object\n" +
                "      format: binary\n" +
                "    UploadRequest:\n" +
                "      title: Schema for Upload\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        name:\n" +
                "          type: string\n" +
                "        picture:\n" +
                "          type: string\n" +
                "          format: binary";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Parameter examples ordering")
    public void testTicket3587() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket3587Resource.class);
        String yaml = "openapi: 3.0.1\n"
                + "paths:\n"
                + "  /test/test:\n"
                + "    get:\n"
                + "      operationId: parameterExamplesOrderingTest\n"
                + "      parameters:\n"
                + "      - in: query\n"
                + "        schema:\n"
                + "          type: string\n"
                + "        examples:\n"
                + "          Example One:\n"
                + "            description: Example One\n"
                + "          Example Two:\n"
                + "            description: Example Two\n"
                + "          Example Three:\n"
                + "            description: Example Three\n"
                + "      - in: query\n"
                + "        schema:\n"
                + "          type: string\n"
                + "        examples:\n"
                + "          Example Three:\n"
                + "            description: Example Three\n"
                + "          Example Two:\n"
                + "            description: Example Two\n"
                + "          Example One:\n"
                + "            description: Example One\n"
                + "      responses:\n"
                + "        default:\n"
                + "          description: default response\n"
                + "          content:\n"
                + "            '*/*': {}";
        SerializationMatchers.assertEqualsToYamlExact(openAPI, yaml);
    }

    @Test(description = "Optional handling")
    public void testTicket3624() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Service.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /example/model:\n" +
                "    get:\n" +
                "      tags:\n" +
                "      - ExampleService\n" +
                "      summary: ' Retrieve models for display to the user'\n" +
                "      operationId: getModels\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/Response\"\n" +
                "  /example/model/by/ids:\n" +
                "    get:\n" +
                "      tags:\n" +
                "      - ExampleService\n" +
                "      summary: ' Retrieve models by their ids'\n" +
                "      operationId: getModelsById\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/ByIdResponse\"\n" +
                "  /example/containerized/model:\n" +
                "    get:\n" +
                "      tags:\n" +
                "      - ExampleService\n" +
                "      summary: ' Retrieve review insights for a specific product'\n" +
                "      operationId: getContainerizedModels\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/ContainerizedResponse\"\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Model:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        text:\n" +
                "          type: string\n" +
                "        title:\n" +
                "          type: string\n" +
                "        active:\n" +
                "          type: boolean\n" +
                "        schemaParent:\n" +
                "          $ref: \"#/components/schemas/Model\"\n" +
                "        optionalString:\n" +
                "          type: string\n" +
                "        parent:\n" +
                "          $ref: \"#/components/schemas/Model\"\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "    Response:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        count:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        models:\n" +
                "          type: array\n" +
                "          items:\n" +
                "            $ref: \"#/components/schemas/Model\"\n" +
                "    ByIdResponse:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        modelsById:\n" +
                "          type: object\n" +
                "          additionalProperties:\n" +
                "            $ref: \"#/components/schemas/Model\"\n" +
                "    ContainerizedResponse:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        totalCount:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        containerizedModels:\n" +
                "          type: array\n" +
                "          items:\n" +
                "            $ref: \"#/components/schemas/ModelContainer\"\n" +
                "    ModelContainer:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        text:\n" +
                "          type: string\n" +
                "        model:\n" +
                "          $ref: \"#/components/schemas/Model\"\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int32";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test
    public void testRequestBodyEncoding() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(UrlEncodedResourceWithEncodings.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /things/search:\n" +
                "    post:\n" +
                "      operationId: searchForThings\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/x-www-form-urlencoded:\n" +
                "            schema:\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                id:\n" +
                "                  type: array\n" +
                "                  description: id param\n" +
                "                  items:\n" +
                "                    type: string\n" +
                "                name:\n" +
                "                  type: array\n" +
                "                  items:\n" +
                "                    type: string\n" +
                "            encoding:\n" +
                "              id:\n" +
                "                style: form\n" +
                "                explode: true\n" +
                "              name:\n" +
                "                style: form\n" +
                "                explode: false\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json: {}\n" +
                "  /things/sriracha:\n" +
                "    post:\n" +
                "      operationId: srirachaThing\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/x-www-form-urlencoded:\n" +
                "            schema:\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                id:\n" +
                "                  type: array\n" +
                "                  description: id param\n" +
                "                  items:\n" +
                "                    type: string\n" +
                "                name:\n" +
                "                  type: array\n" +
                "                  items:\n" +
                "                    type: string\n" +
                "            encoding:\n" +
                "              id:\n" +
                "                style: form\n" +
                "                explode: true\n" +
                "              name:\n" +
                "                style: form\n" +
                "                explode: false\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json: {}\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "overridden generic resource methods")
    public void testTicket3694() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket3694ResourceExtendedType.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /foo:\n" +
                "    post:\n" +
                "      tags:\n" +
                "      - Foo\n" +
                "      summary: Foo List in Interface\n" +
                "      operationId: foo\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: array\n" +
                "              items:\n" +
                "                type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /bar:\n" +
                "    post:\n" +
                "      operationId: bar\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: array\n" +
                "              items:\n" +
                "                type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                type: string\n" +
                "  /another:\n" +
                "    post:\n" +
                "      operationId: another\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);

        reader = new Reader(new OpenAPI());
        openAPI = reader.read(Ticket3694Resource.class);
        yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /foo:\n" +
                "    post:\n" +
                "      tags:\n" +
                "      - Foo\n" +
                "      summary: Foo List in Interface\n" +
                "      operationId: foo\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: array\n" +
                "              items:\n" +
                "                type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /bar:\n" +
                "    post:\n" +
                "      operationId: bar\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: array\n" +
                "              items:\n" +
                "                type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                type: string\n" +
                "  /another:\n" +
                "    post:\n" +
                "      operationId: another\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);

        reader = new Reader(new OpenAPI());
        openAPI = reader.read(Ticket3694ResourceSimple.class);
        yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /bar:\n" +
                "    post:\n" +
                "      operationId: bar\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: array\n" +
                "              items:\n" +
                "                type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                type: string";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);

        reader = new Reader(new OpenAPI());
        openAPI = reader.read(Ticket3694ResourceSimpleSameReturn.class);
        yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /bar:\n" +
                "    post:\n" +
                "      operationId: bar\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: array\n" +
                "              items:\n" +
                "                type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "non consistent overridden generic resource methods")
    public void testTicket2144() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(ItemResource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /item/{id}:\n" +
                "    get:\n" +
                "      operationId: getById\n" +
                "      parameters:\n" +
                "      - name: id\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/ItemWithChildren\"\n" +
                "  /item/nogeneric/{id}:\n" +
                "    get:\n" +
                "      operationId: getByIdNoGeneric\n" +
                "      parameters:\n" +
                "      - name: id\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/ItemWithChildren\"\n" +
                "  /item/nogenericsamereturn/{id}:\n" +
                "    get:\n" +
                "      operationId: getByIdNoGenericSameReturn\n" +
                "      parameters:\n" +
                "      - name: id\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/BaseDTO\"\n" +
                "  /item/genericparam:\n" +
                "    post:\n" +
                "      operationId: genericParam\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/ItemWithChildren\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/BaseDTO\"\n" +
                "components:\n" +
                "  schemas:\n" +
                "    ItemWithChildren:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        name:\n" +
                "          type: string\n" +
                "        names:\n" +
                "          type: string\n" +
                "    BaseDTO:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        name:\n" +
                "          type: string";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "overridden generic resource interface default methods")
    public void testTicket3149() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(MainResource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test:\n" +
                "    post:\n" +
                "      tags:\n" +
                "      - Test inheritance on default implementation in interfaces\n" +
                "      operationId: firstEndpoint\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/SampleDTO\"\n" +
                "      responses:\n" +
                "        \"201\":\n" +
                "          description: Created\n" +
                "        \"400\":\n" +
                "          description: Bad Request\n" +
                "        \"403\":\n" +
                "          description: Forbidden\n" +
                "        \"404\":\n" +
                "          description: Not Found\n" +
                "  /test/{id}:\n" +
                "    get:\n" +
                "      tags:\n" +
                "      - Test inheritance on default implementation in interfaces\n" +
                "      operationId: secondEnpoint\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/SampleOtherDTO\"\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: OK\n" +
                "        \"400\":\n" +
                "          description: Bad Request\n" +
                "        \"403\":\n" +
                "          description: Forbidden\n" +
                "        \"404\":\n" +
                "          description: Not Found\n" +
                "  /test/original/{id}:\n" +
                "    get:\n" +
                "      tags:\n" +
                "      - Test inheritance on default implementation in interfaces\n" +
                "      operationId: originalEndpoint\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/SampleOtherDTO\"\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: OK\n" +
                "        \"400\":\n" +
                "          description: Bad Request\n" +
                "        \"403\":\n" +
                "          description: Forbidden\n" +
                "        \"404\":\n" +
                "          description: Not Found\n" +
                "components:\n" +
                "  schemas:\n" +
                "    SampleDTO:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        name:\n" +
                "          type: string\n" +
                "    SampleOtherDTO:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        label:\n" +
                "          type: string";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "overridden generic resource methods operationId")
    public void testTicket3426() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket3426Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /inheritExample/{input}:\n" +
                "    get:\n" +
                "      operationId: get\n" +
                "      parameters:\n" +
                "      - name: input\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                type: string";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Constraints annotations in models")
    public void testTicket3731() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket3731Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/cart:\n" +
                "    get:\n" +
                "      summary: Get cart items\n" +
                "      description: Paging follows RFC 5005.\n" +
                "      operationId: getCart\n" +
                "      parameters:\n" +
                "      - name: pageSize\n" +
                "        in: query\n" +
                "        description: \"Number of items per page. Range[1, 200]\"\n" +
                "        schema:\n" +
                "          maximum: 200\n" +
                "          minimum: 1\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "          default: 50\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                type: array\n" +
                "                items:\n" +
                "                  type: string\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);

        reader = new Reader(new OpenAPI());
        openAPI = reader.read(Ticket3731BisResource.class);
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Test SchemaProperties and additionalProperties annotations")
    public void testSchemaProperties() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(SchemaPropertiesResource.class);
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
                "                type: object\n" +
                "                properties:\n" +
                "                  foo:\n" +
                "                    maximum: 1\n" +
                "                    type: integer\n" +
                "        default:\n" +
                "          description: boo\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                maxProperties: 3\n" +
                "                type: object\n" +
                "                properties:\n" +
                "                  foo:\n" +
                "                    maximum: 1\n" +
                "                    type: integer\n" +
                "                description: various properties\n" +
                "        \"400\":\n" +
                "          description: additionalProperties schema\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                maxProperties: 2\n" +
                "                type: object\n" +
                "                additionalProperties:\n" +
                "                  type: string\n" +
                "        \"401\":\n" +
                "          description: additionalProperties boolean\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                maxProperties: 2\n" +
                "                type: object\n" +
                "                additionalProperties: false\n" +
                "      deprecated: true\n" +
                "  /one:\n" +
                "    get:\n" +
                "      operationId: requestBodySchemaPropertyNoSchema\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/yaml:\n" +
                "            schema:\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                foo:\n" +
                "                  type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/MultipleBaseBean\"\n" +
                "  /two:\n" +
                "    get:\n" +
                "      operationId: requestBodySchemaPropertySchema\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/yaml:\n" +
                "            schema:\n" +
                "              required:\n" +
                "              - foo\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                foo:\n" +
                "                  type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/MultipleBaseBean\"\n" +
                "  /three:\n" +
                "    get:\n" +
                "      operationId: requestBodySchemaPropertySchemaArray\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/yaml:\n" +
                "            schema:\n" +
                "              type: array\n" +
                "              items:\n" +
                "                required:\n" +
                "                - foo\n" +
                "                type: object\n" +
                "                properties:\n" +
                "                  foo:\n" +
                "                    type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/MultipleBaseBean\"\n" +
                "components:\n" +
                "  schemas:\n" +
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
                "      description: MultipleSub1Bean\n" +
                "      allOf:\n" +
                "      - $ref: \"#/components/schemas/MultipleBaseBean\"\n" +
                "      - type: object\n" +
                "        properties:\n" +
                "          c:\n" +
                "            type: integer\n" +
                "            format: int32\n" +
                "    MultipleSub2Bean:\n" +
                "      type: object\n" +
                "      description: MultipleSub2Bean\n" +
                "      allOf:\n" +
                "      - $ref: \"#/components/schemas/MultipleBaseBean\"\n" +
                "      - type: object\n" +
                "        properties:\n" +
                "          d:\n" +
                "            type: integer\n" +
                "            format: int32\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Test Schema AdditionalProperties annotations")
    public void testSchemaAdditionalProperties() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(SchemaAdditionalPropertiesResource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /arraySchemaImpl:\n" +
                "    get:\n" +
                "      operationId: arraySchemaImpl\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: voila!\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: object\n" +
                "                additionalProperties:\n" +
                "                  type: array\n" +
                "                  items:\n" +
                "                    $ref: \"#/components/schemas/Pet\"\n" +
                "  /fromtResponseType:\n" +
                "    get:\n" +
                "      operationId: fromtResponseType\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                type: object\n" +
                "                additionalProperties:\n" +
                "                  type: array\n" +
                "                  items:\n" +
                "                    $ref: \"#/components/schemas/Pet\"\n" +
                "  /schemaImpl:\n" +
                "    get:\n" +
                "      operationId: schemaImpl\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: voila!\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: object\n" +
                "                additionalProperties:\n" +
                "                  $ref: \"#/components/schemas/Pet\"\n" +
                "  /schemaNotImpl:\n" +
                "    get:\n" +
                "      operationId: schemaNotImpl\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: voila!\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: object\n" +
                "                additionalProperties:\n" +
                "                  $ref: \"#/components/schemas/Pet\"\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Pet:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        foo:\n" +
                "          type: string\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Test Schema AdditionalProperties annotations")
    public void testSchemaAdditionalPropertiesBoolean() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI(new OpenAPI()).schemaResolution(Schema.SchemaResolution.ALL_OF);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(SchemaAdditionalPropertiesBooleanResource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test:\n" +
                "    get:\n" +
                "      operationId: test\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/Pet\"\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Bar:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        foo:\n" +
                "          type: string\n" +
                "    Pet:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        bar:\n" +
                "          allOf:\n" +
                "          - additionalProperties:\n" +
                "              $ref: \"#/components/schemas/Bar\"\n" +
                "          - $ref: \"#/components/schemas/Bar\"\n" +
                "        vbar:\n" +
                "          allOf:\n" +
                "          - additionalProperties: false\n" +
                "          - $ref: \"#/components/schemas/Bar\"\n" +
                "      additionalProperties: false\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "Test ArraySchema implementation annotations")
    public void testArraySchemaImplementation() {
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI31(true).openAPI(new OpenAPI());
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(ArraySchemaImplementationResource.class);
        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /test:\n" +
                "    get:\n" +
                "      operationId: test\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/Pet\"\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Pet:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        cars:\n" +
                "          type: array\n" +
                "          items:\n" +
                "            type: integer\n" +
                "            format: int32\n" +
                "            description: A house in a street\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test(description = "Responses schema resolved from return type")
    public void testResponseReturnType() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(ResponseReturnTypeResource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /sample/{id}:\n" +
                "    get:\n" +
                "      summary: Find by id\n" +
                "      description: Find by id operation\n" +
                "      operationId: find\n" +
                "      parameters:\n" +
                "      - name: id\n" +
                "        in: path\n" +
                "        description: ID\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: Ok\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/TestDTO\"\n" +
                "        \"201\":\n" +
                "          description: \"201\"\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/TestDTO\"\n" +
                "        \"204\":\n" +
                "          description: No Content\n" +
                "          content:\n" +
                "            application/json: {}\n" +
                "  /sample/{id}/default:\n" +
                "    get:\n" +
                "      summary: Find by id (default)\n" +
                "      description: Find by id operation (default)\n" +
                "      operationId: findDefault\n" +
                "      parameters:\n" +
                "      - name: id\n" +
                "        in: path\n" +
                "        description: ID\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/TestDTO\"\n" +
                "components:\n" +
                "  schemas:\n" +
                "    TestDTO:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        foo:\n" +
                "          type: string";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Responses Default Status")
    public void testResponseDefaultStatus() {
        SwaggerConfiguration config = new SwaggerConfiguration().defaultResponseCode("200");
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(DefaultResponseResource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      operationId: test\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                type: string\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test
    public void test4412PathWildcards() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket4412Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/sws/{var}:\n" +
                "    get:\n" +
                "      operationId: getCart\n" +
                "      parameters:\n" +
                "      - name: var\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          pattern: .*\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            text/xml:\n" +
                "              schema:\n" +
                "                type: array\n" +
                "                items:\n" +
                "                  type: string";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test
    public void testOas31Petstore() {
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI31(true).openAPI(new OpenAPI());
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(PetResource.class);
        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /pet:\n" +
                "    put:\n" +
                "      summary: Update an existing pet\n" +
                "      operationId: updatePet\n" +
                "      requestBody:\n" +
                "        description: Pet object that needs to be added to the store\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Pet\"\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        \"400\":\n" +
                "          description: Invalid ID supplied\n" +
                "        \"404\":\n" +
                "          description: Pet not found\n" +
                "        \"405\":\n" +
                "          description: Validation exception\n" +
                "    post:\n" +
                "      summary: Add a new pet to the store\n" +
                "      operationId: addPet\n" +
                "      requestBody:\n" +
                "        description: Pet object that needs to be added to the store\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Pet\"\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Pet\"\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        \"405\":\n" +
                "          description: Invalid input\n" +
                "  /pet/bodyid:\n" +
                "    post:\n" +
                "      summary: Add a new pet to the store passing an integer with generic parameter\n" +
                "        annotation\n" +
                "      operationId: addPetByInteger\n" +
                "      requestBody:\n" +
                "        description: Pet object that needs to be added to the store\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: integer\n" +
                "              format: int32\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              type: integer\n" +
                "              format: int32\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        \"405\":\n" +
                "          description: Invalid input\n" +
                "  /pet/bodyidnoannotation:\n" +
                "    post:\n" +
                "      summary: Add a new pet to the store passing an integer without parameter annotation\n" +
                "      operationId: addPetByIntegerNoAnnotation\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: integer\n" +
                "              format: int32\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              type: integer\n" +
                "              format: int32\n" +
                "      responses:\n" +
                "        \"405\":\n" +
                "          description: Invalid input\n" +
                "  /pet/bodynoannotation:\n" +
                "    post:\n" +
                "      summary: Add a new pet to the store no annotation\n" +
                "      operationId: addPetNoAnnotation\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Pet\"\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Pet\"\n" +
                "      responses:\n" +
                "        \"405\":\n" +
                "          description: Invalid input\n" +
                "  /pet/findByStatus:\n" +
                "    get:\n" +
                "      summary: Finds Pets by status\n" +
                "      description: Multiple status values can be provided with comma separated strings\n" +
                "      operationId: findPetsByStatus\n" +
                "      parameters:\n" +
                "      - name: status\n" +
                "        in: query\n" +
                "        description: Status values that need to be considered for filter\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      - name: skip\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "      - name: limit\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "      responses:\n" +
                "        default:\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/Pet\"\n" +
                "        \"400\":\n" +
                "          description: Invalid status value\n" +
                "  /pet/findByTags:\n" +
                "    get:\n" +
                "      summary: Finds Pets by tags\n" +
                "      description: \"Multiple tags can be provided with comma separated strings. Use\\\n" +
                "        \\ tag1, tag2, tag3 for testing.\"\n" +
                "      operationId: findPetsByTags\n" +
                "      parameters:\n" +
                "      - name: tags\n" +
                "        in: query\n" +
                "        description: Tags to filter by\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: Pets matching criteria\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/Pet\"\n" +
                "        \"400\":\n" +
                "          description: Invalid tag value\n" +
                "      deprecated: true\n" +
                "  /pet/{petId}:\n" +
                "    get:\n" +
                "      summary: Find pet by ID\n" +
                "      description: Returns a pet when 0 < ID <= 10.  ID > 10 or nonintegers will simulate\n" +
                "        API error conditions\n" +
                "      operationId: getPetById\n" +
                "      parameters:\n" +
                "      - name: petId\n" +
                "        in: path\n" +
                "        description: ID of pet that needs to be fetched\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: The pet\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/Pet\"\n" +
                "            application/xml:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/Pet\"\n" +
                "        \"400\":\n" +
                "          description: Invalid ID supplied\n" +
                "        \"404\":\n" +
                "          description: Pet not found\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Bar:\n" +
                "      type: object\n" +
                "      deprecated: true\n" +
                "      description: Bar\n" +
                "      properties:\n" +
                "        foo:\n" +
                "          type: string\n" +
                "          const: bar\n" +
                "        bar:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "          exclusiveMaximum: 4\n" +
                "        foobar:\n" +
                "          type:\n" +
                "          - integer\n" +
                "          - string\n" +
                "          format: int32\n" +
                "    Category:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        name:\n" +
                "          type: string\n" +
                "      xml:\n" +
                "        name: Category\n" +
                "    Foo:\n" +
                "      type: object\n" +
                "      deprecated: true\n" +
                "      description: Foo\n" +
                "      properties:\n" +
                "        foo:\n" +
                "          type: string\n" +
                "          const: foo\n" +
                "        bar:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "          exclusiveMaximum: 2\n" +
                "        foobar:\n" +
                "          type:\n" +
                "          - integer\n" +
                "          - string\n" +
                "          - object\n" +
                "          format: int32\n" +
                "    IfSchema:\n" +
                "      type: object\n" +
                "      deprecated: true\n" +
                "      description: if schema\n" +
                "      properties:\n" +
                "        foo:\n" +
                "          type: string\n" +
                "          const: foo\n" +
                "        bar:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "          exclusiveMaximum: 2\n" +
                "        foobar:\n" +
                "          type:\n" +
                "          - integer\n" +
                "          - string\n" +
                "          - object\n" +
                "          format: int32\n" +
                "    Pet:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        category:\n" +
                "          $ref: \"#/components/schemas/Category\"\n" +
                "        name:\n" +
                "          type: string\n" +
                "        photoUrls:\n" +
                "          type: array\n" +
                "          items:\n" +
                "            type: string\n" +
                "            xml:\n" +
                "              name: photoUrl\n" +
                "          xml:\n" +
                "            wrapped: true\n" +
                "        tags:\n" +
                "          type: array\n" +
                "          items:\n" +
                "            $ref: \"#/components/schemas/Tag\"\n" +
                "          xml:\n" +
                "            wrapped: true\n" +
                "        status:\n" +
                "          type: string\n" +
                "          if:\n" +
                "            $ref: \"#/components/schemas/IfSchema\"\n" +
                "          $id: idtest\n" +
                "          description: pet status in the store\n" +
                "          enum:\n" +
                "          - \"available,pending,sold\"\n" +
                "      xml:\n" +
                "        name: Pet\n" +
                "    Tag:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        name:\n" +
                "          type: string\n" +
                "        annotated:\n" +
                "          $ref: \"#/components/schemas/Category\"\n" +
                "          description: child description\n" +
                "          properties:\n" +
                "            foo:\n" +
                "              $ref: \"#/components/schemas/Foo\"\n" +
                "            bar:\n" +
                "              $ref: \"#/components/schemas/Bar\"\n" +
                "      xml:\n" +
                "        name: Tag\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void test31RefSiblings() {
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI31(true).openAPI(new OpenAPI());
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(TagResource.class);
        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /tag/tag:\n" +
                "    get:\n" +
                "      operationId: getTag\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/SimpleTag\"\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Foo:\n" +
                "      type: object\n" +
                "      deprecated: true\n" +
                "      description: Foo\n" +
                "      properties:\n" +
                "        foo:\n" +
                "          type: string\n" +
                "          const: foo\n" +
                "        bar:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "          exclusiveMaximum: 2\n" +
                "        foobar:\n" +
                "          type:\n" +
                "          - integer\n" +
                "          - string\n" +
                "          - object\n" +
                "          format: int32\n" +
                "    SimpleTag:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        annotated:\n" +
                "          $ref: \"#/components/schemas/SimpleCategory\"\n" +
                "          description: child description\n" +
                "          properties:\n" +
                "            foo:\n" +
                "              $ref: \"#/components/schemas/Foo\"\n" +
                "    SimpleCategory: {}\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void testSiblings() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));

        OpenAPI openAPI = reader.read(SiblingsResource.class);
        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /test:\n" +
                "    get:\n" +
                "      operationId: getCart\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/Pet\"\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Category:\n" +
                "      type: object\n" +
                "      description: parent\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "    Pet:\n" +
                "      type: object\n" +
                "      description: Pet\n" +
                "      properties:\n" +
                "        category:\n" +
                "          $ref: \"#/components/schemas/Category\"\n" +
                "          description: child\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void testSiblingsOnResource() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));

        OpenAPI openAPI = reader.read(SiblingsResourceSimple.class);
        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /test:\n" +
                "    get:\n" +
                "      operationId: getCart\n" +
                "      responses:\n" +
                "        \"300\":\n" +
                "          description: aaa\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/PetSimple\"\n" +
                "                description: resource pet\n" +
                "                readOnly: true\n" +
                "  /test/impl:\n" +
                "    get:\n" +
                "      operationId: getCartImpl\n" +
                "      responses:\n" +
                "        \"300\":\n" +
                "          description: aaa\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/PetSimple\"\n" +
                "                description: resource pet\n" +
                "                readOnly: true\n" +
                "components:\n" +
                "  schemas:\n" +
                "    PetSimple:\n" +
                "      description: Pet\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void testSiblingsOnResourceResponse() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));

        OpenAPI openAPI = reader.read(SiblingsResourceResponse.class);
        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /test:\n" +
                "    get:\n" +
                "      operationId: getCart\n" +
                "      responses:\n" +
                "        \"300\":\n" +
                "          description: aaa\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/PetSimple\"\n" +
                "                description: resource pet\n" +
                "                readOnly: true\n" +
                "            application/xml:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/PetSimple\"\n" +
                "                description: resource pet xml\n" +
                "                readOnly: true\n" +
                "  /test/impl:\n" +
                "    get:\n" +
                "      operationId: getCartImpl\n" +
                "      responses:\n" +
                "        \"300\":\n" +
                "          description: aaa\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/PetSimple\"\n" +
                "                description: resource pet\n" +
                "                readOnly: true\n" +
                "            application/xml:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/PetSimple\"\n" +
                "                description: resource pet xml\n" +
                "                readOnly: true\n" +
                "components:\n" +
                "  schemas:\n" +
                "    PetSimple:\n" +
                "      description: Pet\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void testSiblingsOnResourceRequestBody() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));

        OpenAPI openAPI = reader.read(SiblingsResourceRequestBody.class);
        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /test/bodyimpl:\n" +
                "    get:\n" +
                "      operationId: getBodyImpl\n" +
                "      requestBody:\n" +
                "        description: aaa\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/PetSimple\"\n" +
                "              description: resource pet\n" +
                "              writeOnly: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/bodyimplparam:\n" +
                "    get:\n" +
                "      operationId: getBodyImplParam\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/PetSimple\"\n" +
                "              description: resource pet\n" +
                "              writeOnly: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    PetSimple:\n" +
                "      description: Pet\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void testSiblingsOnResourceRequestBodyMultiple() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));

        OpenAPI openAPI = reader.read(SiblingsResourceRequestBodyMultiple.class);
        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /test/bodyimpl:\n" +
                "    get:\n" +
                "      operationId: getBodyImpl\n" +
                "      requestBody:\n" +
                "        description: aaa\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/PetSimple\"\n" +
                "              description: resource pet\n" +
                "              writeOnly: true\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/PetSimple\"\n" +
                "              description: resource pet xml\n" +
                "              writeOnly: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/bodyimplparam:\n" +
                "    get:\n" +
                "      operationId: getBodyImplParam\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/PetSimple\"\n" +
                "              description: resource pet\n" +
                "              writeOnly: true\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/PetSimple\"\n" +
                "              description: resource pet xml\n" +
                "              writeOnly: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/bodyparam:\n" +
                "    get:\n" +
                "      operationId: getBodyParam\n" +
                "      requestBody:\n" +
                "        description: test\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/PetSimple\"\n" +
                "              description: resource pet\n" +
                "              writeOnly: true\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/PetSimple\"\n" +
                "              description: resource pet xml\n" +
                "              writeOnly: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    PetSimple:\n" +
                "      description: Pet\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void testSiblingsOnProperty() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));
        Set<Class<?>> classes = new HashSet<>(Arrays.asList(SiblingPropResource.class, WebHookResource.class));
        OpenAPI openAPI = reader.read(classes);
        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /pet:\n" +
                "    put:\n" +
                "      tags:\n" +
                "      - pet\n" +
                "      summary: Update an existing pet\n" +
                "      operationId: updatePet\n" +
                "      requestBody:\n" +
                "        description: Pet object that needs to be updated in the store\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Pet\"\n" +
                "              description: A Pet in JSON Format\n" +
                "              required:\n" +
                "              - id\n" +
                "              writeOnly: true\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Pet\"\n" +
                "              description: A Pet in XML Format\n" +
                "              required:\n" +
                "              - id\n" +
                "              writeOnly: true\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: Successful operation\n" +
                "          content:\n" +
                "            application/xml:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/Pet\"\n" +
                "                description: A Pet in XML Format\n" +
                "                readOnly: true\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/Pet\"\n" +
                "                description: A Pet in JSON Format\n" +
                "                readOnly: true\n" +
                "        \"400\":\n" +
                "          description: Invalid ID supplied\n" +
                "        \"404\":\n" +
                "          description: Pet not found\n" +
                "        \"405\":\n" +
                "          description: Validation exception\n" +
                "      security:\n" +
                "      - petstore_auth:\n" +
                "        - write:pets\n" +
                "        - read:pets\n" +
                "      - mutual_tls: []\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Category:\n" +
                "      type: object\n" +
                "      description: parent\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "    Pet:\n" +
                "      type: object\n" +
                "      description: Pet\n" +
                "      properties:\n" +
                "        category:\n" +
                "          $ref: \"#/components/schemas/Category\"\n" +
                "          description: child\n" +
                "webhooks:\n" +
                "  newPet:\n" +
                "    post:\n" +
                "      requestBody:\n" +
                "        description: Information about a new pet in the system\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Pet\"\n" +
                "              description: Webhook Pet\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: Return a 200 status to indicate that the data was received\n" +
                "            successfully\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void testMisc31() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));
        Set<Class<?>> classes = new HashSet<>(Arrays.asList(Misc31Resource.class));
        OpenAPI openAPI = reader.read(classes);
        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /pet:\n" +
                "    put:\n" +
                "      operationId: updatePet\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/ModelWithOAS31Stuff\"\n" +
                "            application/xml:\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/ModelWithOAS31Stuff\"\n" +
                "components:\n" +
                "  schemas:\n" +
                "    ModelWithOAS31Stuff:\n" +
                "      type: object\n" +
                "      $comment: Random comment at schema level\n" +
                "      $id: http://yourdomain.com/schemas/myschema.json\n" +
                "      description: this is model for testing OAS 3.1 resolving\n" +
                "      properties:\n" +
                "        randomList:\n" +
                "          type: array\n" +
                "          contains:\n" +
                "            type: string\n" +
                "          items:\n" +
                "            type: string\n" +
                "          maxContains: 10\n" +
                "          minContains: 1\n" +
                "          prefixItems:\n" +
                "          - type: string\n" +
                "          unevaluatedItems:\n" +
                "            type: number\n" +
                "        status:\n" +
                "          type:\n" +
                "          - string\n" +
                "          - number\n" +
                "        intValue:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "          $anchor: intValue\n" +
                "          $comment: comment at schema property level\n" +
                "          exclusiveMaximum: 100\n" +
                "          exclusiveMinimum: 1\n" +
                "        text:\n" +
                "          type: string\n" +
                "          contentEncoding: plan/text\n" +
                "          contentMediaType: base64\n" +
                "        encodedString:\n" +
                "          type: string\n" +
                "          contentMediaType: application/jwt\n" +
                "          contentSchema:\n" +
                "            $ref: \"#/components/schemas/MultipleBaseBean\"\n" +
                "        address:\n" +
                "          $ref: \"#/components/schemas/Address\"\n" +
                "        client:\n" +
                "          type: string\n" +
                "          dependentSchemas:\n" +
                "            creditCard:\n" +
                "              $ref: \"#/components/schemas/CreditCard\"\n" +
                "    MultipleBaseBean:\n" +
                "      type: object\n" +
                "      description: MultipleBaseBean\n" +
                "      properties:\n" +
                "        beanType:\n" +
                "          type: string\n" +
                "        a:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        b:\n" +
                "          type: string\n" +
                "    MultipleSub1Bean:\n" +
                "      allOf:\n" +
                "      - $ref: \"#/components/schemas/MultipleBaseBean\"\n" +
                "      - type: object\n" +
                "        properties:\n" +
                "          c:\n" +
                "            type: integer\n" +
                "            format: int32\n" +
                "      description: MultipleSub1Bean\n" +
                "    MultipleSub2Bean:\n" +
                "      allOf:\n" +
                "      - $ref: \"#/components/schemas/MultipleBaseBean\"\n" +
                "      - type: object\n" +
                "        properties:\n" +
                "          d:\n" +
                "            type: integer\n" +
                "            format: int32\n" +
                "      description: MultipleSub2Bean\n" +
                "    Address:\n" +
                "      type: object\n" +
                "      if:\n" +
                "        $ref: \"#/components/schemas/AnnotatedCountry\"\n" +
                "      then:\n" +
                "        $ref: \"#/components/schemas/PostalCodeNumberPattern\"\n" +
                "      else:\n" +
                "        $ref: \"#/components/schemas/PostalCodePattern\"\n" +
                "      dependentRequired:\n" +
                "        street:\n" +
                "        - country\n" +
                "      properties:\n" +
                "        street:\n" +
                "          type: string\n" +
                "        country:\n" +
                "          type: string\n" +
                "          enum:\n" +
                "          - United States of America\n" +
                "          - Canada\n" +
                "      propertyNames:\n" +
                "        pattern: \"^[A-Za-z_][A-Za-z0-9_]*$\"\n" +
                "    AnnotatedCountry:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        country:\n" +
                "          const: United States\n" +
                "    CreditCard:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        billingAddress:\n" +
                "          type: string\n" +
                "    PostalCodeNumberPattern:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        postalCode:\n" +
                "          pattern: \"[0-9]{5}(-[0-9]{4})?\"\n" +
                "    PostalCodePattern:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        postalCode:\n" +
                "          pattern: \"[A-Z][0-9][A-Z] [0-9][A-Z][0-9]\"\n" +
                "    PropertyNamesPattern:\n" +
                "      pattern: \"^[A-Za-z_][A-Za-z0-9_]*$\"\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void test4446CyclicProp() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket4446Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/test:\n" +
                "    get:\n" +
                "      operationId: getCart\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/MyPojo\"\n" +
                "components:\n" +
                "  schemas:\n" +
                "    MyPojo:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        someStrings:\n" +
                "          type: array\n" +
                "          items:\n" +
                "            type: string\n" +
                "        morePojos:\n" +
                "          type: array\n" +
                "          items:\n" +
                "            $ref: \"#/components/schemas/MyPojo\"\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test
    public void testParameterMaximumValue() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));

        OpenAPI openAPI = reader.read(ParameterMaximumValueResource.class);
        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /test/{petId}:\n" +
                "    get:\n" +
                "      operationId: getPetById\n" +
                "      parameters:\n" +
                "      - name: petId\n" +
                "        in: path\n" +
                "        description: ID of pet that needs to be fetched\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "          exclusiveMaximum: 10\n" +
                "          exclusiveMinimum: 1\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void test4483Response() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket4483Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "tags:\n" +
                "- name: Dummy\n" +
                "  description: Dummy resource for testing setup\n" +
                "paths:\n" +
                "  /test:\n" +
                "    get:\n" +
                "      tags:\n" +
                "      - Dummy\n" +
                "      description: Dummy GET\n" +
                "      operationId: dummy\n" +
                "      responses:\n" +
                "        \"401\":\n" +
                "          description: Authentication is required\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: array\n" +
                "                items:\n" +
                "                  $ref: \"#/components/schemas/LocalizedError\"\n" +
                "        \"200\":\n" +
                "          description: test\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: object\n" +
                "                additionalProperties:\n" +
                "                  type: boolean\n" +
                "  /test/opresp:\n" +
                "    get:\n" +
                "      tags:\n" +
                "      - Dummy\n" +
                "      operationId: dummyopresp\n" +
                "      responses:\n" +
                "        \"401\":\n" +
                "          description: Authentication is required\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: array\n" +
                "                items:\n" +
                "                  $ref: \"#/components/schemas/LocalizedError\"\n" +
                "        \"200\":\n" +
                "          description: Dummy GET opresp\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: object\n" +
                "                additionalProperties:\n" +
                "                  type: boolean\n" +
                "  /test/oprespnodesc:\n" +
                "    get:\n" +
                "      tags:\n" +
                "      - Dummy\n" +
                "      operationId: oprespnodesc\n" +
                "      responses:\n" +
                "        \"401\":\n" +
                "          description: Authentication is required\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: array\n" +
                "                items:\n" +
                "                  $ref: \"#/components/schemas/LocalizedError\"\n" +
                "components:\n" +
                "  schemas:\n" +
                "    LocalizedError:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        code:\n" +
                "          type: string\n" +
                "        message:\n" +
                "          type: string\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "openAPIVersion")
    public void testOpenAPIVersion() {
        SwaggerConfiguration config = new SwaggerConfiguration().openAPIVersion("3.0.4");
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(DefaultResponseResource.class);
        String yaml = "openapi: 3.0.4\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      operationId: test\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                type: string\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Constraints annotations with groups - Inline")
    public void testTicket4804Inline() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration().schemaResolution(Schema.SchemaResolution.INLINE);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4804Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/barcart:\n" +
                "    put:\n" +
                "      operationId: barCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              required:\n" +
                "              - notNullcartDetails\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                pageSize:\n" +
                "                  type: integer\n" +
                "                  format: int32\n" +
                "                cartDetails:\n" +
                "                  required:\n" +
                "                  - description\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "                notNullcartDetails:\n" +
                "                  required:\n" +
                "                  - description\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/foocart:\n" +
                "    put:\n" +
                "      operationId: fooCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              required:\n" +
                "              - cartDetails\n" +
                "              - notNullcartDetails\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                pageSize:\n" +
                "                  type: integer\n" +
                "                  format: int32\n" +
                "                cartDetails:\n" +
                "                  required:\n" +
                "                  - description\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "                notNullcartDetails:\n" +
                "                  required:\n" +
                "                  - description\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/createcart:\n" +
                "    post:\n" +
                "      operationId: postCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              required:\n" +
                "              - notNullcartDetails\n" +
                "              - pageSize\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                pageSize:\n" +
                "                  type: integer\n" +
                "                  format: int32\n" +
                "                cartDetails:\n" +
                "                  required:\n" +
                "                  - description\n" +
                "                  - name\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "                notNullcartDetails:\n" +
                "                  required:\n" +
                "                  - description\n" +
                "                  - name\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/updatecart:\n" +
                "    put:\n" +
                "      operationId: putCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              required:\n" +
                "              - notNullcartDetails\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                pageSize:\n" +
                "                  type: integer\n" +
                "                  format: int32\n" +
                "                cartDetails:\n" +
                "                  required:\n" +
                "                  - description\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "                notNullcartDetails:\n" +
                "                  required:\n" +
                "                  - description\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Cart:\n" +
                "      required:\n" +
                "      - notNullcartDetails\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        pageSize:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        cartDetails:\n" +
                "          required:\n" +
                "          - description\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            name:\n" +
                "              type: string\n" +
                "            description:\n" +
                "              type: string\n" +
                "        notNullcartDetails:\n" +
                "          required:\n" +
                "          - description\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            name:\n" +
                "              type: string\n" +
                "            description:\n" +
                "              type: string\n" +
                "    CartDetails:\n" +
                "      required:\n" +
                "      - description\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        name:\n" +
                "          type: string\n" +
                "        description:\n" +
                "          type: string\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "Constraints annotations with groups - Default")
    public void testTicket4804Default() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration();
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4804Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/barcart:\n" +
                "    put:\n" +
                "      operationId: barCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Cart\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/foocart:\n" +
                "    put:\n" +
                "      operationId: fooCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Cart\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/createcart:\n" +
                "    post:\n" +
                "      operationId: postCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Cart\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/updatecart:\n" +
                "    put:\n" +
                "      operationId: putCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Cart\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Cart:\n" +
                "      required:\n" +
                "      - notNullcartDetails\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        pageSize:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        cartDetails:\n" +
                "          $ref: \"#/components/schemas/CartDetails\"\n" +
                "        notNullcartDetails:\n" +
                "          $ref: \"#/components/schemas/CartDetails\"\n" +
                "    CartDetails:\n" +
                "      required:\n" +
                "      - description\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        name:\n" +
                "          type: string\n" +
                "        description:\n" +
                "          type: string\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "Constraints annotations with groups - Default NotBlank")
    public void testTicket4804DefaultNotBlank() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration();
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4804NotBlankResource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/barcart:\n" +
                "    put:\n" +
                "      operationId: barCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Cart\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/createcart:\n" +
                "    post:\n" +
                "      operationId: postCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Cart\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/updatecart:\n" +
                "    put:\n" +
                "      operationId: putCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Cart\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Cart:\n" +
                "      required:\n" +
                "      - notNullcartDetails\n" +
                "      - pageSizes\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        pageSizes:\n" +
                "          type: array\n" +
                "          items:\n" +
                "            type: integer\n" +
                "            format: int32\n" +
                "        notNullcartDetails:\n" +
                "          $ref: \"#/components/schemas/CartDetails\"\n" +
                "    CartDetails:\n" +
                "      required:\n" +
                "      - description\n" +
                "      - name\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        name:\n" +
                "          minLength: 1\n" +
                "          type: string\n" +
                "        description:\n" +
                "          minItems: 1\n" +
                "          type: array\n" +
                "          items:\n" +
                "            type: string\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "Constraints annotations with groups - Always")
    public void testTicket4804Always() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration().groupsValidationStrategy(Configuration.GroupsValidationStrategy.ALWAYS);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4804Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/barcart:\n" +
                "    put:\n" +
                "      operationId: barCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Cart\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/foocart:\n" +
                "    put:\n" +
                "      operationId: fooCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Cart\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/createcart:\n" +
                "    post:\n" +
                "      operationId: postCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Cart\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/updatecart:\n" +
                "    put:\n" +
                "      operationId: putCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Cart\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Cart:\n" +
                "      required:\n" +
                "      - cartDetails\n" +
                "      - notNullcartDetails\n" +
                "      - pageSize\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        pageSize:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        cartDetails:\n" +
                "          $ref: \"#/components/schemas/CartDetails\"\n" +
                "        notNullcartDetails:\n" +
                "          $ref: \"#/components/schemas/CartDetails\"\n" +
                "    CartDetails:\n" +
                "      required:\n" +
                "      - description\n" +
                "      - name\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        name:\n" +
                "          type: string\n" +
                "        description:\n" +
                "          type: string\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "Constraints annotations with groups - Never")
    public void testTicket4804Never() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration().groupsValidationStrategy(Configuration.GroupsValidationStrategy.NEVER);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4804Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/barcart:\n" +
                "    put:\n" +
                "      operationId: barCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Cart\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/foocart:\n" +
                "    put:\n" +
                "      operationId: fooCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Cart\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/createcart:\n" +
                "    post:\n" +
                "      operationId: postCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Cart\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/updatecart:\n" +
                "    put:\n" +
                "      operationId: putCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Cart\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Cart:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        pageSize:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        cartDetails:\n" +
                "          $ref: \"#/components/schemas/CartDetails\"\n" +
                "        notNullcartDetails:\n" +
                "          $ref: \"#/components/schemas/CartDetails\"\n" +
                "    CartDetails:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        name:\n" +
                "          type: string\n" +
                "        description:\n" +
                "          type: string\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "Constraints annotations with groups - NeverNoContext")
    public void testTicket4804NeverNoContext() {
        ModelConverters.reset();
        SwaggerConfiguration config =
                new SwaggerConfiguration()
                        .groupsValidationStrategy(Configuration.GroupsValidationStrategy.NEVER_IF_NO_CONTEXT)
                        .schemaResolution(Schema.SchemaResolution.INLINE);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4804Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/barcart:\n" +
                "    put:\n" +
                "      operationId: barCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                pageSize:\n" +
                "                  type: integer\n" +
                "                  format: int32\n" +
                "                cartDetails:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "                notNullcartDetails:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/foocart:\n" +
                "    put:\n" +
                "      operationId: fooCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              required:\n" +
                "              - cartDetails\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                pageSize:\n" +
                "                  type: integer\n" +
                "                  format: int32\n" +
                "                cartDetails:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "                notNullcartDetails:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/createcart:\n" +
                "    post:\n" +
                "      operationId: postCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              required:\n" +
                "              - pageSize\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                pageSize:\n" +
                "                  type: integer\n" +
                "                  format: int32\n" +
                "                cartDetails:\n" +
                "                  required:\n" +
                "                  - name\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "                notNullcartDetails:\n" +
                "                  required:\n" +
                "                  - name\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/updatecart:\n" +
                "    put:\n" +
                "      operationId: putCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                pageSize:\n" +
                "                  type: integer\n" +
                "                  format: int32\n" +
                "                cartDetails:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "                notNullcartDetails:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Cart:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        pageSize:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        cartDetails:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            name:\n" +
                "              type: string\n" +
                "            description:\n" +
                "              type: string\n" +
                "        notNullcartDetails:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            name:\n" +
                "              type: string\n" +
                "            description:\n" +
                "              type: string\n" +
                "    CartDetails:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        name:\n" +
                "          type: string\n" +
                "        description:\n" +
                "          type: string\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "Constraints annotations with groups - Processor")
    public void testTicket4804Processor() {
        ModelConverters.reset();
        SwaggerConfiguration config =
                new SwaggerConfiguration()
                        .validatorProcessorClass(Ticket4804ProcessorResource.CustomValidatorProcessor.class.getName())
                        .schemaResolution(Schema.SchemaResolution.INLINE);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4804ProcessorResource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/barcart:\n" +
                "    put:\n" +
                "      operationId: barCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                pageSize:\n" +
                "                  type: integer\n" +
                "                  format: int32\n" +
                "                cartDetails:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "                notNullcartDetails:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/foocart:\n" +
                "    put:\n" +
                "      operationId: fooCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                pageSize:\n" +
                "                  type: integer\n" +
                "                  format: int32\n" +
                "                cartDetails:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "                notNullcartDetails:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/createcart:\n" +
                "    post:\n" +
                "      operationId: postCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                pageSize:\n" +
                "                  type: integer\n" +
                "                  format: int32\n" +
                "                cartDetails:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "                notNullcartDetails:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/updatecart:\n" +
                "    put:\n" +
                "      operationId: putCart\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              required:\n" +
                "              - cartDetails\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                pageSize:\n" +
                "                  type: integer\n" +
                "                  format: int32\n" +
                "                cartDetails:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "                notNullcartDetails:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    name:\n" +
                "                      type: string\n" +
                "                    description:\n" +
                "                      type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Cart:\n" +
                "      required:\n" +
                "      - cartDetails\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        pageSize:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        cartDetails:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            name:\n" +
                "              type: string\n" +
                "            description:\n" +
                "              type: string\n" +
                "        notNullcartDetails:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            name:\n" +
                "              type: string\n" +
                "            description:\n" +
                "              type: string\n" +
                "    CartDetails:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        name:\n" +
                "          type: string\n" +
                "        description:\n" +
                "          type: string\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test
    public void shouldIncludeOnlyNonGroupedJakartaValidatedFieldsAsMandatoryByDefault() {
        ModelConverters.reset();
        ResolvedSchema schema = ModelConverters.getInstance(false).resolveAsResolvedSchema(new AnnotatedType().type(Ticket4804CustomClass.class));
        String expectedYaml = "schema:\n" +
                "  required:\n" +
                "  - nonGroupValidatedField\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    nonGroupValidatedField:\n" +
                "      type: string\n" +
                "    singleGroupValidatedField:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "    multipleGroupValidatedField:\n" +
                "      type: number\n" +
                "    otherGroupValidatedField:\n" +
                "      type: string\n" +
                "    singleGroupValidatedField2:\n" +
                "      type: string\n" +
                "referencedSchemas:\n" +
                "  Ticket4804CustomClass:\n" +
                "    required:\n" +
                "    - nonGroupValidatedField\n" +
                "    type: object\n" +
                "    properties:\n" +
                "      nonGroupValidatedField:\n" +
                "        type: string\n" +
                "      singleGroupValidatedField:\n" +
                "        type: integer\n" +
                "        format: int32\n" +
                "      multipleGroupValidatedField:\n" +
                "        type: number\n" +
                "      otherGroupValidatedField:\n" +
                "        type: string\n" +
                "      singleGroupValidatedField2:\n" +
                "        type: string\n";
        SerializationMatchers.assertEqualsToYaml(schema, expectedYaml);
        ModelConverters.reset();
    }

    @Test(description = "test schema.minLength applied")
    public void testTicket4859() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration();
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4859Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/minlength:\n" +
                "    put:\n" +
                "      operationId: minlength\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/Minlength\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Minlength:\n" +
                "      required:\n" +
                "      - name\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        name:\n" +
                "          maxLength: 19\n" +
                "          minLength: 12\n" +
                "          type: string\n" +
                "          example: \"4242424242424242\"\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "test default value type")
    public void testTicket4879() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI31(true);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4879Resource.class);
        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /test/test:\n" +
                "    put:\n" +
                "      operationId: test\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: \"#/components/schemas/DefaultClass\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/testDefaultValueAnnotation:\n" +
                "    get:\n" +
                "      operationId: testDefault\n" +
                "      parameters:\n" +
                "      - name: myBool\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: boolean\n" +
                "          default: true\n" +
                "      - name: myInt\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "          default: 1\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /test/testsize:\n" +
                "    get:\n" +
                "      operationId: testSize\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              type: array\n" +
                "              items:\n" +
                "                type: string\n" +
                "              maxItems: 100\n" +
                "              minItems: 1\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    DefaultClass:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        name:\n" +
                "          type: boolean\n" +
                "          default: true\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "test explode FALSE")
    public void testTicket4065() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration();
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4065Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /bar:\n" +
                "    get:\n" +
                "      operationId: test\n" +
                "      parameters:\n" +
                "      - name: blub\n" +
                "        in: query\n" +
                "        explode: false\n" +
                "        schema:\n" +
                "          type: array\n" +
                "          items:\n" +
                "            type: integer\n" +
                "            format: int64\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json: {}\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "Extensions Tests OAS 3.1")
    public void testExtensionsOAS31() {
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI31(true);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4850Resource.class);
        assertNotNull(openAPI);

        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /bar:\n" +
                "    get:\n" +
                "      operationId: test\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/ExtensionsResource\"\n" +
                "components:\n" +
                "  schemas:\n" +
                "    ExtensionsResource:\n" +
                "      description: ExtensionsResource\n" +
                "      x-user:\n" +
                "        name: Josh\n" +
                "      user-extensions:\n" +
                "        lastName: Hart\n" +
                "        address: House";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }
    @Test(description = "Test model resolution for global path parameters with openAPI 3.1")
    public void testTicket4878() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI31(true);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4878Resource.class);
        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /{globalPathParam}/{localPathParam}:\n" +
                "    get:\n" +
                "      operationId: getMethod\n" +
                "      parameters:\n" +
                "      - name: globalPathParam\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "          $comment: 3.1 property for global path param\n" +
                "      - name: localPathParam\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "          $comment: 3.1 property for local path param\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "Test model resolution for global path parameters with openAPI 3.1")
    public void testTicket4907() {
        ModelConverters.reset();

        // openAPI31 true and no other config
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI31(true);
        Reader reader = new Reader(config);
        OpenAPI openAPI = reader.read(Ticket4878Resource.class);
        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /{globalPathParam}/{localPathParam}:\n" +
                "    get:\n" +
                "      operationId: getMethod\n" +
                "      parameters:\n" +
                "      - name: globalPathParam\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "          $comment: 3.1 property for global path param\n" +
                "      - name: localPathParam\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "          $comment: 3.1 property for local path param\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);

        // openAPI31 true and openAPI set
        config.setOpenAPI(new OpenAPI().openapi("3.1.1"));
        reader = new Reader(config);
        openAPI = reader.read(Ticket4878Resource.class);
        yaml = "openapi: 3.1.1\n" +
                "paths:\n" +
                "  /{globalPathParam}/{localPathParam}:\n" +
                "    get:\n" +
                "      operationId: getMethod\n" +
                "      parameters:\n" +
                "      - name: globalPathParam\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "          $comment: 3.1 property for global path param\n" +
                "      - name: localPathParam\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "          $comment: 3.1 property for local path param\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);

        // openAPI31 true and openAPIVersion set
        config.setOpenAPI(null);
        config.setOpenAPIVersion("3.1.1");
        reader = new Reader(config);
        openAPI = reader.read(Ticket4878Resource.class);
        yaml = "openapi: 3.1.1\n" +
                "paths:\n" +
                "  /{globalPathParam}/{localPathParam}:\n" +
                "    get:\n" +
                "      operationId: getMethod\n" +
                "      parameters:\n" +
                "      - name: globalPathParam\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "          $comment: 3.1 property for global path param\n" +
                "      - name: localPathParam\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "          $comment: 3.1 property for local path param\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "array property metadata is resolved from ArraySchema.arraySchema, items metadata from ArraySchema.schema")
    public void test4341ArraySchemaOtherAttributes() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(Ticket4341Resource.class);
        System.out.println(Json.pretty(openAPI));

        Schema userSchema = openAPI.getComponents().getSchemas().get("User");
        assertNotNull(userSchema, "User schema should be present");

        @SuppressWarnings("unchecked")
        Map<String, Schema> properties = userSchema.getProperties();
        assertNotNull(properties, "User properties should not be null");

        Schema metadataArray = properties.get("metadataArray");
        assertNotNull(metadataArray, "metadataArray property should be present");
        assertTrue(metadataArray instanceof ArraySchema, "metadataArray should be an ArraySchema");

        // Property-level assertions
        assertEquals(
                metadataArray.getDescription(),
                "array-level description",
                "Array property description should come from arraySchema, not items schema"
        );

        assertEquals(
                metadataArray.getDeprecated(),
                Boolean.TRUE,
                "Array property deprecated should come from arraySchema"
        );

        assertEquals(
                metadataArray.getReadOnly(),
                Boolean.TRUE,
                "Array property readOnly should be true from arraySchema.accessMode=READ_ONLY"
        );
        assertNotEquals(
                metadataArray.getWriteOnly(),
                Boolean.TRUE,
                "Array property writeOnly should not be true when accessMode=READ_ONLY"
        );

        // Item-level assertions

        ArraySchema metadataArraySchema = (ArraySchema) metadataArray;
        Schema items = metadataArraySchema.getItems();
        assertNotNull(items, "Items schema should not be null");

        assertEquals(
                items.getDescription(),
                "item-level description",
                "Items description should come from schema element of @ArraySchema"
        );

        assertNotEquals(
                items.getDeprecated(),
                Boolean.TRUE,
                "Items deprecated should not be true when schema.deprecated=false"
        );

        assertEquals(
                items.getWriteOnly(),
                Boolean.TRUE,
                "Items writeOnly should be true from schema.accessMode=WRITE_ONLY"
        );
        assertNotEquals(
                items.getReadOnly(),
                Boolean.TRUE,
                "Items readOnly should not be true when accessMode=WRITE_ONLY"
        );

        assertEquals(
                items.getFormat(),
                "email",
                "Items format should come from schema.format"
        );
    }
}
