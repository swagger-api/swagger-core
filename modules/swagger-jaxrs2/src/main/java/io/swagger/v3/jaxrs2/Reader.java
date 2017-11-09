package io.swagger.v3.jaxrs2;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.ParameterProcessor;
import io.swagger.v3.core.util.PathUtils;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtensions;
import io.swagger.v3.jaxrs2.util.ReaderUtils;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.integration.ContextUtils;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiReader;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Application;
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
    public static final String DEFAULT_DESCRIPTION = "default response";

    protected OpenAPIConfiguration config;

    private Application application;
    private OpenAPI openAPI;
    private Components components;
    private Paths paths;
    private Set<Tag> openApiTags;
    javax.ws.rs.Consumes classConsumes;

    javax.ws.rs.Produces classProduces;
    javax.ws.rs.Produces methodProduces;
    javax.ws.rs.Consumes methodConsumes;

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
        return read(cls, resolveApplicationPath());
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

        Map<Class<?>, ReaderListener> listeners = new HashMap<>();

        for (Class<?> cls : sortedClasses) {
            if (ReaderListener.class.isAssignableFrom(cls) && !listeners.containsKey(cls)) {
                try {
                    listeners.put(cls, (ReaderListener) cls.newInstance());
                } catch (Exception e) {
                    LOGGER.error("Failed to create ReaderListener", e);
                }
            }
        }

        for (ReaderListener listener : listeners.values()) {
            try {
                listener.beforeScan(this, openAPI);
            } catch (Exception e) {
                LOGGER.error("Unexpected error invoking beforeScan listener [" + listener.getClass().getName() + "]", e);
            }
        }

        for (Class<?> cls : sortedClasses) {
            read(cls, resolveApplicationPath());
        }

        for (ReaderListener listener : listeners.values()) {
            try {
                listener.afterScan(this, openAPI);
            } catch (Exception e) {
                LOGGER.error("Unexpected error invoking afterScan listener [" + listener.getClass().getName() + "]", e);
            }
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

    protected String resolveApplicationPath() {
        if (application != null) {
            ApplicationPath applicationPath = application.getClass().getAnnotation(ApplicationPath.class);
            if (applicationPath != null) {
                if (StringUtils.isNotBlank(applicationPath.value())) {
                    return applicationPath.value();
                }
            }
            // look for inner application, e.g. ResourceConfig
            try {
                Application innerApp = application;
                Method m = application.getClass().getMethod("getApplication", null);
                while (m != null) {
                    Application retrievedApp  = (Application) m.invoke(innerApp, null);
                    if (retrievedApp == null) {
                        break;
                    }
                    if (retrievedApp.getClass().equals(innerApp.getClass())) {
                        break;
                    }
                    innerApp = retrievedApp;
                    applicationPath = innerApp.getClass().getAnnotation(ApplicationPath.class);
                    if (applicationPath != null) {
                        if (StringUtils.isNotBlank(applicationPath.value())) {
                            return applicationPath.value();
                        }
                    }
                    m = innerApp.getClass().getMethod("getApplication", null);
                }
            } catch (NoSuchMethodException e) {
                // no inner application found
            } catch (Exception e) {
                // no inner application found
            }
        }
        return "";
    }

    public OpenAPI read(Class<?> cls, String parentPath) {

        List<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme = ReflectionUtils.getRepeatableAnnotations(cls, io.swagger.v3.oas.annotations.security.SecurityScheme.class);
        ExternalDocumentation apiExternalDocs = ReflectionUtils.getAnnotation(cls, ExternalDocumentation.class);
        // TODO process full @OpenAPIDefinition
        OpenAPIDefinition openAPIDefinition = ReflectionUtils.getAnnotation(cls, OpenAPIDefinition.class);
        Info apiInfo = null;
        if (openAPIDefinition != null) {
            apiInfo = openAPIDefinition.info();
        }
        classConsumes = ReflectionUtils.getAnnotation(cls, javax.ws.rs.Consumes.class);
        classProduces = ReflectionUtils.getAnnotation(cls, javax.ws.rs.Produces.class);

        if (apiSecurityScheme != null) {
            for (io.swagger.v3.oas.annotations.security.SecurityScheme securitySchemeAnnotation: apiSecurityScheme) {
                Optional<SecurityScheme> securityScheme = SecurityParser.getSecurityScheme(securitySchemeAnnotation);
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
            }
        }

        final javax.ws.rs.Path apiPath = ReflectionUtils.getAnnotation(cls, javax.ws.rs.Path.class);

        JavaType classType = TypeFactory.defaultInstance().constructType(cls);
        BeanDescription bd = Json.mapper().getSerializationConfig().introspect(classType);

        final List<Parameter> globalParameters = new ArrayList<>();

        // look for constructor-level annotated properties
        globalParameters.addAll(ReaderUtils.collectConstructorParameters(cls, components, classConsumes));

        // look for field-level annotated properties
        globalParameters.addAll(ReaderUtils.collectFieldParameters(cls, components, classConsumes));

        Method methods[] = cls.getMethods();
        for (Method method : methods) {
            if (isOperationHidden(method)) {
                continue;
            }
            AnnotatedMethod annotatedMethod = bd.findMethod(method.getName(), method.getParameterTypes());
            methodProduces = ReflectionUtils.getAnnotation(method, javax.ws.rs.Produces.class);
            methodConsumes = ReflectionUtils.getAnnotation(method, javax.ws.rs.Consumes.class);

            if (ReflectionUtils.isOverriddenMethod(method, cls)) {
                continue;
            }
            javax.ws.rs.Path methodPath = ReflectionUtils.getAnnotation(method, javax.ws.rs.Path.class);

            String operationPath = ReaderUtils.getPath(apiPath, methodPath, parentPath);

            // skip if path is the same as parent, e.g. for @ApplicationPath annotated application
            // extending resource config.
            if (ignoreOperationPath(operationPath, parentPath)) {
                continue;
            }

            Map<String, String> regexMap = new LinkedHashMap<>();
            operationPath = PathUtils.parsePath(operationPath, regexMap);
            if (operationPath != null) {
                if (ReaderUtils.isIgnored(operationPath, config)) {
                    continue;
                }

                Operation operation = parseMethod(method, globalParameters);
                if (operation != null) {
                    PathItem pathItemObject;
                    if (openAPI.getPaths() != null && openAPI.getPaths().get(operationPath) != null) {
                        pathItemObject = openAPI.getPaths().get(operationPath);
                    } else {
                        pathItemObject = new PathItem();
                    }

                    String httpMethod = ReaderUtils.extractOperationMethod(operation, method, OpenAPIExtensions.chain());
                    if (StringUtils.isBlank(httpMethod)) {
                        continue;
                    }
                    setPathItemOperation(pathItemObject, httpMethod, operation);

                    List<Parameter> operationParameters = new ArrayList<>();
                    Annotation[][] paramAnnotations = ReflectionUtils.getParameterAnnotations(method);
                    if (annotatedMethod == null) { // annotatedMethod not null only when method with 0-2 parameters
                        Type[] genericParameterTypes = method.getGenericParameterTypes();
                        for (int i = 0; i < genericParameterTypes.length; i++) {
                            final Type type = TypeFactory.defaultInstance().constructType(genericParameterTypes[i], cls);
                            ResolvedParameter resolvedParameter = getParameters(type, Arrays.asList(paramAnnotations[i]), operation, classConsumes, methodConsumes);
                            for (Parameter p: resolvedParameter.parameters) {
                                operationParameters.add(p);
                            }
                            if (resolvedParameter.requestBody != null)  {
                                processRequestBody(resolvedParameter.requestBody, operation, operationParameters, paramAnnotations[i], type);
                            }
                        }
                    } else {
                        for (int i = 0; i < annotatedMethod.getParameterCount(); i++) {
                            AnnotatedParameter param = annotatedMethod.getParameter(i);
                            final Type type = TypeFactory.defaultInstance().constructType(param.getParameterType(), cls);
                            ResolvedParameter resolvedParameter = getParameters(type, Arrays.asList(paramAnnotations[i]), operation, classConsumes, methodConsumes);
                            for (Parameter p: resolvedParameter.parameters) {
                                operationParameters.add(p);
                            }
                            if (resolvedParameter.requestBody != null)  {
                                processRequestBody(resolvedParameter.requestBody, operation, operationParameters, paramAnnotations[i], type);
                            }
                        }
                    }

                    if (operationParameters.size() > 0) {
                        for (Parameter operationParameter : operationParameters) {
                            operation.addParametersItem(operationParameter);
                        }
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
            Set<Tag> tagsSet = new LinkedHashSet<>();
            tagsSet.addAll(openApiTags);
            if (openAPI.getTags() != null) {
                tagsSet.addAll(openAPI.getTags());
            }
            openAPI.setTags(new ArrayList<>(tagsSet));
        }

        AnnotationsUtils.getExternalDocumentation(apiExternalDocs).ifPresent(externalDocumentation -> openAPI.setExternalDocs(externalDocumentation));
        AnnotationsUtils.getInfo(apiInfo).ifPresent(info -> openAPI.setInfo(info));

        return openAPI;
    }

    protected Content processContent(Content content, Schema schema) {
        if (content == null) {
            content = new Content();
        }
        if (methodConsumes != null) {
            for (String value : methodConsumes.value()) {
                setMediaTypeToContent(schema, content, value);
            }
        } else if (classConsumes != null) {
            for (String value : classConsumes.value()) {
                setMediaTypeToContent(schema, content, value);
            }
        } else {
            setMediaTypeToContent(schema, content, DEFAULT_MEDIA_TYPE_VALUE);
        }
        return content;
    }

    protected void processRequestBody(Parameter requestBodyParameter, Operation operation,
                                    List<Parameter> operationParameters,
                                    Annotation[] paramAnnotations, Type type) {
                if (operation.getRequestBody() == null) {
                    io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyAnnotation = getRequestBody(Arrays.asList(paramAnnotations));
                    if (requestBodyAnnotation != null) {
                        Optional<RequestBody> optionalRequestBody = OperationParser.getRequestBody(requestBodyAnnotation, classConsumes, methodConsumes, components);
                        if (optionalRequestBody.isPresent()) {
                            RequestBody requestBody = optionalRequestBody.get();
                            if (StringUtils.isBlank(requestBody.get$ref()) &&
                                    (requestBody.getContent() == null || requestBody.getContent().isEmpty())) {
                                if (requestBodyParameter.getSchema() != null) {
                                    Content content = processContent(requestBody.getContent(), requestBodyParameter.getSchema());
                                    requestBody.setContent(content);
                                }
                            } else if (StringUtils.isBlank(requestBody.get$ref()) &&
                                    requestBody.getContent() != null &&
                                    !requestBody.getContent().isEmpty()) {
                                if (requestBodyParameter.getSchema() != null) {
                                    for (MediaType mediaType: requestBody.getContent().values()) {
                                        if (StringUtils.isBlank(mediaType.getSchema().getType())) {
                                            mediaType.getSchema().setType(requestBodyParameter.getSchema().getType());
                                        }
                                    }
                                }
                            }

                            operation.setRequestBody(requestBody);
                        }
                    } else {
                        boolean isRequestBodyEmpty = true;
                        RequestBody requestBody = new RequestBody();
                        if (StringUtils.isNotBlank(requestBodyParameter.get$ref())) {
                            requestBody.set$ref(requestBodyParameter.get$ref());
                            isRequestBodyEmpty = false;
                        }
                        if (StringUtils.isNotBlank(requestBodyParameter.getDescription())) {
                            requestBody.setDescription(requestBodyParameter.getDescription());
                            isRequestBodyEmpty = false;
                        }
                        if (Boolean.TRUE.equals(requestBodyParameter.getRequired())) {
                            requestBody.setRequired(requestBodyParameter.getRequired());
                            isRequestBodyEmpty = false;
                        }

                        if (requestBodyParameter.getSchema() != null) {
                            Content content = processContent(null, requestBodyParameter.getSchema());
                            requestBody.setContent(content);
                            isRequestBodyEmpty = false;
                        }
                        if (!isRequestBodyEmpty) {
                            operation.setRequestBody(requestBody);
                        }
                    }
                }
    }

    private io.swagger.v3.oas.annotations.parameters.RequestBody getRequestBody(List<Annotation> annotations) {
        if (annotations == null) {
            return null;
        }
        for (Annotation a : annotations) {
            if (a instanceof io.swagger.v3.oas.annotations.parameters.RequestBody) {
                return (io.swagger.v3.oas.annotations.parameters.RequestBody) a;
            }
        }
        return null;
    }

    private void setMediaTypeToContent(Schema schema, Content content, String value) {
        MediaType mediaTypeObject = new MediaType();
        mediaTypeObject.setSchema(schema);
        content.addMediaType(value, mediaTypeObject);
    }

    public Operation parseMethod(Method method, List<Parameter> globalParameters) {
        JavaType classType = TypeFactory.defaultInstance().constructType(method.getDeclaringClass());
        return parseMethod(classType.getClass(), method, globalParameters);
    }

    private Operation parseMethod(Class<?> cls, Method method, List<Parameter> globalParameters) {
        Operation operation = new Operation();

        io.swagger.v3.oas.annotations.Operation apiOperation = ReflectionUtils.getAnnotation(method, io.swagger.v3.oas.annotations.Operation.class);

        List<io.swagger.v3.oas.annotations.security.SecurityRequirement> apiSecurity = ReflectionUtils.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.security.SecurityRequirement.class);
        List<io.swagger.v3.oas.annotations.callbacks.Callback> apiCallbacks = ReflectionUtils.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.callbacks.Callback.class);
        List<Server> apiServers = ReflectionUtils.getRepeatableAnnotations(method, Server.class);
        List<io.swagger.v3.oas.annotations.tags.Tag> apiTags = ReflectionUtils.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.tags.Tag.class);
        List<io.swagger.v3.oas.annotations.Parameter> apiParameters = ReflectionUtils.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.Parameter.class);
        List<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponses = ReflectionUtils.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.responses.ApiResponse.class);
        // TODO extensions and external docs
        List<Extension> apiExtensions = ReflectionUtils.getRepeatableAnnotations(method, Extension.class);
        ExternalDocumentation apiExternalDocumentation = ReflectionUtils.getAnnotation(method, ExternalDocumentation .class);

        // callbacks
        Map<String, Callback> callbacks = new LinkedHashMap<>();

        if (apiCallbacks != null) {
            for (io.swagger.v3.oas.annotations.callbacks.Callback methodCallback : apiCallbacks) {
                Map<String, Callback> currentCallbacks = getCallbacks(methodCallback);
                callbacks.putAll(currentCallbacks);
            }
        }
        if (callbacks.size() > 0) {
            operation.setCallbacks(callbacks);
        }

        // security
        if (apiSecurity != null) {
            SecurityParser.getSecurityRequirements(apiSecurity.toArray(new io.swagger.v3.oas.annotations.security.SecurityRequirement[apiSecurity.size()])).ifPresent(operation::setSecurity);
        }

        // servers
        if (apiServers != null) {
            AnnotationsUtils.getServers(apiServers.toArray(new Server[apiServers.size()])).ifPresent(servers -> servers.forEach(operation::addServersItem));
        }

        // tags
        if (apiTags != null) {
            apiTags.stream()
                .filter(t -> operation.getTags() == null || (operation.getTags() != null && !operation.getTags().contains(t.name())))
                .map(t -> t.name())
                .forEach(operation::addTagsItem);
            AnnotationsUtils.getTags(apiTags.toArray(new io.swagger.v3.oas.annotations.tags.Tag[apiTags.size()])).ifPresent(tags -> openApiTags.addAll(tags));
        }

        // parameters
        if (globalParameters != null) {
            for (Parameter globalParameter : globalParameters) {
                operation.addParametersItem(globalParameter);
            }
        }
        if (apiParameters != null) {
            getParametersListFromAnnotation(
            //OperationParser.getParametersList(
                    apiParameters.toArray(new io.swagger.v3.oas.annotations.Parameter[apiParameters.size()]),
                    classConsumes,
                    methodConsumes,
                    operation).ifPresent(p -> p.forEach(operation::addParametersItem));
        }

        // apiResponses
        if (apiResponses != null) {
            OperationParser.getApiResponses(apiResponses.toArray(new io.swagger.v3.oas.annotations.responses.ApiResponse[apiResponses.size()]), classProduces, methodProduces, components).ifPresent(responses -> {
                if (operation.getResponses() == null) {
                    operation.setResponses(responses);
                } else {
                    responses.forEach(operation.getResponses()::addApiResponse);
                }
            });
        }

        // operation id
        if (StringUtils.isBlank(operation.getOperationId())) {
            operation.setOperationId(method.getName());
        }

        if (apiOperation != null) {
            setOperationObjectFromApiOperationAnnotation(operation, apiOperation);
        }

        // handle return type, add as response in case.
        Type returnType = method.getGenericReturnType();
        if (!shouldIgnoreClass(returnType.getTypeName())) {
            ResolvedSchema resolvedSchema = ModelConverters.getInstance().resolveAnnotatedType(returnType, new ArrayList<>(), "");
            if (resolvedSchema.schema != null) {
                Schema returnTypeSchema = resolvedSchema.schema;
                Content content = new Content();
                MediaType mediaType = new MediaType().schema(returnTypeSchema);
                AnnotationsUtils.applyTypes(classConsumes == null ? new String[0] : classConsumes.value(),
                        methodConsumes == null ? new String[0] : methodConsumes.value(), content, mediaType);
                if (operation.getResponses() == null) {
                    operation.responses(
                            new ApiResponses()._default(
                                    new ApiResponse().description(DEFAULT_DESCRIPTION)
                                            .content(content)
                            )
                    );
                }
                if (operation.getResponses().getDefault() != null &&
                        StringUtils.isBlank(operation.getResponses().getDefault().get$ref()) &&
                        operation.getResponses().getDefault().getContent() == null) {
                    operation.getResponses().getDefault().content(content);
                }
                Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
                if (schemaMap != null) {
                    schemaMap.forEach((key, schema) -> components.addSchemas(key, schema));
                }

            }
        }
        if (operation.getResponses() == null || operation.getResponses().size() == 0) {
            ApiResponse apiResponseObject = new ApiResponse();
            apiResponseObject.setDescription(DEFAULT_DESCRIPTION);
            operation.setResponses(new ApiResponses()._default(apiResponseObject));

        }
        return operation;
    }

    private boolean shouldIgnoreClass(String className) {
        if (StringUtils.isBlank(className)) {
            return true;
        }
        boolean ignore = false;
        ignore = ignore || className.startsWith("javax.ws.rs.");
        ignore = ignore || className.equalsIgnoreCase("void");
        return ignore;
    }

    private Map<String, Callback> getCallbacks(io.swagger.v3.oas.annotations.callbacks.Callback apiCallback) {
        Map<String, Callback> callbackMap = new HashMap<>();
        if (apiCallback == null) {
            return callbackMap;
        }
        Callback callbackObject = new Callback();
        PathItem pathItemObject = new PathItem();
        for (io.swagger.v3.oas.annotations.Operation callbackOperation : apiCallback.operation()) {
            Operation callbackNewOperation = new Operation();
            setOperationObjectFromApiOperationAnnotation(callbackNewOperation, callbackOperation);
            setPathItemOperation(pathItemObject, callbackOperation.method(), callbackNewOperation);
        }

        callbackObject.addPathItem(apiCallback.callbackUrlExpression(), pathItemObject);
        callbackMap.put(apiCallback.name(), callbackObject);

        return callbackMap;
    }

    private void setPathItemOperation(PathItem pathItemObject, String method, Operation operation) {
        switch (method) {
            case POST_METHOD:
                pathItemObject.post(operation);
                break;
            case GET_METHOD:
                pathItemObject.get(operation);
                break;
            case DELETE_METHOD:
                pathItemObject.delete(operation);
                break;
            case PUT_METHOD:
                pathItemObject.put(operation);
                break;
            case PATCH_METHOD:
                pathItemObject.patch(operation);
                break;
            case TRACE_METHOD:
                pathItemObject.trace(operation);
                break;
            case HEAD_METHOD:
                pathItemObject.head(operation);
                break;
            case OPTIONS_METHOD:
                pathItemObject.options(operation);
                break;
            default:
                // Do nothing here
                break;
        }
    }

    private void setOperationObjectFromApiOperationAnnotation(Operation operation, io.swagger.v3.oas.annotations.Operation apiOperation) {
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

        ReaderUtils.getStringListFromStringArray(apiOperation.tags()).ifPresent(tags -> {
            tags.stream()
                    .filter(t -> operation.getTags() == null || (operation.getTags() != null && !operation.getTags().contains(t)))
                    .forEach(operation::addTagsItem);
        });
        AnnotationsUtils.getTags(apiOperation.tags()).ifPresent(tag -> openApiTags.addAll(tag));

        if (operation.getExternalDocs() == null) { // if not set in root annotation
            AnnotationsUtils.getExternalDocumentation(apiOperation.externalDocs()).ifPresent(operation::setExternalDocs);
        }

        OperationParser.getApiResponses(apiOperation.responses(), classProduces, methodProduces, components).ifPresent(responses -> {
            if (operation.getResponses() == null) {
                operation.setResponses(responses);
            } else {
                responses.forEach(operation.getResponses()::addApiResponse);
            }
        });
        AnnotationsUtils.getServers(apiOperation.servers()).ifPresent(servers -> servers.forEach(operation::addServersItem));

        getParametersListFromAnnotation(
                apiOperation.parameters(),
                classConsumes,
                methodConsumes,
                operation).ifPresent(p -> p.forEach(operation::addParametersItem));

        // security
        List<SecurityRequirement> securityRequirements = operation.getSecurity();

        if (securityRequirements != null && securityRequirements.size() > 0) {
            Optional<List<SecurityRequirement>> requirementsObject = SecurityParser.getSecurityRequirements(apiOperation.security());
            if (requirementsObject.isPresent()) {
                List<SecurityRequirement> requirements = requirementsObject.get();
                for (SecurityRequirement secReq : requirements) {
                    if (!securityRequirements.contains(secReq)) {
                        securityRequirements.add(secReq);
                    }
                }
                operation.setSecurity(securityRequirements);
            }
        } else {
            SecurityParser.getSecurityRequirements(apiOperation.security()).ifPresent(operation::setSecurity);
        }
    
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

    protected Optional<List<Parameter>> getParametersListFromAnnotation(io.swagger.v3.oas.annotations.Parameter[] parameters, Consumes classConsumes, Consumes methodConsumes, Operation operation) {
        if (parameters == null) {
            return Optional.empty();
        }
        List<Parameter> parametersObject = new ArrayList<>();
        for (io.swagger.v3.oas.annotations.Parameter parameter : parameters) {

            ResolvedParameter resolvedParameter = getParameters(ParameterProcessor.getParameterType(parameter), Collections.singletonList(parameter), operation, classConsumes, methodConsumes);
            parametersObject.addAll(resolvedParameter.parameters);
        }
        if (parametersObject.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(parametersObject);
    }


    protected ResolvedParameter getParameters(Type type, List<Annotation> annotations, Operation operation, javax.ws.rs.Consumes classConsumes,
                                              javax.ws.rs.Consumes methodConsumes) {
        final Iterator<OpenAPIExtension> chain = OpenAPIExtensions.chain();
        if (!chain.hasNext()) {
            return new ResolvedParameter();
        }
        LOGGER.debug("getParameters for {}", type);
        Set<Type> typesToSkip = new HashSet<>();
        final OpenAPIExtension extension = chain.next();
        LOGGER.debug("trying extension {}", extension);

        final ResolvedParameter extractParametersResult = extension.extractParameters(annotations, type, typesToSkip, components, classConsumes, methodConsumes, true, chain);
        return extractParametersResult;
    }

    private boolean hasAnnotation(List<Class<? extends Annotation>> aClasses, List<Annotation> list) {

        if (list == null) {
            return false;
        }
        for (Annotation a: list) {
            if (aClasses.contains(a.annotationType())) {
                return true;
            }
        }
        return false;
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

    protected boolean isOperationHidden(Method method) {
        io.swagger.v3.oas.annotations.Operation apiOperation = ReflectionUtils.getAnnotation(method, io.swagger.v3.oas.annotations.Operation.class);
        if (apiOperation != null && apiOperation.hidden()) {
            return true;
        }
        if (!Boolean.TRUE.equals(config.isReadAllResources()) && apiOperation == null) {
            return true;
        }
        return false;
    }

    public void setApplication (Application application) {
        this.application = application;
    }

    protected boolean ignoreOperationPath(String path, String parentPath) {

        if (StringUtils.isBlank(path) && StringUtils.isBlank(parentPath)) {
            return true;
        } else if (StringUtils.isNotBlank(path) && StringUtils.isBlank(parentPath)) {
            return false;
        } else if (StringUtils.isBlank(path) && StringUtils.isNotBlank(parentPath)) {
            return false;
        }
        if (parentPath != null && !"".equals(parentPath) && !"/".equals(parentPath)) {
            if (!parentPath.startsWith("/")) {
                parentPath = "/" + parentPath;
            }
            if (parentPath.endsWith("/")) {
                parentPath = parentPath.substring(0, parentPath.length() - 1);
            }
        }
        if (path != null && !"".equals(path) && !"/".equals(path)) {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
        }
        if (path.equals(parentPath)) {
            return true;
        }
        return false;
    }
}
