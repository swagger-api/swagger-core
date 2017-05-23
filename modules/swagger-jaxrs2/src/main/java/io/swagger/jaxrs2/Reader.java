package io.swagger.jaxrs2;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.oas.annotations.media.ExampleObject;
import io.swagger.oas.models.ExternalDocumentation;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.PathItem;
import io.swagger.oas.models.callbacks.Callback;
import io.swagger.oas.models.callbacks.Callbacks;
import io.swagger.oas.models.examples.Example;
import io.swagger.oas.models.links.Link;
import io.swagger.oas.models.links.LinkParameters;
import io.swagger.oas.models.media.Content;
import io.swagger.oas.models.media.MediaType;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.parameters.RequestBody;
import io.swagger.oas.models.responses.ApiResponse;
import io.swagger.oas.models.responses.ApiResponses;
import io.swagger.oas.models.servers.Server;
import io.swagger.oas.models.servers.ServerVariable;
import io.swagger.oas.models.servers.ServerVariables;
import io.swagger.util.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Reader {
    private OpenAPI openAPI;

    public static final String GET_METHOD = "get";
    public static final String POST_METHOD = "post";
    public static final String PUT_METHOD = "put";
    public static final String DELETE_METHOD = "delete";
    public static final String PATCH_METHOD = "patch";
    public static final String TRACE_METHOD = "trace";
    public static final String HEAD_METHOD = "head";
    public static final String OPTIONS_METHOD = "options";

    public Reader(OpenAPI openAPI) {
        this.openAPI = openAPI;
    }

    public Operation parseMethod(Method method) {
        JavaType classType = TypeFactory.defaultInstance().constructType(method.getDeclaringClass());
        BeanDescription bd = new ObjectMapper().getSerializationConfig().introspect(classType);
        return parseMethod(classType.getClass(), method, bd.findMethod(method.getName(), method.getParameterTypes()),
                Collections.<Parameter>emptyList(), Collections.<ApiResponse>emptyList());
    }

    private Operation parseMethod(Class<?> cls, Method method, AnnotatedMethod annotatedMethod,
                                  List<Parameter> globalParameters, List<ApiResponse> classApiResponses) {
        Operation operation = new Operation();
        io.swagger.oas.annotations.Operation apiOperation = ReflectionUtils.getAnnotation(method, io.swagger.oas.annotations.Operation.class);
        io.swagger.oas.annotations.callbacks.Callback apiCallback = ReflectionUtils.getAnnotation(method, io.swagger.oas.annotations.callbacks.Callback.class);

        if (apiOperation != null) {
            if (apiCallback != null) {
                operation.setCallbacks(getCallbacksObjectFromAnnotation(apiCallback));
            }

            setOperationObjectFromApiOperationAnnotation(operation, apiOperation);
        }

        return operation;
    }

    private Callbacks getCallbacksObjectFromAnnotation(io.swagger.oas.annotations.callbacks.Callback apiCallback) {
        Callbacks callbacksObject = new Callbacks();
        Callback callbackObject = new Callback();
        PathItem pathItemObject = new PathItem();

        for (io.swagger.oas.annotations.Operation callbackOperation : apiCallback.operation()) {
            Operation callbackNewOperation = new Operation();

            setOperationObjectFromApiOperationAnnotation(callbackNewOperation, callbackOperation);

            switch (callbackOperation.method()) {
                case POST_METHOD:
                    pathItemObject.post(callbackNewOperation);
                    break;
                case GET_METHOD:
                    pathItemObject.get(callbackNewOperation);
                    break;
                case DELETE_METHOD:
                    pathItemObject.delete(callbackNewOperation);
                    break;
                case PUT_METHOD:
                    pathItemObject.put(callbackNewOperation);
                    break;
                case PATCH_METHOD:
                    pathItemObject.patch(callbackNewOperation);
                    break;
                case TRACE_METHOD:
                    pathItemObject.trace(callbackNewOperation);
                    break;
                case HEAD_METHOD:
                    pathItemObject.head(callbackNewOperation);
                    break;
                case OPTIONS_METHOD:
                    pathItemObject.options(callbackNewOperation);
                    break;

            }
        }

        pathItemObject.setRef(apiCallback.callbackUrlExpression());
        // pathItemObject.setDescription(apiCallback.name());
        // pathItemObject.setSummary(apiCallback.name());

        callbackObject.addPathItem(apiCallback.name(), pathItemObject);
        callbacksObject.addCallback(apiCallback.name(), callbackObject);

        return callbacksObject;
    }

    private void setOperationObjectFromApiOperationAnnotation(Operation operation, io.swagger.oas.annotations.Operation apiOperation) {
        //Add a Summary and the Description
        operation.setDescription(apiOperation.description());
        operation.setSummary(apiOperation.summary());

        // Set Operation Id
        operation.setOperationId(apiOperation.operationId());

        //Set Deprecated
        operation.setDeprecated(apiOperation.deprecated());

        operation.setResponses(getApiResponsesFromResponseAnnotation(apiOperation.responses()));

        operation.requestBody(getRequestBodyObjectFromAnnotation(apiOperation.requestBody()));

        operation.setExternalDocs(getExternalDocumentationObjectFromAnnotation(apiOperation.externalDocs()));

        operation.setServers(getServersObjectListFromAnnotation(apiOperation.servers()));

        operation.setTags(getTagsFromOperation(apiOperation.tags()));

        operation.setParameters(getParametersListFromAnnotation(apiOperation.parameters()));
    }

    public List<Parameter> getParametersListFromAnnotation(io.swagger.oas.annotations.Parameter[] parameters) {
        List<Parameter> parametersObject = new ArrayList<>();
        for (io.swagger.oas.annotations.Parameter parameter : parameters) {
            Parameter parameterObject = new Parameter();
            parameterObject.setDescription(parameter.description());
            parameterObject.setDeprecated(parameter.deprecated());
            parameterObject.setName(parameter.name());
            parameterObject.setRequired(parameter.required());
            parameterObject.setStyle(StringUtils.isNoneBlank(parameter.style()) ? Parameter.StyleEnum.valueOf(parameter.style()) : null);
            parameterObject.setAllowEmptyValue(parameter.allowEmptyValue());
            parameterObject.setAllowReserved(parameter.allowReserved());
            parameterObject.setExplode(parameter.explode());
            parameterObject.setIn(parameter.in());
            parameterObject.setContent(getContents(parameter.content()));

            io.swagger.oas.annotations.media.Schema schema = parameter.schema();
            if (schema != null) {
                parameterObject.setSchema(getSchemaFromAnnotation(schema));
            }
            parametersObject.add(parameterObject);

        }
        return parametersObject;
    }

    private Schema getSchemaFromAnnotation(io.swagger.oas.annotations.media.Schema schema) {
        Schema schemaObject = new Schema();
        schemaObject.setDescription(schema.description());
        schemaObject.setType(schema.type());
        schemaObject.set$ref(schema.ref());
        schemaObject.setDeprecated(schema.deprecated());
        schemaObject.setReadOnly(schema.readOnly());
        schemaObject.setReadOnly(schema.readOnly());
        schemaObject.setExclusiveMaximum(schema.exclusiveMaximum());
        schemaObject.setExclusiveMinimum(schema.exclusiveMinimum());
        schemaObject.setFormat(schema.format());
        //TODO complete the Schema Object
        return schemaObject;
    }

    private List<String> getTagsFromOperation(String[] tags) {
        List<String> openApiTags = new ArrayList<>();
        for (String tag : tags) {
            openApiTags.add(tag);
        }
        return openApiTags;
    }

    private List<Server> getServersObjectListFromAnnotation(io.swagger.oas.annotations.servers.Server[] servers) {
        List<Server> serverObjects = new ArrayList<>();

        for (io.swagger.oas.annotations.servers.Server server : servers) {
            Server serverObject = new Server();
            serverObject.setUrl(server.url());
            serverObject.setDescription(server.description());
            io.swagger.oas.annotations.servers.ServerVariable[] serverVariables = server.variables();
            ServerVariables serverVariablesObject = new ServerVariables();
            for (io.swagger.oas.annotations.servers.ServerVariable serverVariable : serverVariables) {
                ServerVariable serverVariableObject = new ServerVariable();
                serverVariableObject.setDescription(serverVariable.description());
                serverVariablesObject.addServerVariable(serverVariableObject.getDescription(), serverVariableObject);
            }

            serverObject.setVariables(serverVariablesObject);
        }
        return serverObjects;
    }

    private ExternalDocumentation getExternalDocumentationObjectFromAnnotation(io.swagger.oas.annotations.ExternalDocumentation externalDocumentation) {
        ExternalDocumentation external = new ExternalDocumentation();
        external.setDescription(externalDocumentation.description());
        external.setUrl(externalDocumentation.url());
        return external;
    }

    private RequestBody getRequestBodyObjectFromAnnotation(io.swagger.oas.annotations.parameters.RequestBody requestBody) {
        RequestBody requestBodyObject = new RequestBody();
        requestBodyObject.setDescription(requestBody.description());
        requestBodyObject.setRequired(requestBody.required());
        requestBodyObject.setContent(getContents(requestBody.content()));
        return requestBodyObject;
    }

    public ApiResponses getApiResponsesFromResponseAnnotation(final io.swagger.oas.annotations.responses.ApiResponse[] responses) {
        ApiResponses apiResponses = new ApiResponses();
        for (io.swagger.oas.annotations.responses.ApiResponse response : responses) {
            ApiResponse apiResponse = new ApiResponse();
            Content content = getContent(response.content());
            apiResponse.content(content);
            apiResponse.setDescription(response.description());

            apiResponses.addApiResponse(response.responseCode(), apiResponse);
        }
        return apiResponses;
    }

    private Content getContents(io.swagger.oas.annotations.media.Content contents[]) {
        Content contentObject = new Content();
        for (io.swagger.oas.annotations.media.Content content : contents) {
            ExampleObject examples[] = content.examples();
            for (ExampleObject example : examples) {
                MediaType mediaType = getMediaType(example);
                contentObject.addMediaType(content.mediaType(), mediaType);
            }
        }
        return contentObject;
    }

    private Content getContent(io.swagger.oas.annotations.media.Content annotationContent) {
        Content content = new Content();
        if (annotationContent != null) {
            ExampleObject examples[] = annotationContent.examples();
            for (ExampleObject example : examples) {
                MediaType mediaType = getMediaType(example);
                content.addMediaType(annotationContent.mediaType(), mediaType);
            }
        }
        return content;
    }

    private MediaType getMediaType(ExampleObject example) {
        MediaType mediaType = new MediaType();
        Example exampleObject = new Example();
        exampleObject.setDescription(example.name());
        exampleObject.setSummary(example.summary());
        exampleObject.setExternalValue(example.externalValue());
        exampleObject.setValue(example.value());
        mediaType.addExamples(example.name(), exampleObject);
        return mediaType;
    }

    private Link getLinks(io.swagger.oas.annotations.links.Link[] links) {
        Link linkObject = new Link();
        for (io.swagger.oas.annotations.links.Link link : links) {
            linkObject.setParameters(getLinkParameters(link.parameters()));
            linkObject.setDescription(link.description());
            linkObject.setOperationId(link.operationId());
            linkObject.setOperationRef(link.operationRef());
        }
        return linkObject;
    }

    private LinkParameters getLinkParameters(io.swagger.oas.annotations.links.LinkParameters linkParameters) {
        LinkParameters linkParametersObject = new LinkParameters();
        linkParametersObject.addExtension(linkParameters.name(), linkParameters.expression());
        return linkParametersObject;
    }
}