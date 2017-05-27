package io.swagger.jaxrs2;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.oas.annotations.media.ExampleObject;
import io.swagger.oas.models.*;
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
import io.swagger.oas.models.security.SecurityRequirement;
import io.swagger.oas.models.security.SecurityScheme;
import io.swagger.oas.models.servers.Server;
import io.swagger.oas.models.servers.ServerVariable;
import io.swagger.oas.models.servers.ServerVariables;
import io.swagger.util.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.*;


public class Reader {
    private final SecurityParser securityParser = new SecurityParser();
    private OpenAPI openAPI;

    private static final String GET_METHOD = "get";
    private static final String POST_METHOD = "post";
    private static final String PUT_METHOD = "put";
    private static final String DELETE_METHOD = "delete";
    private static final String PATCH_METHOD = "patch";
    private static final String TRACE_METHOD = "trace";
    private static final String HEAD_METHOD = "head";
    private static final String OPTIONS_METHOD = "options";

    public Reader(OpenAPI openAPI) {
        this.openAPI = openAPI;
    }

    public Operation parseMethod(Method method) {
        JavaType classType = TypeFactory.defaultInstance().constructType(method.getDeclaringClass());
        BeanDescription bd = new ObjectMapper().getSerializationConfig().introspect(classType);
        return parseMethod(classType.getClass(), method, bd.findMethod(method.getName(), method.getParameterTypes()),
                Collections.<Parameter>emptyList(), Collections.<ApiResponse>emptyList());
    }

    private void read(Class cl) {
        //TODO Class level Annotations
        io.swagger.oas.annotations.security.SecurityScheme apiSecurityScheme = ReflectionUtils.getAnnotation(cl, io.swagger.oas.annotations.security.SecurityScheme.class);

        Optional<SecurityScheme> securityScheme = securityParser.getSecurityScheme(apiSecurityScheme);
        Components components = new Components();
        if (securityScheme.isPresent()) {
            Map<String, SecurityScheme> securitySchemeMap = new HashMap();
            securitySchemeMap.put(securityScheme.get().getName(), securityScheme.get());
            components.setSecuritySchemes(securitySchemeMap);
        }
        openAPI.setComponents(components);

    }

    private Operation parseMethod(Class<?> cls, Method method, AnnotatedMethod annotatedMethod,
                                  List<Parameter> globalParameters, List<ApiResponse> classApiResponses) {
        Operation operation = new Operation();
        io.swagger.oas.annotations.Operation apiOperation = ReflectionUtils.getAnnotation(method, io.swagger.oas.annotations.Operation.class);
        io.swagger.oas.annotations.callbacks.Callback apiCallback = ReflectionUtils.getAnnotation(method, io.swagger.oas.annotations.callbacks.Callback.class);
        io.swagger.oas.annotations.security.SecurityRequirement apiSecurity = ReflectionUtils.getAnnotation(method, io.swagger.oas.annotations.security.SecurityRequirement.class);
        io.swagger.oas.annotations.links.Link apiLinks = ReflectionUtils.getAnnotation(method, io.swagger.oas.annotations.links.Link.class);

        if (apiOperation != null) {
            Optional<Callbacks> callbacksObjectFromAnnotation = getCallbacksObjectFromAnnotation(apiCallback);
            if (callbacksObjectFromAnnotation.isPresent()) {
                operation.setCallbacks(callbacksObjectFromAnnotation.get());
            }

            Optional<List<SecurityRequirement>> securityRequirementObjectFromAnnotation = securityParser.getSecurityRequirementObjectFromAnnotation(apiSecurity);
            if (securityRequirementObjectFromAnnotation.isPresent()) {
                operation.setSecurity(securityRequirementObjectFromAnnotation.get());
            }
            operation.setResponses(getApiResponsesFromResponseAnnotation(apiOperation.responses(), apiLinks).get());
            setOperationObjectFromApiOperationAnnotation(operation, apiOperation);
        }

        return operation;
    }


