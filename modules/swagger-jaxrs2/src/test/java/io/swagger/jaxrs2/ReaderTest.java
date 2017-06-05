package io.swagger.jaxrs2;

import io.swagger.jaxrs2.resources.*;

import io.swagger.oas.models.*;
import io.swagger.oas.models.callbacks.Callback;
import io.swagger.oas.models.callbacks.Callbacks;
import io.swagger.oas.models.links.Link;
import io.swagger.oas.models.links.LinkParameters;
import io.swagger.oas.models.media.Content;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.parameters.RequestBody;
import io.swagger.oas.models.responses.ApiResponse;
import io.swagger.oas.models.responses.ApiResponses;
import io.swagger.oas.models.security.SecurityRequirement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;


public class ReaderTest {
    private static final String EXAMPLE_TAG = "Example Tag";
    private static final String SECOND_TAG = "Second Tag";
    private static final String OPERATION_SUMMARY = "Operation Summary";
    private static final String OPERATION_DESCRIPTION = "Operation Description";
    private static final String CALLBACK_POST_OPERATION_DESCRIPTION = "payload data will be sent";
    private static final String CALLBACK_GET_OPERATION_DESCRIPTION = "payload data will be received";
    private static final String APPLICATION_JSON = "application/json";
    private static final String RESPONSE_CODE_200 = "200";
    private static final String RESPONSE_CODE_DEFAULT = "default";
    private static final String RESPONSE_DESCRIPTION = "voila!";
    private static final String RESPONSE_DESCRIPTION_BOO = "boo";
    private static final String REQUEST_DESCRIPTION = "Request description";
    private static final String EXTERNAL_DOCS_DESCRIPTION = "External documentation description";
    private static final String EXTERNAL_DOCS_URL = "http://url.com";
    private static final String GENERIC_MEDIA_TYPE = "*/*";
    private static final String PARAMETER_IN = "path";
    private static final String PARAMETER_NAME = "subscriptionId";
    private static final String PARAMETER_DESCRIPTION = "parameter description";
    private static final String SCHEMA_TYPE = "string";
    private static final String SCHEMA_FORMAT = "uuid";
    private static final String SCHEMA_DESCRIPTION = "the generated UUID";
    private static final String CALLBACK_SUBSCRIPTION_ID = "subscription";
    private static final String SECURITY_KEY = "security_key";
    private static final String SCOPE_VALUE = "write:petsread:pets";
    private static final String LINK_DESCRIPTION = "Link Description";
    private static final String LINK_OPERATION_REF = "Operation Ref";
    private static final String LINK_OPERATION_ID = "Operation Id";
    private static final String OPERATION_ID = "operationId";

    private static final int RESPONSES_NUMBER = 2;
    private static final int TAG_NUMBER = 2;
    private static final int CALLBACK_NUMBER = 1;
    private static final int PARAMETER_NUMBER = 1;
    private static final int SECURITY_REQUIREMENT_NUMBER = 1;
    private static final int SCOPE_NUMBER = 1;
    private static final int PATHS_NUMBER = 1;


    @Test(description = "test a simple resource class")
    public void testSimpleReadClass() {
        Reader reader = new Reader(new OpenAPI(), null);
        OpenAPI openAPI = reader.read(BasicFieldsResource.class);
        Paths paths = openAPI.getPaths();
        assertEquals(PATHS_NUMBER, paths.size());
        PathItem pathItem = paths.get(OPERATION_ID);
        assertNotNull(pathItem);
        assertEquals(OPERATION_DESCRIPTION, pathItem.getDescription());
        assertEquals(OPERATION_SUMMARY, pathItem.getSummary());
        assertNull(pathItem.getPost());
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        assertEquals(OPERATION_SUMMARY, operation.getSummary());
        assertEquals(OPERATION_DESCRIPTION, operation.getDescription());
    }

