package io.swagger.jaxrs2;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.jaxrs2.ext.OpenAPIExtension;
import io.swagger.jaxrs2.ext.OpenAPIExtensions;
import io.swagger.jaxrs2.util.ReaderUtils;
import io.swagger.oas.integration.ContextUtils;
import io.swagger.oas.integration.SwaggerConfiguration;
import io.swagger.oas.models.Components;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.PathItem;
import io.swagger.oas.models.Paths;
import io.swagger.oas.models.callbacks.Callback;
import io.swagger.oas.models.media.Content;
import io.swagger.oas.models.media.MediaType;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.parameters.RequestBody;
import io.swagger.oas.models.security.SecurityScheme;
import io.swagger.oas.models.tags.Tag;
import io.swagger.oas.integration.api.OpenAPIConfiguration;
import io.swagger.oas.integration.api.OpenApiReader;
import io.swagger.util.Json;
import io.swagger.util.ParameterProcessor;
import io.swagger.util.PathUtils;
import io.swagger.util.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class Reader implements OpenApiReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(Reader.class);
    public static final String DEFAULT_MEDIA_TYPE_VALUE = "*/*";

    protected OpenAPIConfiguration config;

    private OpenAPI openAPI;
    private Components components;
    private Paths paths;
    private Set<Tag> openApiTags;
    javax.ws.rs.Consumes classConsumes;
    javax.ws.rs.Produces classProduces;
    javax.ws.rs.Produces methodProduces;

    private static final String GET_METHOD = "get";
    private static final String POST_METHOD = "post";
    private static final String PUT_METHOD = "put";
    private static final String DELETE_METHOD = "delete";
    private static final String PATCH_METHOD = "patch";
    private static final String TRACE_METHOD = "trace";
    private static final String HEAD_METHOD = "head";
    private static final String OPTIONS_METHOD = "options";

    public Reader() {
        this.openAPI = new OpenAPI();
        paths = new Paths();
        openApiTags = new LinkedHashSet<>();
        components = new Components();

    }
    public Reader(OpenAPI openAPI) {
        this();
        setConfiguration(new SwaggerConfiguration().openAPI(openAPI));
    }

    public Reader(OpenAPIConfiguration openApiConfiguration) {
        this();
        setConfiguration(openApiConfiguration);
    }

    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    /**
     * Scans a single class for Swagger annotations - does not invoke ReaderListeners
     */
    public OpenAPI read(Class<?> cls) {
        return read(cls, "");
    }

    /**
     * Scans a set of classes for both ReaderListeners and OpenAPI annotations. All found listeners will
     * be instantiated before any of the classes are scanned for OpenAPI annotations - so they can be invoked
     * accordingly.
     *
     * @param classes a set of classes to scan
     * @return the generated OpenAPI definition
     */
    public OpenAPI read(Set<Class<?>> classes) {
        Set<Class<?>> sortedClasses = new TreeSet<>(new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> class1, Class<?> class2) {
                if (class1.equals(class2)) {
                    return 0;
                } else if (class1.isAssignableFrom(class2)) {
                    return -1;
                } else if (class2.isAssignableFrom(class1)) {
                    return 1;
                }
                return class1.getName().compareTo(class2.getName());
            }
        });
        sortedClasses.addAll(classes);

        for (Class<?> cls : sortedClasses) {
            read(cls, "");
        }

        return openAPI;
    }

    @Override
    public void setConfiguration(OpenAPIConfiguration openApiConfiguration) {
        if (openApiConfiguration != null) {
            this.config = ContextUtils.deepCopy(openApiConfiguration);
            if (openApiConfiguration.getOpenAPI() != null) {
                this.openAPI = this.config.getOpenAPI();
            }
        }
    }

    public OpenAPI read(Set<Class<?>> classes, Map<String, Object> resources) {
        return read(classes);
    }

    public OpenAPI read(Class<?> cls, String parentPath) {
        io.swagger.oas.annotations.security.SecurityScheme apiSecurityScheme = ReflectionUtils.getAnnotation(cls, io.swagger.oas.annotations.security.SecurityScheme.class);
        io.swagger.oas.annotations.ExternalDocumentation apiExternalDocs = ReflectionUtils.getAnnotation(cls, io.swagger.oas.annotations.ExternalDocumentation.class);
        io.swagger.oas.annotations.info.Info apiInfo = ReflectionUtils.getAnnotation(cls, io.swagger.oas.annotations.info.Info.class);
        classConsumes = ReflectionUtils.getAnnotation(cls, javax.ws.rs.Consumes.class);
        classProduces = ReflectionUtils.getAnnotation(cls, javax.ws.rs.Produces.class);

        Optional<SecurityScheme> securityScheme = SecurityParser.getSecurityScheme(apiSecurityScheme);
        if (securityScheme.isPresent()) {
            Map<String, SecurityScheme> securitySchemeMap = new HashMap<>();
            if (StringUtils.isNotBlank(securityScheme.get().getName())) {
                securitySchemeMap.put(securityScheme.get().getName(), securityScheme.get());
                if (components.getSecuritySchemes() != null && components.getSecuritySchemes().size() != 0) {
                    components.getSecuritySchemes().putAll(securitySchemeMap);
                } else {
                    components.setSecuritySchemes(securitySchemeMap);
                }
            }
        }

        final javax.ws.rs.Path apiPath = ReflectionUtils.getAnnotation(cls, javax.ws.rs.Path.class);

        JavaType classType = TypeFactory.defaultInstance().constructType(cls);
        BeanDescription bd = Json.mapper().getSerializationConfig().introspect(classType);

        final List<Parameter> globalParameters = new ArrayList<>();

        // look for constructor-level annotated properties
        globalParameters.addAll(ReaderUtils.collectConstructorParameters(cls, openAPI));

        // look for field-level annotated properties
        globalParameters.addAll(ReaderUtils.collectFieldParameters(cls, openAPI));

        Method methods[] = cls.getMethods();
        for (Method method : methods) {
            RequestBody requestBody = new RequestBody();
            AnnotatedMethod annotatedMethod = bd.findMethod(method.getName(), method.getParameterTypes());
            methodProduces = ReflectionUtils.getAnnotation(method, javax.ws.rs.Produces.class);

            if (ReflectionUtils.isOverriddenMethod(method, cls)) {
                continue;
            }
            javax.ws.rs.Path methodPath = ReflectionUtils.getAnnotation(method, javax.ws.rs.Path.class);
            javax.ws.rs.Consumes methodConsumes = ReflectionUtils.getAnnotation(method, javax.ws.rs.Consumes.class);

            String operationPath = ReaderUtils.getPath(apiPath, methodPath, parentPath);

            Map<String, String> regexMap = new LinkedHashMap<>();
            operationPath = PathUtils.parsePath(operationPath, regexMap);
            if (operationPath != null) {
                if (ReaderUtils.isIgnored(operationPath, config)) {
                    continue;
                }

                Operation operation = parseMethod(method);
                if (operation != null) {
                    PathItem pathItemObject;
                    if (openAPI.getPaths() != null && openAPI.getPaths().get(operationPath) != null) {
                        pathItemObject = openAPI.getPaths().get(operationPath);
                    } else {
                        pathItemObject = new PathItem();
                    }

                    String httpMethod = ReaderUtils.extractOperationMethod(operation, method, OpenAPIExtensions.chain());
                    if (StringUtils.isNotBlank(httpMethod)) {
                        setPathItemOperation(pathItemObject, httpMethod, operation);
                    }
                    List<Parameter> operationParameters = new ArrayList<>();
                    Annotation[][] paramAnnotations = ReflectionUtils.getParameterAnnotations(method);
                    if (annotatedMethod == null) {
                        Type[] genericParameterTypes = method.getGenericParameterTypes();
                        for (int i = 0; i < genericParameterTypes.length; i++) {
                            final Type type = TypeFactory.defaultInstance().constructType(genericParameterTypes[i], cls);
                            operationParameters.addAll(getParameters(type, Arrays.asList(paramAnnotations[i])));
                        }
                    } else {
                        for (int i = 0; i < annotatedMethod.getParameterCount(); i++) {
                            AnnotatedParameter param = annotatedMethod.getParameter(i);
                            final Type type = TypeFactory.defaultInstance().constructType(param.getParameterType(), cls);
                            List<Parameter> parameters = getParameters(type, Arrays.asList(paramAnnotations[i]));
                            for (Parameter parameter : parameters) {
                                if (StringUtils.isNotBlank(parameter.getIn())) {
                                    operationParameters.add(parameter);
                                } else {
                                    boolean isRequestBodyEmpty = true;
                                    if (StringUtils.isNotBlank(parameter.get$ref())) {
                                        requestBody.set$ref(parameter.get$ref());
                                        isRequestBodyEmpty = false;
                                    }
                                    if (StringUtils.isNotBlank(parameter.getDescription())) {
                                        requestBody.setDescription(parameter.getDescription());
                                        isRequestBodyEmpty = false;
                                    }
                                    if (Boolean.TRUE.equals(parameter.getRequired())) {
                                        requestBody.setRequired(parameter.getRequired());
                                        isRequestBodyEmpty = false;
                                    }

                                    if (parameter.getSchema() != null) {
                                        Content content = new Content();
                                        if (methodConsumes != null) {
                                            for (String value : methodConsumes.value()) {
                                                setMediaTypeToContent(parameter.getSchema(), content, value);
                                            }
                                        } else if (classConsumes != null) {
                                            for (String value : classConsumes.value()) {
                                                setMediaTypeToContent(parameter.getSchema(), content, value);
                                            }
                                        } else {
                                            setMediaTypeToContent(parameter.getSchema(), content, DEFAULT_MEDIA_TYPE_VALUE);
                                        }

                                        requestBody.setContent(content);
                                        isRequestBodyEmpty = false;
                                    }
                                    if (!isRequestBodyEmpty) {
                                        operation.setRequestBody(requestBody);
                                    }

                                }
                            }
                        }
                    }
                    if (operationParameters.size() > 0) {
                        operation.setParameters(operationParameters);
                    }

                    paths.addPathItem(operationPath, pathItemObject);
                    if (openAPI.getPaths() != null) {
                        this.paths.putAll(openAPI.getPaths());
                    }

                    openAPI.setPaths(this.paths);
                }
            }
        }

        if (components.getSecuritySchemes() != null && components.getSecuritySchemes().size() > 0 ||
                components.getSchemas() != null && components.getSchemas().size() > 0) {
            openAPI.setComponents(components);
        }



        if (!openApiTags.isEmpty()) {
            Set<Tag> tagsSet  = new LinkedHashSet<>();
            tagsSet.addAll(openApiTags);
            if (openAPI.getTags() != null) {
                tagsSet.addAll(openAPI.getTags());
            }
            openAPI.setTags(new ArrayList<>(tagsSet));
        }

        OperationParser.getExternalDocumentation(apiExternalDocs).ifPresent(externalDocumentation -> openAPI.setExternalDocs(externalDocumentation));
        OperationParser.getInfo(apiInfo).ifPresent(info -> openAPI.setInfo(info));

        return openAPI;
    }

    private void setMediaTypeToContent(Schema schema, Content content, String value) {
        MediaType mediaTypeObject = new MediaType();
        mediaTypeObject.setSchema(schema);
        content.addMediaType(value, mediaTypeObject);
    }

    public Operation parseMethod(Method method) {
        JavaType classType = TypeFactory.defaultInstance().constructType(method.getDeclaringClass());
        return parseMethod(classType.getClass(), method);
    }

    private Operation parseMethod(Class<?> cls, Method method) {
        Operation operation = new Operation();
        io.swagger.oas.annotations.Operation apiOperation = ReflectionUtils.getAnnotation(method, io.swagger.oas.annotations.Operation.class);
        io.swagger.oas.annotations.callbacks.Callback apiCallback = ReflectionUtils.getAnnotation(method, io.swagger.oas.annotations.callbacks.Callback.class);
        io.swagger.oas.annotations.security.SecurityRequirement apiSecurity = ReflectionUtils.getAnnotation(method, io.swagger.oas.annotations.security.SecurityRequirement.class);

        if (apiOperation != null) {

            Map<String, Callback> callbacks = getCallbacks(apiCallback);

            if (callbacks.size() > 0) {
                operation.setCallbacks(callbacks);
            }
            SecurityParser.getSecurityRequirement(apiSecurity).ifPresent(operation::setSecurity);

            setOperationObjectFromApiOperationAnnotation(operation, apiOperation);
            if (StringUtils.isBlank(operation.getOperationId())) {
                operation.setOperationId(method.getName());
            }
        } else {
            return null;
        }
        return operation;
    }

    private Map<String, Callback> getCallbacks(io.swagger.oas.annotations.callbacks.Callback apiCallback) {
        Map<String, Callback> callbackMap = new HashMap<>();
        if (apiCallback == null) {
            return callbackMap;
        }
        Callback callbackObject = new Callback();
        PathItem pathItemObject = new PathItem();
        for (io.swagger.oas.annotations.Operation callbackOperation : apiCallback.operation()) {
            Operation callbackNewOperation = new Operation();
            setOperationObjectFromApiOperationAnnotation(callbackNewOperation, callbackOperation);
            setPathItemOperation(pathItemObject, callbackOperation.method(), callbackNewOperation);
        }

        callbackObject.addPathItem(apiCallback.callbackUrlExpression(), pathItemObject);
        callbackMap.put(apiCallback.name(), callbackObject);

        return callbackMap;
    }

    private void setPathItemOperation(PathItem pathItemObject, String method, Operation callbackNewOperation) {
        switch (method) {
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

    private void setOperationObjectFromApiOperationAnnotation(Operation operation, io.swagger.oas.annotations.Operation apiOperation) {
        if (StringUtils.isNotBlank(apiOperation.summary())) {
            operation.setSummary(apiOperation.summary());
        }
        if (StringUtils.isNotBlank(apiOperation.description())) {
            operation.setDescription(apiOperation.description());
        }
        if (StringUtils.isNotBlank(apiOperation.operationId())) {
            operation.setOperationId(getOperationId(apiOperation.operationId()));
        }
        if (apiOperation.deprecated()) {
            operation.setDeprecated(apiOperation.deprecated());
        }
        ReaderUtils.getStringListFromStringArray(apiOperation.tags()).ifPresent(operation::setTags);
        OperationParser.getTags(apiOperation.tags()).ifPresent(tag -> openApiTags.addAll(tag));
        OperationParser.getExternalDocumentation(apiOperation.externalDocs()).ifPresent(operation::setExternalDocs);
        OperationParser.getRequestBody(apiOperation.requestBody(), components).ifPresent(operation::setRequestBody);
        OperationParser.getApiResponses(apiOperation.responses(), classProduces, methodProduces, components).ifPresent(operation::setResponses);
        OperationParser.getServers(apiOperation.servers()).ifPresent(operation::setServers);
        OperationParser.getParametersList(apiOperation.parameters(), components).ifPresent(operation::setParameters);
    }

    protected String getOperationId(String operationId) {
        boolean operationIdUsed = existOperationId(operationId);
        String operationIdToFind = null;
        int counter = 0;
        while (operationIdUsed) {
            operationIdToFind = String.format("%s_%d", operationId, ++counter);
            operationIdUsed = existOperationId(operationIdToFind);
        }
        if (operationIdToFind != null) {
            operationId = operationIdToFind;
        }
        return operationId;
    }

    private boolean existOperationId(String operationId) {
        if (openAPI == null) {
            return false;
        }
        if (openAPI.getPaths() == null || openAPI.getPaths().isEmpty()) {
            return false;
        }
        for (PathItem path : openAPI.getPaths().values()) {
            String pathOperationId = extractOperationIdFromPathItem(path);
            if (operationId.equalsIgnoreCase(pathOperationId)) {
                return true;
            }

        }
        return false;
    }

    private List<Parameter> getParameters(Type type, List<Annotation> annotations) {
        final Iterator<OpenAPIExtension> chain = OpenAPIExtensions.chain();
        if (!chain.hasNext()) {
            return Collections.emptyList();
        }
        LOGGER.debug("getParameters for {}", type);
        Set<Type> typesToSkip = new HashSet<>();
        final OpenAPIExtension extension = chain.next();
        LOGGER.debug("trying extension {}", extension);

        final List<Parameter> parameters = extension.extractParameters(annotations, type, typesToSkip, chain);
        if (!parameters.isEmpty()) {
            final List<Parameter> processed = new ArrayList<>(parameters.size());
            for (Parameter parameter : parameters) {
                if (ParameterProcessor.applyAnnotations(openAPI, parameter, type, annotations) != null) {
                    processed.add(parameter);
                }
            }
            return processed;
        } else {
            LOGGER.debug("no parameter found, looking at body params");
            final List<Parameter> body = new ArrayList<>();
            if (!typesToSkip.contains(type)) {
                // TODO #2312 body - passing null means returned always NULL
                Parameter param = ParameterProcessor.applyAnnotations(openAPI, null, type, annotations);
                if (param != null) {
                    body.add(param);
                }
            }
            return body;
        }
    }

    private void mergeComponents(OpenAPI openAPI, boolean isComponentEmpty) {
        Components openAPIComponent = openAPI.getComponents();
        if (!isComponentEmpty) {
            if (openAPIComponent != null) {
                if (components.getCallbacks() != null) {
                    components.getCallbacks().putAll(openAPIComponent.getCallbacks());
                }
                if (components.getExamples() != null) {
                    components.getExamples().putAll(openAPIComponent.getExamples());
                }
                if (components.getExtensions() != null) {
                    components.getExtensions().putAll(openAPIComponent.getExtensions());
                }
                if (components.getHeaders() != null) {
                    components.getHeaders().putAll(openAPIComponent.getHeaders());
                }
                if (components.getLinks() != null) {
                    components.getLinks().putAll(openAPIComponent.getLinks());
                }
                if (components.getParameters() != null) {
                    components.getParameters().putAll(openAPIComponent.getParameters());
                }
                if (components.getRequestBodies() != null) {
                    components.getRequestBodies().putAll(openAPIComponent.getRequestBodies());
                }
                if (components.getResponses() != null) {
                    components.getResponses().putAll(openAPIComponent.getResponses());
                }
                if (components.getSchemas() != null) {
                    components.getSchemas().putAll(openAPIComponent.getSchemas());
                }

            }
            openAPI.setComponents(components);
        }
    }

    private String extractOperationIdFromPathItem(PathItem path) {
        if (path.getGet() != null) {
            return path.getGet().getOperationId();
        } else if (path.getPost() != null) {
            return path.getPost().getOperationId();
        } else if (path.getPut() != null) {
            return path.getPut().getOperationId();
        } else if (path.getDelete() != null) {
            return path.getDelete().getOperationId();
        } else if (path.getOptions() != null) {
            return path.getOptions().getOperationId();
        } else if (path.getHead() != null) {
            return path.getHead().getOperationId();
        } else if (path.getPatch() != null) {
            return path.getPatch().getOperationId();
        }
        return "";
    }
}