    private Optional<Callbacks> getCallbacksObjectFromAnnotation(io.swagger.oas.annotations.callbacks.Callback apiCallback) {
        if (apiCallback == null) {
            return Optional.empty();
        }
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
                default:
                    // Do nothing here
                    break;

            }
        }
        pathItemObject.setDescription(apiCallback.name());
        pathItemObject.setSummary(apiCallback.name());

        callbackObject.addPathItem(apiCallback.name(), pathItemObject);
        callbacksObject.addCallback(apiCallback.name(), callbackObject);

        return Optional.of(callbacksObject);
    }

    private void setOperationObjectFromApiOperationAnnotation(Operation operation, io.swagger.oas.annotations.Operation apiOperation) {
        operation.setTags(getTagsFromOperation(apiOperation.tags()).get());
        operation.setSummary(apiOperation.summary());
        operation.setDescription(apiOperation.description());
        operation.setExternalDocs(getExternalDocumentationObjectFromAnnotation(apiOperation.externalDocs()).get());
        operation.setOperationId(apiOperation.operationId());
        operation.setParameters(getParametersListFromAnnotation(apiOperation.parameters()).get());
        operation.setRequestBody(getRequestBodyObjectFromAnnotation(apiOperation.requestBody()).get());
        operation.setDeprecated(apiOperation.deprecated());
        operation.setServers(getServersObjectListFromAnnotation(apiOperation.servers()).get());
    }

    public Optional<List<Parameter>> getParametersListFromAnnotation(io.swagger.oas.annotations.Parameter[] parameters) {
        if (parameters == null) {
            return Optional.empty();
        }
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
            parameterObject.setContent(getContents(parameter.content()).get());

            io.swagger.oas.annotations.media.Schema schema = parameter.schema();
            parameterObject.setSchema(getSchemaFromAnnotation(schema).get());
            parametersObject.add(parameterObject);

        }
        return Optional.of(parametersObject);
    }

    private Optional<Schema> getSchemaFromAnnotation(io.swagger.oas.annotations.media.Schema schema) {
        if (schema == null) {
            return Optional.empty();
        }
        Schema schemaObject = new Schema();

        schemaObject.set$ref(schema.ref());
        schemaObject.setDefault(schema._default());
        schemaObject.setDeprecated(schema.deprecated());
        schemaObject.setDescription(schema.description());
        schemaObject.setEnum(getStringListFromStringArray(schema._enum()).get());
        schemaObject.setExample(schema.example());
        schemaObject.setExclusiveMaximum(schema.exclusiveMaximum());
        schemaObject.setExclusiveMinimum(schema.exclusiveMinimum());
        schemaObject.setExternalDocs(getExternalDocumentationObjectFromAnnotation(schema.externalDocs()).get());
        schemaObject.setFormat(schema.format());
        schemaObject.setPattern(schema.pattern());
        schemaObject.setMaxLength(schema.maxLength());
        schemaObject.setMaxProperties(schema.maxProperties());
        schemaObject.setMinLength(schema.minLength());
        schemaObject.setMinProperties(schema.minProperties());
        schemaObject.setReadOnly(schema.readOnly());
        schemaObject.setType(schema.type());
        schemaObject.setTitle(schema.title());
        schemaObject.setWriteOnly(schema.writeOnly());
        return Optional.of(schemaObject);
    }

    private Optional<List<String>> getStringListFromStringArray(String[] array) {
        if(array == null){
            return Optional.empty();
        }
        List<String> list = new ArrayList<>();
        for (String value : array) {
            list.add(value);
        }
        return Optional.of(list);
    }

    private Optional<List<String>> getTagsFromOperation(String[] tags) {
        if (tags == null) {
            Optional.empty();
        }
        List<String> openApiTags = new ArrayList<>();
        for (String tag : tags) {
            openApiTags.add(tag);
        }
        return Optional.of(openApiTags);
    }

    private Optional<List<Server>> getServersObjectListFromAnnotation(io.swagger.oas.annotations.servers.Server[] servers) {
        if (servers == null) {
            return Optional.empty();
        }
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
        return Optional.of(serverObjects);
    }

    private Optional<ExternalDocumentation> getExternalDocumentationObjectFromAnnotation(io.swagger.oas.annotations.ExternalDocumentation externalDocumentation) {
        if (externalDocumentation == null) {
            return Optional.empty();
        }
        ExternalDocumentation external = new ExternalDocumentation();
        external.setDescription(externalDocumentation.description());
        external.setUrl(externalDocumentation.url());
        return Optional.of(external);
    }

    private Optional<RequestBody> getRequestBodyObjectFromAnnotation(io.swagger.oas.annotations.parameters.RequestBody requestBody) {
        if (requestBody == null) {
            return Optional.empty();
        }
        RequestBody requestBodyObject = new RequestBody();
        requestBodyObject.setDescription(requestBody.description());
        requestBodyObject.setRequired(requestBody.required());
        requestBodyObject.setContent(getContents(requestBody.content()).get());
        return Optional.of(requestBodyObject);
    }

    public Optional<ApiResponses> getApiResponsesFromResponseAnnotation(final io.swagger.oas.annotations.responses.ApiResponse[] responses, io.swagger.oas.annotations.links.Link links) {
        if (responses == null) {
            return Optional.empty();
        }
        ApiResponses apiResponsesObject = new ApiResponses();
        for (io.swagger.oas.annotations.responses.ApiResponse response : responses) {
            ApiResponse apiResponseObject = new ApiResponse();
            Content content = getContent(response.content()).get();
            apiResponseObject.content(content);
            apiResponseObject.setDescription(response.description());
            Optional<Link> link = getLink(links);
            if (link.isPresent()) {
                apiResponseObject.setLinks(link.get());
            }
            apiResponsesObject.addApiResponse(response.responseCode(), apiResponseObject);
        }
        return Optional.of(apiResponsesObject);
    }

    private Optional<Content> getContents(io.swagger.oas.annotations.media.Content[] contents) {
        if (contents == null) {
            return Optional.empty();
        }
        Content contentObject = new Content();
        for (io.swagger.oas.annotations.media.Content content : contents) {
            ExampleObject[] examples = content.examples();
            for (ExampleObject example : examples) {
                Optional<MediaType> mediaType = getMediaType(example);
                if (mediaType.isPresent()) {
                    contentObject.addMediaType(content.mediaType(), mediaType.get());
                }
            }
        }
        return Optional.of(contentObject);
    }

    private Optional<Content> getContent(io.swagger.oas.annotations.media.Content annotationContent) {
        if (annotationContent == null) {
            Optional.empty();
        }
        Content content = new Content();
        if (annotationContent != null) {
            ExampleObject[] examples = annotationContent.examples();
            for (ExampleObject example : examples) {
                Optional<MediaType> mediaType = getMediaType(example);
                if (mediaType.isPresent()) {
                    content.addMediaType(annotationContent.mediaType(), mediaType.get());
                }
            }
        }
        return Optional.of(content);
    }

    private Optional<MediaType> getMediaType(ExampleObject example) {
        if (example == null) {
            return Optional.empty();
        }
        MediaType mediaType = new MediaType();
        Example exampleObject = new Example();
        exampleObject.setDescription(example.name());
        exampleObject.setSummary(example.summary());
        exampleObject.setExternalValue(example.externalValue());
        exampleObject.setValue(example.value());
        mediaType.addExamples(example.name(), exampleObject);
        return Optional.of(mediaType);
    }

    private Optional<Link> getLink(io.swagger.oas.annotations.links.Link link) {
        if (link == null) {
            return Optional.empty();
        }
        Link linkObject = new Link();
        linkObject.setParameters(getLinkParameters(link.parameters()).get());
        linkObject.setDescription(link.description());
        linkObject.setOperationId(link.operationId());
        linkObject.setOperationRef(link.operationRef());
        return Optional.of(linkObject);
    }

    private Optional<LinkParameters> getLinkParameters(io.swagger.oas.annotations.links.LinkParameters linkParameters) {
        if (linkParameters == null) {
            return Optional.empty();
        }
        LinkParameters linkParametersObject = new LinkParameters();
        linkParametersObject.addExtension(linkParameters.name(), linkParameters.expression());
        return Optional.of(linkParametersObject);
    }
}