    @Test(description = "scan methods")
    public void testCompleteReadClass() {
        Reader reader = new Reader(new OpenAPI(), null);
        OpenAPI openAPI = reader.read(CompleteFieldsResource.class);
        Paths paths = openAPI.getPaths();
        assertEquals(PATHS_NUMBER, paths.size());
        PathItem pathItem = paths.get(OPERATION_ID);
        assertNotNull(pathItem);
        assertEquals(OPERATION_DESCRIPTION, pathItem.getDescription());
        assertEquals(OPERATION_SUMMARY, pathItem.getSummary());
        assertNull(pathItem.getPost());
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        assertEquals(OPERATION_SUMMARY, operation.getSummary());
        assertEquals(OPERATION_DESCRIPTION, operation.getDescription());

        assertEquals(TAG_NUMBER, operation.getTags().size());
        assertEquals(EXAMPLE_TAG, operation.getTags().get(0));
        assertEquals(SECOND_TAG, operation.getTags().get(1));

        RequestBody requestBody = operation.getRequestBody();
        assertEquals(REQUEST_DESCRIPTION, requestBody.getDescription());

        Content content = requestBody.getContent();
        assertNotNull(content);
        assertNotNull(content.get(APPLICATION_JSON));

        ExternalDocumentation externalDocs = operation.getExternalDocs();
        assertEquals(EXTERNAL_DOCS_DESCRIPTION, externalDocs.getDescription());
        assertEquals(EXTERNAL_DOCS_URL, externalDocs.getUrl());
    }


    @Test(description = "scan methods")
    public void testScanMethods() {
        Reader reader = new Reader(new OpenAPI(), null);
        Method[] methods = SimpleMethods.class.getMethods();
        for (final Method method : methods) {
            if (isValidRestPath(method)) {
                Operation operation = reader.parseMethod(method);
                assertNotNull(operation);
            }
        }
    }

    @Test(description = "Get a Summary and Description")
    public void testGetSummaryAndDescription() {
        Reader reader = new Reader(new OpenAPI(), null);
        Method[] methods = BasicFieldsResource.class.getMethods();
        Operation operation = reader.parseMethod(methods[0]);
        assertNotNull(operation);
        assertEquals(OPERATION_SUMMARY, operation.getSummary());
        assertEquals(OPERATION_DESCRIPTION, operation.getDescription());
    }

    @Test(description = "Get the Param of an operation")
    public void testSubscriptionIdParam() {
        Reader reader = new Reader(new OpenAPI(), null);
        OpenAPI openAPI = reader.read(DuplicatedOperationIdResource.class);
    }

    @Test(description = "Do nothing")
    public void testBasicEmptyOperation() {
        Reader reader = new Reader(new OpenAPI(), null);
        Method[] methods = BasicClass.class.getMethods();
        Operation operation = reader.parseMethod(methods[0]);
        assertNull(operation.getCallbacks());
        assertNull(operation.getTags());
        assertNull(operation.getExternalDocs());
        assertNull(operation.getOperationId());
        assertNull(operation.getParameters());
        assertNull(operation.getResponses());
        assertNull(operation.getSecurity());
        assertNull(operation.getServers());
        assertNull(operation.getSummary());
        assertNull(operation.getDescription());
    }

    @Test(description = "Get a Duplicated Operation Id")
    public void testResolveDuplicatedOperationId() {
        Reader reader = new Reader(new OpenAPI(), null);
        OpenAPI openAPI = reader.read(DuplicatedOperationIdResource.class);

        Paths paths = openAPI.getPaths();
        Operation firstOperation = paths.get("operationId").getGet();
        Operation secondOperation = paths.get("operationId_1").getGet();
        assertNotNull(firstOperation);
        assertNotNull(secondOperation);
        assertNotEquals(firstOperation.getOperationId(), secondOperation.getOperationId());
    }

    @Test(description = "Deprecated Method")
    public void testDeprecatedMethod() {
        Reader reader = new Reader(new OpenAPI(), null);
        Method[] methods = DeprecatedFieldsResource.class.getMethods();
        Operation deprecatedOperation = reader.parseMethod(methods[0]);
        assertNotNull(deprecatedOperation);
        assertTrue(deprecatedOperation.getDeprecated());
    }

    @Test(description = "Get tags")
    public void testGetTags() {
        Reader reader = new Reader(new OpenAPI(), null);
        Method[] methods = TagsResource.class.getMethods();
        Operation operation = reader.parseMethod(methods[0]);
        assertNotNull(operation);
        assertEquals(TAG_NUMBER, operation.getTags().size());
        assertEquals(EXAMPLE_TAG, operation.getTags().get(0));
        assertEquals(SECOND_TAG, operation.getTags().get(1));
    }

    @Test(description = "Responses")
    public void testGetResponses() {
        Reader reader = new Reader(new OpenAPI(), null);

        Method[] methods = ResponsesResource.class.getMethods();

        Operation responseOperation = reader.parseMethod(methods[0]);
        assertNotNull(responseOperation);

        ApiResponses responses = responseOperation.getResponses();
        assertEquals(RESPONSES_NUMBER, responses.size());

        ApiResponse apiResponse = responses.get(RESPONSE_CODE_200);
        assertNotNull(apiResponse);
        assertEquals(RESPONSE_DESCRIPTION, apiResponse.getDescription());
    }

