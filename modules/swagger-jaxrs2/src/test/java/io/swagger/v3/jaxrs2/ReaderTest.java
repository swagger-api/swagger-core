package io.swagger.v3.jaxrs2;

import tools.jackson.databind.ObjectMapper;

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
import io.swagger.v3.jaxrs2.resources.Ticket5017Resource;
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
import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;
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
        assertEquals(operation.getSummary(), OPERATION_SUMMARY);
        assertEquals(operation.getDescription(), OPERATION_DESCRIPTION);
    }

    @Test(description = "scan methods")
    public void testCompleteReadClass() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(CompleteFieldsResource.class);
        Paths paths = openAPI.getPaths();
        assertEquals(paths.size(), PATHS_NUMBER);
        PathItem pathItem = paths.get(PATH_REF);
        assertNotNull(pathItem);
        assertNull(pathItem.getPost());
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        assertEquals(operation.getSummary(), OPERATION_SUMMARY);
        assertEquals(operation.getDescription(), OPERATION_DESCRIPTION);

        assertEquals(operation.getTags().size(), TAG_NUMBER);
        assertEquals(operation.getTags().get(0), EXAMPLE_TAG);
        assertEquals(operation.getTags().get(1), SECOND_TAG);

        ExternalDocumentation externalDocs = operation.getExternalDocs();
        assertEquals(externalDocs.getDescription(), EXTERNAL_DOCS_DESCRIPTION);
        assertEquals(externalDocs.getUrl(), EXTERNAL_DOCS_URL);
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
        assertEquals(operation.getSummary(), OPERATION_SUMMARY);
        assertEquals(operation.getDescription(), OPERATION_DESCRIPTION);
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
        assertEquals(securitySchemes.size(), SECURITY_SCHEMAS);
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
        assertEquals(operation.getTags().size(), 6);
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
        assertEquals(operation.getServers().size(), 5);
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
        assertEquals(responses.size(), RESPONSES_NUMBER);

        ApiResponse apiResponse = responses.get(RESPONSE_CODE_200);
        assertNotNull(apiResponse);
        assertEquals(apiResponse.getDescription(), RESPONSE_DESCRIPTION);
    }

    @Test(description = "More Responses")
    public void testMoreResponses() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(EnhancedResponsesResource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /:
                    get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex output
                        object
                      operationId: getWithPayloadResponse
                      responses:
                        "200":
                          description: voila!
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/SampleResponseSchema"
                        "404":
                          description: not found!
                        "400":
                          description: boo
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/GenericError"
                      deprecated: true
                components:
                  schemas:
                    GenericError:
                      type: object
                    SampleResponseSchema:
                      type: object
                """;

        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Responses with composition")
    public void testGetResponsesWithComposition() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(ResponsesResource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /:
                    get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex output
                        object
                      operationId: getWithPayloadResponse
                      responses:
                        "200":
                          description: voila!
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/SampleResponseSchema"
                        default:
                          description: boo
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/GenericError"
                      deprecated: true
                  /allOf:
                    get:
                      summary: Test inheritance / polymorphism
                      operationId: getAllOf
                      parameters:
                      - name: number
                        in: query
                        description: Test inheritance / polymorphism
                        required: true
                        schema:
                          type: integer
                          format: int32
                        example: 1
                      responses:
                        "200":
                          description: bean answer
                          content:
                            application/json:
                              schema:
                                allOf:
                                - $ref: "#/components/schemas/MultipleSub1Bean"
                                - $ref: "#/components/schemas/MultipleSub2Bean"
                  /anyOf:
                    get:
                      summary: Test inheritance / polymorphism
                      operationId: getAnyOf
                      parameters:
                      - name: number
                        in: query
                        description: Test inheritance / polymorphism
                        required: true
                        schema:
                          type: integer
                          format: int32
                        example: 1
                      responses:
                        "200":
                          description: bean answer
                          content:
                            application/json:
                              schema:
                                anyOf:
                                - $ref: "#/components/schemas/MultipleSub1Bean"
                                - $ref: "#/components/schemas/MultipleSub2Bean"
                  /oneOf:
                    get:
                      summary: Test inheritance / polymorphism
                      operationId: getOneOf
                      parameters:
                      - name: number
                        in: query
                        description: Test inheritance / polymorphism
                        required: true
                        schema:
                          type: integer
                          format: int32
                        example: 1
                      responses:
                        "200":
                          description: bean answer
                          content:
                            application/json:
                              schema:
                                oneOf:
                                - $ref: "#/components/schemas/MultipleSub1Bean"
                                - $ref: "#/components/schemas/MultipleSub2Bean"
                components:
                  schemas:
                    SampleResponseSchema:
                      type: object
                    GenericError:
                      type: object
                    MultipleSub1Bean:
                      type: object
                      description: MultipleSub1Bean
                      allOf:
                      - $ref: "#/components/schemas/MultipleBaseBean"
                      - type: object
                        properties:
                          c:
                            type: integer
                            format: int32
                    MultipleSub2Bean:
                      type: object
                      description: MultipleSub2Bean
                      allOf:
                      - $ref: "#/components/schemas/MultipleBaseBean"
                      - type: object
                        properties:
                          d:
                            type: integer
                            format: int32
                    MultipleBaseBean:
                      type: object
                      properties:
                        beanType:
                          type: string
                        a:
                          type: integer
                          format: int32
                        b:
                          type: string
                      description: MultipleBaseBean""";
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
        assertEquals(externalDocs.getDescription(), "External documentation description in schema");
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
        assertEquals(extensions.size(), 1);
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
        assertEquals(securityRequirements.size(), SECURITY_REQUIREMENT_NUMBER);
        List<String> scopes = securityRequirements.get(0).get(SECURITY_KEY);
        assertNotNull(scopes);
        assertEquals(scopes.size(), SCOPE_NUMBER);
        assertEquals(scopes.get(0), SCOPE_VALUE1);
        assertEquals(scopes.get(1), SCOPE_VALUE2);
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
        assertEquals(postOperation.getDescription(), CALLBACK_POST_OPERATION_DESCRIPTION);

        Operation getOperation = pathItem.getGet();
        assertNotNull(getOperation);
        assertEquals(getOperation.getDescription(), CALLBACK_GET_OPERATION_DESCRIPTION);

        Operation putOperation = pathItem.getPut();
        assertNotNull(putOperation);
        assertEquals(putOperation.getDescription(), CALLBACK_POST_OPERATION_DESCRIPTION);
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
        assertEquals(parameters.size(), PARAMETER_NUMBER);
        Parameter parameter = parameters.get(0);
        assertNotNull(parameter);
        assertEquals(parameter.getIn(), PARAMETER_IN);
        assertEquals(parameter.getName(), PARAMETER_NAME);
        assertEquals(parameter.getDescription(), PARAMETER_DESCRIPTION);
        assertEquals(parameter.getRequired(), Boolean.TRUE);
        assertEquals(parameter.getAllowEmptyValue(), Boolean.TRUE);
        assertEquals(parameter.getAllowReserved(), Boolean.TRUE);
        Schema schema = parameter.getSchema();
        assertNotNull(schema);
        assertEquals(schema.getType(), SCHEMA_TYPE);
        assertEquals(schema.getFormat(), SCHEMA_FORMAT);
        assertEquals(schema.getDescription(), SCHEMA_DESCRIPTION);
        assertEquals(schema.getReadOnly(), Boolean.TRUE);

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
        String yaml = """
                openapi: 3.0.1
                paths:
                  /array:
                    get:
                      operationId: getArrayResponses
                      responses:
                        default:
                          content:
                            application/json:
                              schema:
                                type: array
                                items:
                                  $ref: https://openebench.bsc.es/monitor/tool/tool.json
                  /schema:
                    get:
                      operationId: getSchemaResponses
                      responses:
                        default:
                          content:
                            application/json:
                              schema:
                                $ref: https://openebench.bsc.es/monitor/tool/tool.json""";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Responses with array schema")
    public void testTicket2340() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket2340Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/test:
                    post:
                      operationId: getAnimal
                      requestBody:
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/Animal"
                      responses:
                        default:
                          description: default response
                          content:
                            application/json:
                              schema:
                                type: string
                components:
                  schemas:
                    Animal:
                      required:
                      - type
                      type: object
                      properties:
                        type:
                          type: string
                      discriminator:
                        propertyName: type
                    Cat:
                      type: object
                      allOf:
                      - $ref: "#/components/schemas/Animal"
                      - type: object
                        properties:
                          lives:
                            type: integer
                            format: int32
                    Dog:
                      type: object
                      allOf:
                      - $ref: "#/components/schemas/Animal"
                      - type: object
                        properties:
                          barkVolume:
                            type: number
                            format: double
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "array schema example")
    public void testTicket2806() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket2806Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test:
                    get:
                      operationId: getTest
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/Test"
                components:
                  schemas:
                    Test:
                      type: object
                      properties:
                        stringArray:
                          maxItems: 4
                          minItems: 2
                          uniqueItems: true
                          type: array
                          description: Array desc
                          example:
                          - aaa
                          - bbb
                          items:
                            type: string
                            description: Hello, World!
                            example: Lorem ipsum dolor set
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "NotNull parameters")
    public void testTicket2794() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket2794Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /notnullparameter:
                    get:
                      operationId: getBooks
                      parameters:
                      - name: page
                        in: query
                        required: true
                        schema:
                          type: integer
                          format: int32
                      responses:
                        default:
                          description: default response
                          content:
                            application/json: {}
                  /notnullparameter/newnotnull:
                    post:
                      operationId: insertnotnull
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Book"
                        required: true
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /notnullparameter/new_reqBody_required:
                    post:
                      operationId: insert
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Book"
                        required: true
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    Book:
                      type: object
                      properties:
                        foo:
                          type: string
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "no NPE resolving map")
    public void testTicket2793() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket2793Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /distances:
                    get:
                      operationId: getDistances
                      responses:
                        "200":
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/DistancesResponse"
                components:
                  schemas:
                    DistancesResponse:
                      type: object
                      properties:
                        empty:
                          type: boolean
                """;
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

        String yaml = """
                openapi: 3.0.1
                info:
                  description: info
                paths:
                  /:
                    get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex output
                        object
                      operationId: getWithPayloadResponse
                      responses:
                        "200":
                          description: voila!
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/SampleResponseSchema"
                        default:
                          description: boo
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/GenericError"
                        "401":
                          $ref: "#/components/responses/invalidJWT"
                      deprecated: true
                components:
                  schemas:
                    GenericError:
                      type: object
                    SampleResponseSchema:
                      type: object
                  responses:
                    invalidJWT:
                      description: when JWT token invalid/expired""";
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

        String yaml = """
                openapi: 3.0.1
                info:
                  description: info
                paths:
                  /:
                    get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex output
                        object
                      operationId: getWithPayloadResponse
                      responses:
                        "200":
                          description: voila!
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/SampleResponseSchema"
                        default:
                          description: boo
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/GenericError"
                        "401":
                          $ref: "#/components/responses/invalidJWT"
                      deprecated: true
                components:
                  schemas:
                    GenericError:
                      type: object
                    SampleResponseSchema:
                      type: object
                  responses:
                    invalidJWT:
                      description: when JWT token invalid/expired""";
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
        String yaml = """
                openapi: 3.0.1
                paths:
                  /:
                    get:
                      operationId: getter
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/Town"
                components:
                  schemas:
                    Town:
                      required:
                      - streets
                      type: object
                      properties:
                        streets:
                          minItems: 1
                          uniqueItems: true
                          type: array
                          items:
                            type: string
                """;
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

        String yaml = """
                openapi: 3.0.1
                info:
                  description: info
                paths:
                  /:
                    get:
                      summary: Simple get operation
                      description: Defines a simple get operation with a payload complex input object
                      operationId: sendPayload
                      requestBody:
                        $ref: "#/components/requestBodies/User"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                      deprecated: true
                components:
                  schemas:
                    User:
                      type: object
                      properties:
                        id:
                          type: integer
                          format: int64
                        username:
                          type: string
                        firstName:
                          type: string
                        lastName:
                          type: string
                        email:
                          type: string
                        password:
                          type: string
                        phone:
                          type: string
                        userStatus:
                          type: integer
                          description: User Status
                          format: int32
                      xml:
                        name: User
                  requestBodies:
                    User:
                      description: Test RequestBody
                """;
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

        String yaml = """
                openapi: 3.0.1
                info:
                  description: info
                paths:
                  /:
                    get:
                      summary: Simple get operation
                      description: Defines a simple get operation with a payload complex input object
                      operationId: sendPayload
                      requestBody:
                        $ref: "#/components/requestBodies/User"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                      deprecated: true
                components:
                  schemas:
                    User:
                      type: object
                      properties:
                        id:
                          type: integer
                          format: int64
                        username:
                          type: string
                        firstName:
                          type: string
                        lastName:
                          type: string
                        email:
                          type: string
                        password:
                          type: string
                        phone:
                          type: string
                        userStatus:
                          type: integer
                          description: User Status
                          format: int32
                      xml:
                        name: User
                  requestBodies:
                    User: {}
                """;
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

        String yaml = """
                openapi: 3.0.1
                info:
                  description: info
                paths:
                  /:
                    get:
                      summary: Simple get operation
                      description: Defines a simple get operation with a payload complex input object
                      operationId: sendPayload
                      parameters:
                      - $ref: "#/components/parameters/id"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                      deprecated: true
                components:
                  parameters:\s
                    id:
                      in: query
                      description: Id Description
                      required: true
                      schema:
                        type: integer
                        format: int32
                      example: 1
                """;
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

        String yaml = """
                openapi: 3.0.1
                info:
                  description: info
                paths:
                  /:
                    get:
                      summary: Simple get operation
                      description: Defines a simple get operation with a payload complex input object
                      operationId: sendPayload
                      parameters:
                      - $ref: "#/components/parameters/id"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                      deprecated: true
                components:
                  parameters:\s
                    id:
                      in: query
                      description: Id Description
                      required: true
                      schema:
                        type: integer
                        format: int32
                      example: 1
                """;
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

        String yaml = """
                openapi: 3.0.1
                info:
                  description: info
                paths:
                  /example:
                    post:
                      description: subscribes a client to updates relevant to the requestor's account
                      operationId: subscribe
                      parameters:
                      - name: subscriptionId
                        in: path
                        required: true
                        style: simple
                        schema:
                          type: string
                          description: Schema
                          example: Subscription example
                        examples:
                          subscriptionId_1:
                            summary: Subscription number 12345
                            description: subscriptionId_1
                            value: 12345
                            externalValue: Subscription external value 1
                            $ref: "#/components/examples/Id"
                        example: example
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              type: integer
                              format: int32
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/SubscriptionResponse"
                components:
                  schemas:
                    SubscriptionResponse:
                      type: object
                      properties:
                        subscriptionId:
                          type: string
                  examples:
                    Id:
                      summary: Id Example
                      description: Id Example
                      value: "1"
                """;
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

        String yaml = """
                openapi: 3.0.1
                info:
                  description: info
                paths:
                  /example:
                    post:
                      description: subscribes a client to updates relevant to the requestor's account
                      operationId: subscribe
                      parameters:
                      - example:
                          $ref: "#/components/examples/Id"
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              type: integer
                              format: int32
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/SubscriptionResponse"
                components:
                  schemas:
                    SubscriptionResponse:
                      type: object
                      properties:
                        subscriptionId:
                          type: string
                  examples:
                    Id:
                      summary: Id Example
                      description: Id Example
                      value: "1"
                """;
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

        String yaml = """
                openapi: 3.0.1
                info:
                  description: info
                paths:
                  /path:
                    get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex output
                      operationId: getWithPayloadResponse
                      responses:
                        "200":
                          description: voila!
                          headers:
                            Rate-Limit-Limit:
                              description: The number of allowed requests in the current period
                              $ref: "#/components/headers/Header"
                              style: simple
                              schema:
                                type: integer
                      deprecated: true
                components:
                  headers:
                    Header:
                      description: Header Description
                """;
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

        String yaml = """
                openapi: 3.0.1
                info:
                  description: info
                paths:
                  /:
                    get:
                      description: description
                      operationId: Operation Id
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                      security:
                      - security_key:
                        - write:pets
                        - read:pets
                components:
                  securitySchemes:
                    Security:
                      type: oauth2
                      description: Security Example
                    myOauth2Security:
                      type: oauth2
                      description: myOauthSecurity Description
                      $ref: "#/components/securitySchemes/Security"
                      in: header
                      flows:
                        implicit:
                          authorizationUrl: http://x.com
                          scopes:
                            write:pets: modify pets in your account
                """;
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

        String yaml = """
                openapi: 3.0.1
                info:
                  description: info
                paths:
                  /links:
                    get:
                      operationId: getUserWithAddress
                      parameters:
                      - name: userId
                        in: query
                        schema:
                          type: string
                      responses:
                        default:
                          description: test description
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/User"
                          links:
                            address:
                              operationId: getAddress
                              parameters:
                                userId: $request.query.userId
                              $ref: "#/components/links/Link"
                components:
                  links:
                    Link:
                      operationId: id
                      description: Link Description
                """;
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

        String yaml = """
                openapi: 3.0.1
                info:
                  description: info
                paths:
                  /simplecallback:
                    get:
                      summary: Simple get operation
                      operationId: getWithNoParameters
                      responses:
                        "200":
                          description: voila!
                      callbacks:
                        testCallback1:
                          $ref: "#/components/callbacks/Callback"
                components:
                  callbacks:
                    Callback:
                      /post:
                        description: Post Path Item
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test
    public void testTicket3015() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket3015Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/test:
                    get:
                      operationId: schemaImpl
                      responses:
                        "200":
                          description: OK
                          content:
                            '*/*':
                              schema:
                                type: string
                                format: uri
                        "400":
                          description: Bad Request
                        "500":
                          description: Internal Server Error
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        PrimitiveType.customExcludedClasses().add(URI.class.getName());
        openAPI = reader.read(Ticket3015Resource.class);
        yaml = """
                openapi: 3.0.1
                paths:
                  /test/test:
                    get:
                      operationId: schemaImpl_1
                      responses:
                        "200":
                          description: OK
                          content:
                            '*/*':
                              schema:
                                type: object
                                properties:
                                  scheme:
                                    type: string
                                  fragment:
                                    type: string
                                  authority:
                                    type: string
                                  userInfo:
                                    type: string
                                  host:
                                    type: string
                                  port:
                                    type: integer
                                    format: int32
                                  path:
                                    type: string
                                  query:
                                    type: string
                                  schemeSpecificPart:
                                    type: string
                                  rawSchemeSpecificPart:
                                    type: string
                                  rawAuthority:
                                    type: string
                                  rawUserInfo:
                                    type: string
                                  rawPath:
                                    type: string
                                  rawQuery:
                                    type: string
                                  rawFragment:
                                    type: string
                                  absolute:
                                    type: boolean
                                  opaque:
                                    type: boolean
                        "400":
                          description: Bad Request
                        "500":
                          description: Internal Server Error
                """;
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

        String yaml = """
                openapi: 3.0.1
                info:
                  description: info
                paths:
                  /2:
                    get:
                      summary: Simple get operation
                      operationId: sendPayload2
                      parameters:
                      - $ref: "#/components/parameters/id"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /1:
                    get:
                      summary: Simple get operation
                      operationId: sendPayload1
                      parameters:
                      - $ref: "#/components/parameters/id"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  parameters:
                    id:
                      in: query
                      description: Id Description
                      required: true
                      schema:
                        type: integer
                        format: int32
                      example: 1
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "response generic subclass")
    public void testTicket3082() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ProcessTokenRestService.class);

        String yaml = """
                openapi: 3.0.1
                paths:
                  /token:
                    post:
                      operationId: create
                      requestBody:
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/ProcessTokenDTO"
                      responses:
                        default:
                          description: default response
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/ProcessTokenDTO"
                components:
                  schemas:
                    ProcessTokenDTO:
                      type: object
                      properties:
                        guid:
                          type: string
                """;
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

        String yaml = """
                openapi: 3.0.1
                paths:
                  /test1:
                    post:
                      operationId: test1
                      requestBody:
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/User"
                            example:
                              foo: foo
                              bar: bar
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test2:
                    post:
                      operationId: test2
                      requestBody:
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/User"
                            example:
                              foo: foo
                              bar: bar
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    User:
                      type: object
                      properties:
                        id:
                          type: integer
                          format: int64
                        username:
                          type: string
                        firstName:
                          type: string
                        lastName:
                          type: string
                        email:
                          type: string
                        password:
                          type: string
                        phone:
                          type: string
                        userStatus:
                          type: integer
                          description: User Status
                          format: int32
                      xml:
                        name: User
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test
    public void testTicket3092() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(UploadResource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /upload:
                    post:
                      operationId: uploadWithBean
                      requestBody:
                        content:
                          multipart/form-data:
                            schema:
                              type: object
                              properties:
                                name:
                                  type: string
                                picture:
                                  $ref: "#/components/schemas/picture"
                      responses:
                        default:
                          description: default response
                          content:
                            application/json: {}
                  /upload/requestbody:
                    post:
                      operationId: uploadWithBeanAndRequestBody
                      requestBody:
                        content:
                          multipart/form-data:
                            schema:
                              $ref: "#/components/schemas/UploadRequest"
                      responses:
                        default:
                          description: default response
                          content:
                            application/json: {}
                components:
                  schemas:
                    picture:
                      type: object
                      format: binary
                    UploadRequest:
                      title: Schema for Upload
                      type: object
                      properties:
                        name:
                          type: string
                        picture:
                          type: string
                          format: binary""";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Parameter examples ordering")
    public void testTicket3587() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket3587Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/test:
                    get:
                      operationId: parameterExamplesOrderingTest
                      parameters:
                      - in: query
                        schema:
                          type: string
                        examples:
                          Example One:
                            description: Example One
                          Example Two:
                            description: Example Two
                          Example Three:
                            description: Example Three
                      - in: query
                        schema:
                          type: string
                        examples:
                          Example Three:
                            description: Example Three
                          Example Two:
                            description: Example Two
                          Example One:
                            description: Example One
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}""";
        SerializationMatchers.assertEqualsToYamlExact(openAPI, yaml);
    }

    @Test(description = "Optional handling")
    public void testTicket3624() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Service.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /example/model:
                    get:
                      tags:
                      - ExampleService
                      summary: ' Retrieve models for display to the user'
                      operationId: getModels
                      responses:
                        default:
                          description: default response
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/Response"
                  /example/model/by/ids:
                    get:
                      tags:
                      - ExampleService
                      summary: ' Retrieve models by their ids'
                      operationId: getModelsById
                      responses:
                        default:
                          description: default response
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/ByIdResponse"
                  /example/containerized/model:
                    get:
                      tags:
                      - ExampleService
                      summary: ' Retrieve review insights for a specific product'
                      operationId: getContainerizedModels
                      responses:
                        default:
                          description: default response
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/ContainerizedResponse"
                components:
                  schemas:
                    Model:
                      type: object
                      properties:
                        text:
                          type: string
                        title:
                          type: string
                        active:
                          type: boolean
                        schemaParent:
                          $ref: "#/components/schemas/Model"
                        optionalString:
                          type: string
                        parent:
                          $ref: "#/components/schemas/Model"
                        id:
                          type: integer
                          format: int32
                    Response:
                      type: object
                      properties:
                        count:
                          type: integer
                          format: int32
                        models:
                          type: array
                          items:
                            $ref: "#/components/schemas/Model"
                    ByIdResponse:
                      type: object
                      properties:
                        modelsById:
                          type: object
                          additionalProperties:
                            $ref: "#/components/schemas/Model"
                    ContainerizedResponse:
                      type: object
                      properties:
                        totalCount:
                          type: integer
                          format: int32
                        containerizedModels:
                          type: array
                          items:
                            $ref: "#/components/schemas/ModelContainer"
                    ModelContainer:
                      type: object
                      properties:
                        text:
                          type: string
                        model:
                          $ref: "#/components/schemas/Model"
                        id:
                          type: integer
                          format: int32""";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test
    public void testRequestBodyEncoding() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(UrlEncodedResourceWithEncodings.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /things/search:
                    post:
                      operationId: searchForThings
                      requestBody:
                        content:
                          application/x-www-form-urlencoded:
                            schema:
                              type: object
                              properties:
                                id:
                                  type: array
                                  description: id param
                                  items:
                                    type: string
                                name:
                                  type: array
                                  items:
                                    type: string
                            encoding:
                              id:
                                style: form
                                explode: true
                              name:
                                style: form
                                explode: false
                      responses:
                        default:
                          description: default response
                          content:
                            application/json: {}
                  /things/sriracha:
                    post:
                      operationId: srirachaThing
                      requestBody:
                        content:
                          application/x-www-form-urlencoded:
                            schema:
                              type: object
                              properties:
                                id:
                                  type: array
                                  description: id param
                                  items:
                                    type: string
                                name:
                                  type: array
                                  items:
                                    type: string
                            encoding:
                              id:
                                style: form
                                explode: true
                              name:
                                style: form
                                explode: false
                      responses:
                        default:
                          description: default response
                          content:
                            application/json: {}
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "overridden generic resource methods")
    public void testTicket3694() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket3694ResourceExtendedType.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /foo:
                    post:
                      tags:
                      - Foo
                      summary: Foo List in Interface
                      operationId: foo
                      requestBody:
                        content:
                          application/json:
                            schema:
                              type: array
                              items:
                                type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /bar:
                    post:
                      operationId: bar
                      requestBody:
                        content:
                          application/json:
                            schema:
                              type: array
                              items:
                                type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                type: string
                  /another:
                    post:
                      operationId: another
                      requestBody:
                        content:
                          application/json:
                            schema:
                              type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}""";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);

        reader = new Reader(new OpenAPI());
        openAPI = reader.read(Ticket3694Resource.class);
        yaml = """
                openapi: 3.0.1
                paths:
                  /foo:
                    post:
                      tags:
                      - Foo
                      summary: Foo List in Interface
                      operationId: foo
                      requestBody:
                        content:
                          application/json:
                            schema:
                              type: array
                              items:
                                type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /bar:
                    post:
                      operationId: bar
                      requestBody:
                        content:
                          application/json:
                            schema:
                              type: array
                              items:
                                type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                type: string
                  /another:
                    post:
                      operationId: another
                      requestBody:
                        content:
                          application/json:
                            schema:
                              type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}""";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);

        reader = new Reader(new OpenAPI());
        openAPI = reader.read(Ticket3694ResourceSimple.class);
        yaml = """
                openapi: 3.0.1
                paths:
                  /bar:
                    post:
                      operationId: bar
                      requestBody:
                        content:
                          application/json:
                            schema:
                              type: array
                              items:
                                type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                type: string""";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);

        reader = new Reader(new OpenAPI());
        openAPI = reader.read(Ticket3694ResourceSimpleSameReturn.class);
        yaml = """
                openapi: 3.0.1
                paths:
                  /bar:
                    post:
                      operationId: bar
                      requestBody:
                        content:
                          application/json:
                            schema:
                              type: array
                              items:
                                type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}""";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "non consistent overridden generic resource methods")
    public void testTicket2144() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(ItemResource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /item/{id}:
                    get:
                      operationId: getById
                      parameters:
                      - name: id
                        in: path
                        required: true
                        schema:
                          type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/ItemWithChildren"
                  /item/nogeneric/{id}:
                    get:
                      operationId: getByIdNoGeneric
                      parameters:
                      - name: id
                        in: path
                        required: true
                        schema:
                          type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/ItemWithChildren"
                  /item/nogenericsamereturn/{id}:
                    get:
                      operationId: getByIdNoGenericSameReturn
                      parameters:
                      - name: id
                        in: path
                        required: true
                        schema:
                          type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/BaseDTO"
                  /item/genericparam:
                    post:
                      operationId: genericParam
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/ItemWithChildren"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/BaseDTO"
                components:
                  schemas:
                    ItemWithChildren:
                      type: object
                      properties:
                        name:
                          type: string
                        names:
                          type: string
                    BaseDTO:
                      type: object
                      properties:
                        name:
                          type: string""";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "overridden generic resource interface default methods")
    public void testTicket3149() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(MainResource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test:
                    post:
                      tags:
                      - Test inheritance on default implementation in interfaces
                      operationId: firstEndpoint
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/SampleDTO"
                      responses:
                        "201":
                          description: Created
                        "400":
                          description: Bad Request
                        "403":
                          description: Forbidden
                        "404":
                          description: Not Found
                  /test/{id}:
                    get:
                      tags:
                      - Test inheritance on default implementation in interfaces
                      operationId: secondEnpoint
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/SampleOtherDTO"
                      responses:
                        "200":
                          description: OK
                        "400":
                          description: Bad Request
                        "403":
                          description: Forbidden
                        "404":
                          description: Not Found
                  /test/original/{id}:
                    get:
                      tags:
                      - Test inheritance on default implementation in interfaces
                      operationId: originalEndpoint
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/SampleOtherDTO"
                      responses:
                        "200":
                          description: OK
                        "400":
                          description: Bad Request
                        "403":
                          description: Forbidden
                        "404":
                          description: Not Found
                components:
                  schemas:
                    SampleDTO:
                      type: object
                      properties:
                        name:
                          type: string
                    SampleOtherDTO:
                      type: object
                      properties:
                        label:
                          type: string""";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "overridden generic resource methods operationId")
    public void testTicket3426() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket3426Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /inheritExample/{input}:
                    get:
                      operationId: get
                      parameters:
                      - name: input
                        in: path
                        required: true
                        schema:
                          type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                type: string""";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Constraints annotations in models")
    public void testTicket3731() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket3731Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/cart:
                    get:
                      summary: Get cart items
                      description: Paging follows RFC 5005.
                      operationId: getCart
                      parameters:
                      - name: pageSize
                        in: query
                        description: "Number of items per page. Range[1, 200]"
                        schema:
                          maximum: 200
                          minimum: 1
                          type: integer
                          format: int32
                          default: 50
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                type: array
                                items:
                                  type: string
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);

        reader = new Reader(new OpenAPI());
        openAPI = reader.read(Ticket3731BisResource.class);
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Test SchemaProperties and additionalProperties annotations")
    public void testSchemaProperties() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(SchemaPropertiesResource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /:
                    get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex output
                        object
                      operationId: getWithPayloadResponse
                      responses:
                        "200":
                          description: voila!
                          content:
                            application/json:
                              schema:
                                type: object
                                properties:
                                  foo:
                                    maximum: 1
                                    type: integer
                        default:
                          description: boo
                          content:
                            application/json:
                              schema:
                                maxProperties: 3
                                type: object
                                properties:
                                  foo:
                                    maximum: 1
                                    type: integer
                                description: various properties
                        "400":
                          description: additionalProperties schema
                          content:
                            application/json:
                              schema:
                                maxProperties: 2
                                type: object
                                additionalProperties:
                                  type: string
                        "401":
                          description: additionalProperties boolean
                          content:
                            application/json:
                              schema:
                                maxProperties: 2
                                type: object
                                additionalProperties: false
                      deprecated: true
                  /one:
                    get:
                      operationId: requestBodySchemaPropertyNoSchema
                      requestBody:
                        content:
                          application/yaml:
                            schema:
                              type: object
                              properties:
                                foo:
                                  type: string
                      responses:
                        default:
                          description: default response
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/MultipleBaseBean"
                  /two:
                    get:
                      operationId: requestBodySchemaPropertySchema
                      requestBody:
                        content:
                          application/yaml:
                            schema:
                              required:
                              - foo
                              type: object
                              properties:
                                foo:
                                  type: string
                      responses:
                        default:
                          description: default response
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/MultipleBaseBean"
                  /three:
                    get:
                      operationId: requestBodySchemaPropertySchemaArray
                      requestBody:
                        content:
                          application/yaml:
                            schema:
                              type: array
                              items:
                                required:
                                - foo
                                type: object
                                properties:
                                  foo:
                                    type: string
                      responses:
                        default:
                          description: default response
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/MultipleBaseBean"
                components:
                  schemas:
                    MultipleBaseBean:
                      type: object
                      properties:
                        beanType:
                          type: string
                        a:
                          type: integer
                          format: int32
                        b:
                          type: string
                      description: MultipleBaseBean
                    MultipleSub1Bean:
                      type: object
                      description: MultipleSub1Bean
                      allOf:
                      - $ref: "#/components/schemas/MultipleBaseBean"
                      - type: object
                        properties:
                          c:
                            type: integer
                            format: int32
                    MultipleSub2Bean:
                      type: object
                      description: MultipleSub2Bean
                      allOf:
                      - $ref: "#/components/schemas/MultipleBaseBean"
                      - type: object
                        properties:
                          d:
                            type: integer
                            format: int32
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Test Schema AdditionalProperties annotations")
    public void testSchemaAdditionalProperties() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(SchemaAdditionalPropertiesResource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /arraySchemaImpl:
                    get:
                      operationId: arraySchemaImpl
                      responses:
                        "200":
                          description: voila!
                          content:
                            application/json:
                              schema:
                                type: object
                                additionalProperties:
                                  type: array
                                  items:
                                    $ref: "#/components/schemas/Pet"
                  /fromtResponseType:
                    get:
                      operationId: fromtResponseType
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                type: object
                                additionalProperties:
                                  type: array
                                  items:
                                    $ref: "#/components/schemas/Pet"
                  /schemaImpl:
                    get:
                      operationId: schemaImpl
                      responses:
                        "200":
                          description: voila!
                          content:
                            application/json:
                              schema:
                                type: object
                                additionalProperties:
                                  $ref: "#/components/schemas/Pet"
                  /schemaNotImpl:
                    get:
                      operationId: schemaNotImpl
                      responses:
                        "200":
                          description: voila!
                          content:
                            application/json:
                              schema:
                                type: object
                                additionalProperties:
                                  $ref: "#/components/schemas/Pet"
                components:
                  schemas:
                    Pet:
                      type: object
                      properties:
                        foo:
                          type: string
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Test Schema AdditionalProperties annotations")
    public void testSchemaAdditionalPropertiesBoolean() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI(new OpenAPI()).schemaResolution(Schema.SchemaResolution.ALL_OF);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(SchemaAdditionalPropertiesBooleanResource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test:
                    get:
                      operationId: test
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/Pet"
                components:
                  schemas:
                    Bar:
                      type: object
                      properties:
                        foo:
                          type: string
                    Pet:
                      type: object
                      properties:
                        bar:
                          allOf:
                          - additionalProperties:
                              $ref: "#/components/schemas/Bar"
                          - $ref: "#/components/schemas/Bar"
                        vbar:
                          allOf:
                          - additionalProperties: false
                          - $ref: "#/components/schemas/Bar"
                      additionalProperties: false
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "Test ArraySchema implementation annotations")
    public void testArraySchemaImplementation() {
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI31(true).openAPI(new OpenAPI());
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(ArraySchemaImplementationResource.class);
        String yaml = """
                openapi: 3.1.0
                paths:
                  /test:
                    get:
                      operationId: test
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/Pet"
                components:
                  schemas:
                    Pet:
                      type: object
                      properties:
                        cars:
                          type: array
                          items:
                            type: integer
                            format: int32
                            description: A house in a street
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test(description = "Responses schema resolved from return type")
    public void testResponseReturnType() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(ResponseReturnTypeResource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /sample/{id}:
                    get:
                      summary: Find by id
                      description: Find by id operation
                      operationId: find
                      parameters:
                      - name: id
                        in: path
                        description: ID
                        required: true
                        schema:
                          type: integer
                          format: int32
                      responses:
                        "200":
                          description: Ok
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/TestDTO"
                        "201":
                          description: "201"
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/TestDTO"
                        "204":
                          description: No Content
                          content:
                            application/json: {}
                  /sample/{id}/default:
                    get:
                      summary: Find by id (default)
                      description: Find by id operation (default)
                      operationId: findDefault
                      parameters:
                      - name: id
                        in: path
                        description: ID
                        required: true
                        schema:
                          type: integer
                          format: int32
                      responses:
                        default:
                          description: default response
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/TestDTO"
                components:
                  schemas:
                    TestDTO:
                      type: object
                      properties:
                        foo:
                          type: string""";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Responses Default Status")
    public void testResponseDefaultStatus() {
        SwaggerConfiguration config = new SwaggerConfiguration().defaultResponseCode("200");
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(DefaultResponseResource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /:
                    get:
                      operationId: test
                      responses:
                        "200":
                          description: default response
                          content:
                            '*/*':
                              schema:
                                type: string
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test
    public void test4412PathWildcards() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket4412Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/sws/{var}:
                    get:
                      operationId: getCart
                      parameters:
                      - name: var
                        in: path
                        required: true
                        schema:
                          pattern: .*
                          type: string
                      responses:
                        default:
                          description: default response
                          content:
                            text/xml:
                              schema:
                                type: array
                                items:
                                  type: string""";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test
    public void testOas31Petstore() {
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI31(true).openAPI(new OpenAPI());
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(PetResource.class);
        String yaml = """
                openapi: 3.1.0
                paths:
                  /pet:
                    put:
                      summary: Update an existing pet
                      operationId: updatePet
                      requestBody:
                        description: Pet object that needs to be added to the store
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/Pet"
                        required: true
                      responses:
                        "400":
                          description: Invalid ID supplied
                        "404":
                          description: Pet not found
                        "405":
                          description: Validation exception
                    post:
                      summary: Add a new pet to the store
                      operationId: addPet
                      requestBody:
                        description: Pet object that needs to be added to the store
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/Pet"
                          application/xml:
                            schema:
                              $ref: "#/components/schemas/Pet"
                        required: true
                      responses:
                        "405":
                          description: Invalid input
                  /pet/bodyid:
                    post:
                      summary: Add a new pet to the store passing an integer with generic parameter
                        annotation
                      operationId: addPetByInteger
                      requestBody:
                        description: Pet object that needs to be added to the store
                        content:
                          application/json:
                            schema:
                              type: integer
                              format: int32
                          application/xml:
                            schema:
                              type: integer
                              format: int32
                        required: true
                      responses:
                        "405":
                          description: Invalid input
                  /pet/bodyidnoannotation:
                    post:
                      summary: Add a new pet to the store passing an integer without parameter annotation
                      operationId: addPetByIntegerNoAnnotation
                      requestBody:
                        content:
                          application/json:
                            schema:
                              type: integer
                              format: int32
                          application/xml:
                            schema:
                              type: integer
                              format: int32
                      responses:
                        "405":
                          description: Invalid input
                  /pet/bodynoannotation:
                    post:
                      summary: Add a new pet to the store no annotation
                      operationId: addPetNoAnnotation
                      requestBody:
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/Pet"
                          application/xml:
                            schema:
                              $ref: "#/components/schemas/Pet"
                      responses:
                        "405":
                          description: Invalid input
                  /pet/findByStatus:
                    get:
                      summary: Finds Pets by status
                      description: Multiple status values can be provided with comma separated strings
                      operationId: findPetsByStatus
                      parameters:
                      - name: status
                        in: query
                        description: Status values that need to be considered for filter
                        required: true
                        schema:
                          type: string
                      - name: skip
                        in: query
                        schema:
                          type: integer
                          format: int32
                      - name: limit
                        in: query
                        schema:
                          type: integer
                          format: int32
                      responses:
                        default:
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/Pet"
                        "400":
                          description: Invalid status value
                  /pet/findByTags:
                    get:
                      summary: Finds Pets by tags
                      description: "Multiple tags can be provided with comma separated strings. Use\\
                        \\ tag1, tag2, tag3 for testing."
                      operationId: findPetsByTags
                      parameters:
                      - name: tags
                        in: query
                        description: Tags to filter by
                        required: true
                        schema:
                          type: string
                      responses:
                        default:
                          description: Pets matching criteria
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/Pet"
                        "400":
                          description: Invalid tag value
                      deprecated: true
                  /pet/{petId}:
                    get:
                      summary: Find pet by ID
                      description: Returns a pet when 0 < ID <= 10.  ID > 10 or nonintegers will simulate
                        API error conditions
                      operationId: getPetById
                      parameters:
                      - name: petId
                        in: path
                        description: ID of pet that needs to be fetched
                        required: true
                        schema:
                          type: integer
                          format: int64
                      responses:
                        default:
                          description: The pet
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/Pet"
                            application/xml:
                              schema:
                                $ref: "#/components/schemas/Pet"
                        "400":
                          description: Invalid ID supplied
                        "404":
                          description: Pet not found
                components:
                  schemas:
                    Bar:
                      type: object
                      deprecated: true
                      description: Bar
                      properties:
                        foo:
                          type: string
                          const: bar
                        bar:
                          type: integer
                          format: int32
                          exclusiveMaximum: 4
                        foobar:
                          type:
                          - string
                          - integer
                          format: int32
                    Category:
                      type: object
                      properties:
                        id:
                          type: integer
                          format: int64
                        name:
                          type: string
                      xml:
                        name: Category
                    Foo:
                      type: object
                      deprecated: true
                      description: Foo
                      properties:
                        foo:
                          type: string
                          const: foo
                        bar:
                          type: integer
                          format: int32
                          exclusiveMaximum: 2
                        foobar:
                          type:
                          - string
                          - object
                          format: int32
                    IfSchema:
                      type: object
                      deprecated: true
                      description: if schema
                      properties:
                        foo:
                          type: string
                          const: foo
                        bar:
                          type: integer
                          format: int32
                          exclusiveMaximum: 2
                        foobar:
                          type:
                          - string
                          - object
                          format: int32
                    Pet:
                      type: object
                      properties:
                        id:
                          type: integer
                          format: int64
                        category:
                          $ref: "#/components/schemas/Category"
                        name:
                          type: string
                        photoUrls:
                          type: array
                          items:
                            type: string
                            xml:
                              name: photoUrl
                          xml:
                            wrapped: true
                        tags:
                          type: array
                          items:
                            $ref: "#/components/schemas/Tag"
                          xml:
                            wrapped: true
                        status:
                          type: string
                          if:
                            $ref: "#/components/schemas/IfSchema"
                          $id: idtest
                          description: pet status in the store
                          enum:
                          - "available,pending,sold"
                      xml:
                        name: Pet
                    Tag:
                      type: object
                      properties:
                        id:
                          type: integer
                          format: int64
                        name:
                          type: string
                        annotated:
                          $ref: "#/components/schemas/Category"
                          description: child description
                          properties:
                            foo:
                              $ref: "#/components/schemas/Foo"
                            bar:
                              $ref: "#/components/schemas/Bar"
                      xml:
                        name: Tag
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void test31RefSiblings() {
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI31(true).openAPI(new OpenAPI());
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(TagResource.class);
        String yaml = """
                openapi: 3.1.0
                paths:
                  /tag/tag:
                    get:
                      operationId: getTag
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/SimpleTag"
                components:
                  schemas:
                    Foo:
                      type: object
                      deprecated: true
                      description: Foo
                      properties:
                        foo:
                          type: string
                          const: foo
                        bar:
                          type: integer
                          format: int32
                          exclusiveMaximum: 2
                        foobar:
                          type:
                          - string
                          - object
                          format: int32
                    SimpleTag:
                      type: object
                      properties:
                        annotated:
                          $ref: "#/components/schemas/SimpleCategory"
                          description: child description
                          properties:
                            foo:
                              $ref: "#/components/schemas/Foo"
                    SimpleCategory: {}
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void testSiblings() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));

        OpenAPI openAPI = reader.read(SiblingsResource.class);
        String yaml = """
                openapi: 3.1.0
                paths:
                  /test:
                    get:
                      operationId: getCart
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/Pet"
                components:
                  schemas:
                    Category:
                      type: object
                      description: parent
                      properties:
                        id:
                          type: integer
                          format: int64
                    Pet:
                      type: object
                      description: Pet
                      properties:
                        category:
                          $ref: "#/components/schemas/Category"
                          description: child
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void testSiblingsOnResource() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));

        OpenAPI openAPI = reader.read(SiblingsResourceSimple.class);
        String yaml = """
                openapi: 3.1.0
                paths:
                  /test:
                    get:
                      operationId: getCart
                      responses:
                        "300":
                          description: aaa
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/PetSimple"
                                description: resource pet
                                readOnly: true
                  /test/impl:
                    get:
                      operationId: getCartImpl
                      responses:
                        "300":
                          description: aaa
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/PetSimple"
                                description: resource pet
                                readOnly: true
                components:
                  schemas:
                    PetSimple:
                      description: Pet
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void testSiblingsOnResourceResponse() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));

        OpenAPI openAPI = reader.read(SiblingsResourceResponse.class);
        String yaml = """
                openapi: 3.1.0
                paths:
                  /test:
                    get:
                      operationId: getCart
                      responses:
                        "300":
                          description: aaa
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/PetSimple"
                                description: resource pet
                                readOnly: true
                            application/xml:
                              schema:
                                $ref: "#/components/schemas/PetSimple"
                                description: resource pet xml
                                readOnly: true
                  /test/impl:
                    get:
                      operationId: getCartImpl
                      responses:
                        "300":
                          description: aaa
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/PetSimple"
                                description: resource pet
                                readOnly: true
                            application/xml:
                              schema:
                                $ref: "#/components/schemas/PetSimple"
                                description: resource pet xml
                                readOnly: true
                components:
                  schemas:
                    PetSimple:
                      description: Pet
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void testSiblingsOnResourceRequestBody() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));

        OpenAPI openAPI = reader.read(SiblingsResourceRequestBody.class);
        String yaml = """
                openapi: 3.1.0
                paths:
                  /test/bodyimpl:
                    get:
                      operationId: getBodyImpl
                      requestBody:
                        description: aaa
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/PetSimple"
                              description: resource pet
                              writeOnly: true
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/bodyimplparam:
                    get:
                      operationId: getBodyImplParam
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/PetSimple"
                              description: resource pet
                              writeOnly: true
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    PetSimple:
                      description: Pet
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void testSiblingsOnResourceRequestBodyMultiple() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));

        OpenAPI openAPI = reader.read(SiblingsResourceRequestBodyMultiple.class);
        String yaml = """
                openapi: 3.1.0
                paths:
                  /test/bodyimpl:
                    get:
                      operationId: getBodyImpl
                      requestBody:
                        description: aaa
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/PetSimple"
                              description: resource pet
                              writeOnly: true
                          application/xml:
                            schema:
                              $ref: "#/components/schemas/PetSimple"
                              description: resource pet xml
                              writeOnly: true
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/bodyimplparam:
                    get:
                      operationId: getBodyImplParam
                      requestBody:
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/PetSimple"
                              description: resource pet
                              writeOnly: true
                          application/xml:
                            schema:
                              $ref: "#/components/schemas/PetSimple"
                              description: resource pet xml
                              writeOnly: true
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/bodyparam:
                    get:
                      operationId: getBodyParam
                      requestBody:
                        description: test
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/PetSimple"
                              description: resource pet
                              writeOnly: true
                          application/xml:
                            schema:
                              $ref: "#/components/schemas/PetSimple"
                              description: resource pet xml
                              writeOnly: true
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    PetSimple:
                      description: Pet
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void testSiblingsOnProperty() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));
        Set<Class<?>> classes = new HashSet<>(Arrays.asList(SiblingPropResource.class, WebHookResource.class));
        OpenAPI openAPI = reader.read(classes);
        String yaml = """
                openapi: 3.1.0
                paths:
                  /pet:
                    put:
                      tags:
                      - pet
                      summary: Update an existing pet
                      operationId: updatePet
                      requestBody:
                        description: Pet object that needs to be updated in the store
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/Pet"
                              description: A Pet in JSON Format
                              required:
                              - id
                              writeOnly: true
                          application/xml:
                            schema:
                              $ref: "#/components/schemas/Pet"
                              description: A Pet in XML Format
                              required:
                              - id
                              writeOnly: true
                        required: true
                      responses:
                        "200":
                          description: Successful operation
                          content:
                            application/xml:
                              schema:
                                $ref: "#/components/schemas/Pet"
                                description: A Pet in XML Format
                                readOnly: true
                            application/json:
                              schema:
                                $ref: "#/components/schemas/Pet"
                                description: A Pet in JSON Format
                                readOnly: true
                        "400":
                          description: Invalid ID supplied
                        "404":
                          description: Pet not found
                        "405":
                          description: Validation exception
                      security:
                      - petstore_auth:
                        - write:pets
                        - read:pets
                      - mutual_tls: []
                components:
                  schemas:
                    Category:
                      type: object
                      description: parent
                      properties:
                        id:
                          type: integer
                          format: int64
                    Pet:
                      type: object
                      description: Pet
                      properties:
                        category:
                          $ref: "#/components/schemas/Category"
                          description: child
                webhooks:
                  newPet:
                    post:
                      requestBody:
                        description: Information about a new pet in the system
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/Pet"
                              description: Webhook Pet
                      responses:
                        "200":
                          description: Return a 200 status to indicate that the data was received
                            successfully
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void testMisc31() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));
        Set<Class<?>> classes = new HashSet<>(Arrays.asList(Misc31Resource.class));
        OpenAPI openAPI = reader.read(classes);
        String yaml = """
                openapi: 3.1.0
                paths:
                  /pet:
                    put:
                      operationId: updatePet
                      responses:
                        default:
                          description: default response
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/ModelWithOAS31Stuff"
                            application/xml:
                              schema:
                                $ref: "#/components/schemas/ModelWithOAS31Stuff"
                components:
                  schemas:
                    ModelWithOAS31Stuff:
                      type: object
                      $comment: Random comment at schema level
                      $id: http://yourdomain.com/schemas/myschema.json
                      description: this is model for testing OAS 3.1 resolving
                      properties:
                        randomList:
                          type: array
                          contains:
                            type: string
                          items:
                            type: string
                          maxContains: 10
                          minContains: 1
                          prefixItems:
                          - type: string
                          unevaluatedItems:
                            type: number
                        status:
                          type:
                          - string
                          - number
                        intValue:
                          type: integer
                          format: int32
                          $anchor: intValue
                          $comment: comment at schema property level
                          exclusiveMaximum: 100
                          exclusiveMinimum: 1
                        text:
                          type: string
                          contentEncoding: plan/text
                          contentMediaType: base64
                        encodedString:
                          type: string
                          contentMediaType: application/jwt
                          contentSchema:
                            $ref: "#/components/schemas/MultipleBaseBean"
                        address:
                          $ref: "#/components/schemas/Address"
                        client:
                          type: string
                          dependentSchemas:
                            creditCard:
                              $ref: "#/components/schemas/CreditCard"
                    MultipleBaseBean:
                      type: object
                      description: MultipleBaseBean
                      properties:
                        beanType:
                          type: string
                        a:
                          type: integer
                          format: int32
                        b:
                          type: string
                    MultipleSub1Bean:
                      allOf:
                      - $ref: "#/components/schemas/MultipleBaseBean"
                      - type: object
                        properties:
                          c:
                            type: integer
                            format: int32
                      description: MultipleSub1Bean
                    MultipleSub2Bean:
                      allOf:
                      - $ref: "#/components/schemas/MultipleBaseBean"
                      - type: object
                        properties:
                          d:
                            type: integer
                            format: int32
                      description: MultipleSub2Bean
                    Address:
                      type: object
                      if:
                        $ref: "#/components/schemas/AnnotatedCountry"
                      then:
                        $ref: "#/components/schemas/PostalCodeNumberPattern"
                      else:
                        $ref: "#/components/schemas/PostalCodePattern"
                      dependentRequired:
                        street:
                        - country
                      properties:
                        street:
                          type: string
                        country:
                          type: string
                          enum:
                          - United States of America
                          - Canada
                      propertyNames:
                        pattern: "^[A-Za-z_][A-Za-z0-9_]*$"
                    AnnotatedCountry:
                      type: object
                      properties:
                        country:
                          const: United States
                    CreditCard:
                      type: object
                      properties:
                        billingAddress:
                          type: string
                    PostalCodeNumberPattern:
                      type: object
                      properties:
                        postalCode:
                          pattern: "[0-9]{5}(-[0-9]{4})?"
                    PostalCodePattern:
                      type: object
                      properties:
                        postalCode:
                          pattern: "[A-Z][0-9][A-Z] [0-9][A-Z][0-9]"
                    PropertyNamesPattern:
                      pattern: "^[A-Za-z_][A-Za-z0-9_]*$"
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void test4446CyclicProp() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket4446Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/test:
                    get:
                      operationId: getCart
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/MyPojo"
                components:
                  schemas:
                    MyPojo:
                      type: object
                      properties:
                        someStrings:
                          type: array
                          items:
                            type: string
                        morePojos:
                          type: array
                          items:
                            $ref: "#/components/schemas/MyPojo"
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test
    public void testParameterMaximumValue() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));

        OpenAPI openAPI = reader.read(ParameterMaximumValueResource.class);
        String yaml = """
                openapi: 3.1.0
                paths:
                  /test/{petId}:
                    get:
                      operationId: getPetById
                      parameters:
                      - name: petId
                        in: path
                        description: ID of pet that needs to be fetched
                        required: true
                        schema:
                          type: integer
                          format: int64
                          exclusiveMaximum: 10
                          exclusiveMinimum: 1
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void test4483Response() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket4483Resource.class);
        String yaml = """
                openapi: 3.0.1
                tags:
                - name: Dummy
                  description: Dummy resource for testing setup
                paths:
                  /test:
                    get:
                      tags:
                      - Dummy
                      description: Dummy GET
                      operationId: dummy
                      responses:
                        "401":
                          description: Authentication is required
                          content:
                            application/json:
                              schema:
                                type: array
                                items:
                                  $ref: "#/components/schemas/LocalizedError"
                        "200":
                          description: test
                          content:
                            application/json:
                              schema:
                                type: object
                                additionalProperties:
                                  type: boolean
                  /test/opresp:
                    get:
                      tags:
                      - Dummy
                      operationId: dummyopresp
                      responses:
                        "401":
                          description: Authentication is required
                          content:
                            application/json:
                              schema:
                                type: array
                                items:
                                  $ref: "#/components/schemas/LocalizedError"
                        "200":
                          description: Dummy GET opresp
                          content:
                            application/json:
                              schema:
                                type: object
                                additionalProperties:
                                  type: boolean
                  /test/oprespnodesc:
                    get:
                      tags:
                      - Dummy
                      operationId: oprespnodesc
                      responses:
                        "401":
                          description: Authentication is required
                          content:
                            application/json:
                              schema:
                                type: array
                                items:
                                  $ref: "#/components/schemas/LocalizedError"
                components:
                  schemas:
                    LocalizedError:
                      type: object
                      properties:
                        code:
                          type: string
                        message:
                          type: string
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "openAPIVersion")
    public void testOpenAPIVersion() {
        SwaggerConfiguration config = new SwaggerConfiguration().openAPIVersion("3.0.4");
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(DefaultResponseResource.class);
        String yaml = """
                openapi: 3.0.4
                paths:
                  /:
                    get:
                      operationId: test
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                type: string
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Constraints annotations with groups - Inline")
    public void testTicket4804Inline() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration().schemaResolution(Schema.SchemaResolution.INLINE);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4804Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/barcart:
                    put:
                      operationId: barCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              required:
                              - notNullcartDetails
                              type: object
                              properties:
                                pageSize:
                                  type: integer
                                  format: int32
                                cartDetails:
                                  required:
                                  - description
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                                notNullcartDetails:
                                  required:
                                  - description
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/foocart:
                    put:
                      operationId: fooCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              required:
                              - cartDetails
                              - notNullcartDetails
                              type: object
                              properties:
                                pageSize:
                                  type: integer
                                  format: int32
                                cartDetails:
                                  required:
                                  - description
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                                notNullcartDetails:
                                  required:
                                  - description
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/createcart:
                    post:
                      operationId: postCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              required:
                              - notNullcartDetails
                              - pageSize
                              type: object
                              properties:
                                pageSize:
                                  type: integer
                                  format: int32
                                cartDetails:
                                  required:
                                  - description
                                  - name
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                                notNullcartDetails:
                                  required:
                                  - description
                                  - name
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/updatecart:
                    put:
                      operationId: putCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              required:
                              - notNullcartDetails
                              type: object
                              properties:
                                pageSize:
                                  type: integer
                                  format: int32
                                cartDetails:
                                  required:
                                  - description
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                                notNullcartDetails:
                                  required:
                                  - description
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    Cart:
                      required:
                      - notNullcartDetails
                      type: object
                      properties:
                        pageSize:
                          type: integer
                          format: int32
                        cartDetails:
                          required:
                          - description
                          type: object
                          properties:
                            name:
                              type: string
                            description:
                              type: string
                        notNullcartDetails:
                          required:
                          - description
                          type: object
                          properties:
                            name:
                              type: string
                            description:
                              type: string
                    CartDetails:
                      required:
                      - description
                      type: object
                      properties:
                        name:
                          type: string
                        description:
                          type: string
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "Constraints annotations with groups - Default")
    public void testTicket4804Default() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration();
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4804Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/barcart:
                    put:
                      operationId: barCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Cart"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/foocart:
                    put:
                      operationId: fooCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Cart"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/createcart:
                    post:
                      operationId: postCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Cart"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/updatecart:
                    put:
                      operationId: putCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Cart"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    Cart:
                      required:
                      - notNullcartDetails
                      type: object
                      properties:
                        pageSize:
                          type: integer
                          format: int32
                        cartDetails:
                          $ref: "#/components/schemas/CartDetails"
                        notNullcartDetails:
                          $ref: "#/components/schemas/CartDetails"
                    CartDetails:
                      required:
                      - description
                      type: object
                      properties:
                        name:
                          type: string
                        description:
                          type: string
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "Constraints annotations with groups - Default NotBlank")
    public void testTicket4804DefaultNotBlank() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration();
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4804NotBlankResource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/barcart:
                    put:
                      operationId: barCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Cart"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/createcart:
                    post:
                      operationId: postCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Cart"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/updatecart:
                    put:
                      operationId: putCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Cart"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    Cart:
                      required:
                      - notNullcartDetails
                      - pageSizes
                      type: object
                      properties:
                        pageSizes:
                          type: array
                          items:
                            type: integer
                            format: int32
                        notNullcartDetails:
                          $ref: "#/components/schemas/CartDetails"
                    CartDetails:
                      required:
                      - description
                      - name
                      type: object
                      properties:
                        name:
                          minLength: 1
                          type: string
                        description:
                          minItems: 1
                          type: array
                          items:
                            type: string
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "Constraints annotations with groups - Always")
    public void testTicket4804Always() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration().groupsValidationStrategy(Configuration.GroupsValidationStrategy.ALWAYS);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4804Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/barcart:
                    put:
                      operationId: barCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Cart"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/foocart:
                    put:
                      operationId: fooCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Cart"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/createcart:
                    post:
                      operationId: postCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Cart"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/updatecart:
                    put:
                      operationId: putCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Cart"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    Cart:
                      required:
                      - cartDetails
                      - notNullcartDetails
                      - pageSize
                      type: object
                      properties:
                        pageSize:
                          type: integer
                          format: int32
                        cartDetails:
                          $ref: "#/components/schemas/CartDetails"
                        notNullcartDetails:
                          $ref: "#/components/schemas/CartDetails"
                    CartDetails:
                      required:
                      - description
                      - name
                      type: object
                      properties:
                        name:
                          type: string
                        description:
                          type: string
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "Constraints annotations with groups - Never")
    public void testTicket4804Never() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration().groupsValidationStrategy(Configuration.GroupsValidationStrategy.NEVER);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4804Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/barcart:
                    put:
                      operationId: barCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Cart"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/foocart:
                    put:
                      operationId: fooCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Cart"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/createcart:
                    post:
                      operationId: postCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Cart"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/updatecart:
                    put:
                      operationId: putCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Cart"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    Cart:
                      type: object
                      properties:
                        pageSize:
                          type: integer
                          format: int32
                        cartDetails:
                          $ref: "#/components/schemas/CartDetails"
                        notNullcartDetails:
                          $ref: "#/components/schemas/CartDetails"
                    CartDetails:
                      type: object
                      properties:
                        name:
                          type: string
                        description:
                          type: string
                """;
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
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/barcart:
                    put:
                      operationId: barCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              type: object
                              properties:
                                pageSize:
                                  type: integer
                                  format: int32
                                cartDetails:
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                                notNullcartDetails:
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/foocart:
                    put:
                      operationId: fooCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              required:
                              - cartDetails
                              type: object
                              properties:
                                pageSize:
                                  type: integer
                                  format: int32
                                cartDetails:
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                                notNullcartDetails:
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/createcart:
                    post:
                      operationId: postCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              required:
                              - pageSize
                              type: object
                              properties:
                                pageSize:
                                  type: integer
                                  format: int32
                                cartDetails:
                                  required:
                                  - name
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                                notNullcartDetails:
                                  required:
                                  - name
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/updatecart:
                    put:
                      operationId: putCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              type: object
                              properties:
                                pageSize:
                                  type: integer
                                  format: int32
                                cartDetails:
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                                notNullcartDetails:
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    Cart:
                      type: object
                      properties:
                        pageSize:
                          type: integer
                          format: int32
                        cartDetails:
                          type: object
                          properties:
                            name:
                              type: string
                            description:
                              type: string
                        notNullcartDetails:
                          type: object
                          properties:
                            name:
                              type: string
                            description:
                              type: string
                    CartDetails:
                      type: object
                      properties:
                        name:
                          type: string
                        description:
                          type: string
                """;
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
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/barcart:
                    put:
                      operationId: barCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              type: object
                              properties:
                                pageSize:
                                  type: integer
                                  format: int32
                                cartDetails:
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                                notNullcartDetails:
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/foocart:
                    put:
                      operationId: fooCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              type: object
                              properties:
                                pageSize:
                                  type: integer
                                  format: int32
                                cartDetails:
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                                notNullcartDetails:
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/createcart:
                    post:
                      operationId: postCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              type: object
                              properties:
                                pageSize:
                                  type: integer
                                  format: int32
                                cartDetails:
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                                notNullcartDetails:
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/updatecart:
                    put:
                      operationId: putCart
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              required:
                              - cartDetails
                              type: object
                              properties:
                                pageSize:
                                  type: integer
                                  format: int32
                                cartDetails:
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                                notNullcartDetails:
                                  type: object
                                  properties:
                                    name:
                                      type: string
                                    description:
                                      type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    Cart:
                      required:
                      - cartDetails
                      type: object
                      properties:
                        pageSize:
                          type: integer
                          format: int32
                        cartDetails:
                          type: object
                          properties:
                            name:
                              type: string
                            description:
                              type: string
                        notNullcartDetails:
                          type: object
                          properties:
                            name:
                              type: string
                            description:
                              type: string
                    CartDetails:
                      type: object
                      properties:
                        name:
                          type: string
                        description:
                          type: string
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test
    public void shouldIncludeOnlyNonGroupedJakartaValidatedFieldsAsMandatoryByDefault() {
        ModelConverters.reset();
        ResolvedSchema schema = ModelConverters.getInstance(false).resolveAsResolvedSchema(new AnnotatedType().type(Ticket4804CustomClass.class));
        String expectedYaml = """
                schema:
                  required:
                  - nonGroupValidatedField
                  type: object
                  properties:
                    nonGroupValidatedField:
                      type: string
                    singleGroupValidatedField:
                      type: integer
                      format: int32
                    multipleGroupValidatedField:
                      type: number
                    otherGroupValidatedField:
                      type: string
                    singleGroupValidatedField2:
                      type: string
                referencedSchemas:
                  Ticket4804CustomClass:
                    required:
                    - nonGroupValidatedField
                    type: object
                    properties:
                      nonGroupValidatedField:
                        type: string
                      singleGroupValidatedField:
                        type: integer
                        format: int32
                      multipleGroupValidatedField:
                        type: number
                      otherGroupValidatedField:
                        type: string
                      singleGroupValidatedField2:
                        type: string
                """;
        SerializationMatchers.assertEqualsToYaml(schema, expectedYaml);
        ModelConverters.reset();
    }

    @Test(description = "test schema.minLength applied")
    public void testTicket4859() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration();
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4859Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/minlength:
                    put:
                      operationId: minlength
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Minlength"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    Minlength:
                      required:
                      - name
                      type: object
                      properties:
                        name:
                          maxLength: 19
                          minLength: 12
                          type: string
                          example: "4242424242424242"
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "test default value type")
    public void testTicket4879() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI31(true);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4879Resource.class);
        String yaml = """
                openapi: 3.1.0
                paths:
                  /test/test:
                    put:
                      operationId: test
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/DefaultClass"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/testDefaultValueAnnotation:
                    get:
                      operationId: testDefault
                      parameters:
                      - name: myBool
                        in: query
                        schema:
                          type: boolean
                          default: true
                      - name: myInt
                        in: query
                        schema:
                          type: integer
                          format: int32
                          default: 1
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/testsize:
                    get:
                      operationId: testSize
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              type: array
                              items:
                                type: string
                              maxItems: 100
                              minItems: 1
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /test/teststringsize:
                    get:
                      operationId: testStringSize
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              type: string
                              maxLength: 50
                              minLength: 1
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    DefaultClass:
                      type: object
                      properties:
                        name:
                          type: boolean
                          default: true
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "test explode FALSE")
    public void testTicket4065() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration();
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4065Resource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /bar:
                    get:
                      operationId: test
                      parameters:
                      - name: blub
                        in: query
                        explode: false
                        schema:
                          type: array
                          items:
                            type: integer
                            format: int64
                      responses:
                        default:
                          description: default response
                          content:
                            application/json: {}
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test(description = "Extensions Tests OAS 3.1")
    public void testExtensionsOAS31() {
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI31(true);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4850Resource.class);
        assertNotNull(openAPI);

        String yaml = """
                openapi: 3.1.0
                paths:
                  /bar:
                    get:
                      operationId: test
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/ExtensionsResource"
                components:
                  schemas:
                    ExtensionsResource:
                      description: ExtensionsResource
                      x-user:
                        name: Josh
                      user-extensions:
                        lastName: Hart
                        address: House""";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }
    @Test(description = "Test model resolution for global path parameters with openAPI 3.1")
    public void testTicket4878() {
        ModelConverters.reset();
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI31(true);
        Reader reader = new Reader(config);

        OpenAPI openAPI = reader.read(Ticket4878Resource.class);
        String yaml = """
                openapi: 3.1.0
                paths:
                  /{globalPathParam}/{localPathParam}:
                    get:
                      operationId: getMethod
                      parameters:
                      - name: globalPathParam
                        in: path
                        required: true
                        schema:
                          type: string
                          $comment: 3.1 property for global path param
                      - name: localPathParam
                        in: path
                        required: true
                        schema:
                          type: string
                          $comment: 3.1 property for local path param
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                """;
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
        String yaml = """
                openapi: 3.1.0
                paths:
                  /{globalPathParam}/{localPathParam}:
                    get:
                      operationId: getMethod
                      parameters:
                      - name: globalPathParam
                        in: path
                        required: true
                        schema:
                          type: string
                          $comment: 3.1 property for global path param
                      - name: localPathParam
                        in: path
                        required: true
                        schema:
                          type: string
                          $comment: 3.1 property for local path param
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);

        // openAPI31 true and openAPI set
        config.setOpenAPI(new OpenAPI().openapi("3.1.1"));
        reader = new Reader(config);
        openAPI = reader.read(Ticket4878Resource.class);
        yaml = """
                openapi: 3.1.1
                paths:
                  /{globalPathParam}/{localPathParam}:
                    get:
                      operationId: getMethod
                      parameters:
                      - name: globalPathParam
                        in: path
                        required: true
                        schema:
                          type: string
                          $comment: 3.1 property for global path param
                      - name: localPathParam
                        in: path
                        required: true
                        schema:
                          type: string
                          $comment: 3.1 property for local path param
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);

        // openAPI31 true and openAPIVersion set
        config.setOpenAPI(null);
        config.setOpenAPIVersion("3.1.1");
        reader = new Reader(config);
        openAPI = reader.read(Ticket4878Resource.class);
        yaml = """
                openapi: 3.1.1
                paths:
                  /{globalPathParam}/{localPathParam}:
                    get:
                      operationId: getMethod
                      parameters:
                      - name: globalPathParam
                        in: path
                        required: true
                        schema:
                          type: string
                          $comment: 3.1 property for global path param
                      - name: localPathParam
                        in: path
                        required: true
                        schema:
                          type: string
                          $comment: 3.1 property for local path param
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                """;
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

    @Test
    void testTicket5017() {
        ModelResolver.enumsAsRef = true;
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI31(true);
        Reader reader = new Reader(config);
        OpenAPI openAPI = reader.read(Ticket5017Resource.class);

        OpenAPISpecFilter filterImpl = new RemoveUnusedSchemasOAS31Filter();
        SpecFilter f = new SpecFilter();
        openAPI = f.filter(openAPI, filterImpl, null, null, null);

        String yaml = """
                openapi: 3.1.0
                paths:
                  /test:
                    get:
                      operationId: myMethod
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/Example"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    Example:
                      type: object
                      properties:
                        myMap:
                          type: object
                          additionalProperties:
                            type: string
                          propertyNames:
                            $ref: "#/components/schemas/MyEnum"
                    MyEnum:
                      type: string
                      enum:
                      - FOO
                      - BAR
                """;
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
        ModelResolver.enumsAsRef = false;
    }

    static class RemoveUnusedSchemasOAS31Filter extends AbstractSpecFilter {
        @Override
        public boolean isRemovingUnreferencedDefinitions() {
            return true;
        }

        @Override
        public boolean isOpenAPI31Filter() {
            return true;
        }
    }

    @Test(description = "generic resource with 3-param method resolves type variable")
    public void testGenericResourceWithThreeParamMethodResolvesTypeVariable() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(GenericPetCreateResource.class);

        assertNotNull(openAPI.getPaths());
        PathItem pathItem = openAPI.getPaths().get("/generic-pets");
        assertNotNull(pathItem);
        assertNotNull(pathItem.getPost());
        assertNotNull(pathItem.getPost().getRequestBody());

        io.swagger.v3.oas.models.media.Schema bodySchema = pathItem.getPost().getRequestBody()
                .getContent().get("application/json").getSchema();
        assertNotNull(bodySchema);
        String ref = bodySchema.get$ref();
        assertTrue(ref != null && ref.contains("GenericPet"),
                "T must resolve to GenericPet, not Object; got: " + ref);
    }

    abstract static class GenericBaseResource<T> {
        @POST
        @Path("/generic-pets")
        @Consumes("application/json")
        public abstract void create(T body, @HeaderParam("x-header") String header, @QueryParam("dryRun") boolean dryRun);
    }

    static class GenericPetCreateResource extends GenericBaseResource<GenericPet> {
        @Override
        public void create(GenericPet body, String header, boolean dryRun) {
        }
    }

    static class GenericPet {
        public String name;
        public String getName() { return name; }
    }
}