    @Test(description = "Request Body")
    public void testGetRequestBody() {
        Reader reader = new Reader(new OpenAPI(), null);

        Method[] methods = RequestBodyResource.class.getMethods();

        Operation requestOperation = reader.parseMethod(methods[0]);
        assertNotNull(requestOperation);
        RequestBody requestBody = requestOperation.getRequestBody();
        assertEquals(REQUEST_DESCRIPTION, requestBody.getDescription());

        Content content = requestBody.getContent();
        assertNotNull(content);
        assertNotNull(content.get(APPLICATION_JSON));

    }

    @Test(description = "External Docs")
    public void testGetExternalDocs() {
        Reader reader = new Reader(new OpenAPI(), null);

        Method[] methods = ExternalDocsReference.class.getMethods();
        Operation externalDocsOperation = reader.parseMethod(methods[0]);
        assertNotNull(externalDocsOperation);
        ExternalDocumentation externalDocs = externalDocsOperation.getExternalDocs();
        assertEquals(EXTERNAL_DOCS_DESCRIPTION, externalDocs.getDescription());
        assertEquals(EXTERNAL_DOCS_URL, externalDocs.getUrl());
    }

    @Test(description = "Parameters")
    public void testGetParameters() {
        Reader reader = new Reader(new OpenAPI(), null);

        Method[] methods = ParametersResource.class.getMethods();

        Operation parametersOperation = reader.parseMethod(methods[0]);
        assertNotNull(parametersOperation);

        List<Parameter> parameters = parametersOperation.getParameters();
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
        assertEquals(Boolean.FALSE, parameter.getDeprecated());
        Schema schema = parameter.getSchema();
        assertNotNull(schema);
        assertEquals(SCHEMA_TYPE, schema.getType());
        assertEquals(SCHEMA_FORMAT, schema.getFormat());
        assertEquals(SCHEMA_DESCRIPTION, schema.getDescription());
        assertEquals(Boolean.TRUE, schema.getReadOnly());
    }

    @Test(description = "Security Requirement")
    public void testSecurityRequirement() {
        Reader reader = new Reader(new OpenAPI(), null);

        Method[] methods = SecurityResource.class.getMethods();

        Operation securityOperation = reader.parseMethod(methods[0]);
        assertNotNull(securityOperation);
        List<SecurityRequirement> securityRequirements = securityOperation.getSecurity();
        assertNotNull(securityRequirements);
        assertEquals(SECURITY_REQUIREMENT_NUMBER, securityRequirements.size());
        List<String> scopes = securityRequirements.get(0).get(SECURITY_KEY);
        assertNotNull(scopes);
        assertEquals(SCOPE_NUMBER, scopes.size());
        assertEquals(SCOPE_VALUE, scopes.get(0));
    }


    @Test(description = "Callbacks")
    public void testGetCallbacks() {
        Reader reader = new Reader(new OpenAPI(), null);

        Method[] methods = SimpleCallbackResource.class.getMethods();
        Operation callbackOperation = reader.parseMethod(methods[0]);
        assertNotNull(callbackOperation);
        Callbacks callbacks = callbackOperation.getCallbacks();
        assertNotNull(callbacks);
        Callback callback = callbacks.get(CALLBACK_SUBSCRIPTION_ID);
        assertNotNull(callback);
        PathItem pathItem = callback.get(CALLBACK_SUBSCRIPTION_ID);
        assertNotNull(pathItem);
        Operation postOperation = pathItem.getPost();
        assertNotNull(postOperation);
        assertEquals(CALLBACK_POST_OPERATION_DESCRIPTION, postOperation.getDescription());

        List<Parameter> parameters = postOperation.getParameters();
        assertNotNull(parameters);
        assertEquals(CALLBACK_NUMBER, parameters.size());

        Operation getOperation = pathItem.getGet();
        assertNotNull(getOperation);
        assertEquals(CALLBACK_GET_OPERATION_DESCRIPTION, getOperation.getDescription());

        Operation putOperation = pathItem.getPut();
        assertNotNull(putOperation);
        assertEquals(CALLBACK_POST_OPERATION_DESCRIPTION, putOperation.getDescription());

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
