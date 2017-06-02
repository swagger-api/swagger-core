package io.swagger.jaxrs2;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.jaxrs2.config.DefaultReaderConfig;
import io.swagger.jaxrs2.config.ReaderConfig;
import io.swagger.jaxrs2.ext.OpenAPIExtensions;
import io.swagger.jaxrs2.util.ReaderUtils;
import io.swagger.oas.models.*;
import io.swagger.oas.models.callbacks.Callback;
import io.swagger.oas.models.callbacks.Callbacks;
import io.swagger.oas.models.security.SecurityScheme;
import io.swagger.oas.models.tags.Tag;
import io.swagger.util.PathUtils;
import io.swagger.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

public class Reader {
    private final ReaderConfig config;

    private OpenAPI openAPI;
    private Paths paths;
    private Set<Tag> openApiTags;

    private static final String GET_METHOD = "get";
    private static final String POST_METHOD = "post";
    private static final String PUT_METHOD = "put";
    private static final String DELETE_METHOD = "delete";
    private static final String PATCH_METHOD = "patch";
    private static final String TRACE_METHOD = "trace";
    private static final String HEAD_METHOD = "head";
    private static final String OPTIONS_METHOD = "options";

    public Reader(OpenAPI openAPI, ReaderConfig config) {
        this.openAPI = openAPI;
        paths = new Paths();
        openApiTags = new LinkedHashSet<>();
        this.config = new DefaultReaderConfig();
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
     * be instantiated before any of the classes are scanned for Swagger annotations - so they can be invoked
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

    public OpenAPI read(Class<?> cls, String parentPath) {
        io.swagger.oas.annotations.security.SecurityScheme apiSecurityScheme = ReflectionUtils.getAnnotation(cls, io.swagger.oas.annotations.security.SecurityScheme.class);
        io.swagger.oas.annotations.ExternalDocumentation apiExternalDocs = ReflectionUtils.getAnnotation(cls, io.swagger.oas.annotations.ExternalDocumentation.class);
        io.swagger.oas.annotations.info.Info apiInfo = ReflectionUtils.getAnnotation(cls, io.swagger.oas.annotations.info.Info.class);

        Optional<SecurityScheme> securityScheme = SecurityParser.getSecurityScheme(apiSecurityScheme);
        Components components = new Components();
        if (securityScheme.isPresent()) {
            Map<String, SecurityScheme> securitySchemeMap = new HashMap<>();
            securitySchemeMap.put(securityScheme.get().getName(), securityScheme.get());
            components.setSecuritySchemes(securitySchemeMap);
        }
        openAPI.setComponents(components);
        final javax.ws.rs.Path apiPath = ReflectionUtils.getAnnotation(cls, javax.ws.rs.Path.class);

        Method methods[] = cls.getMethods();
        for (Method method : methods) {
            if (ReflectionUtils.isOverriddenMethod(method, cls)) {
                continue;
            }
            javax.ws.rs.Path methodPath = ReflectionUtils.getAnnotation(method, javax.ws.rs.Path.class);

            String operationPath = ReaderUtils.getPath(apiPath, methodPath, parentPath);

            Map<String, String> regexMap = new LinkedHashMap<>();
            operationPath = PathUtils.parsePath(operationPath, regexMap);
            if (operationPath != null) {
                if (ReaderUtils.isIgnored(operationPath, config)) {
                    continue;
                }

                Operation operation = parseMethod(method);
                PathItem pathItemObject = new PathItem();
                pathItemObject.set$ref(operation.getOperationId());
                pathItemObject.setSummary(operation.getSummary());
                pathItemObject.setDescription(operation.getDescription());
                String httpMethod = ReaderUtils.extractOperationMethod(operation, method, OpenAPIExtensions.chain());
                setPathItemOperation(pathItemObject, httpMethod, operation);

                paths.addPathItem(pathItemObject.get$ref(), pathItemObject);
            }
        }

        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.addAll(openApiTags);
        openAPI.setTags(tagList);

        OperationParser.getExternalDocumentation(apiExternalDocs).ifPresent(externalDocumentation -> openAPI.setExternalDocs(externalDocumentation));
        OperationParser.getInfo(apiInfo).ifPresent(info -> openAPI.setInfo(info));

        openAPI.setPaths(paths);

        return openAPI;
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
        io.swagger.oas.annotations.links.Link apiLinks = ReflectionUtils.getAnnotation(method, io.swagger.oas.annotations.links.Link.class);

        if (apiOperation != null) {
            getCallbacks(apiCallback).ifPresent(callbacks -> operation.setCallbacks(callbacks));
            SecurityParser.getSecurityRequirement(apiSecurity).ifPresent(securityRequirements -> operation.setSecurity(securityRequirements));
            OperationParser.getApiResponses(apiOperation.responses(), apiLinks).ifPresent(apiResponses -> operation.setResponses(apiResponses));
            setOperationObjectFromApiOperationAnnotation(operation, apiOperation);
        }
        return operation;
    }

    private Optional<Callbacks> getCallbacks(io.swagger.oas.annotations.callbacks.Callback apiCallback) {
        if (apiCallback == null) {
            return Optional.empty();
        }
        Callbacks callbacksObject = new Callbacks();
        Callback callbackObject = new Callback();
        PathItem pathItemObject = new PathItem();

        for (io.swagger.oas.annotations.Operation callbackOperation : apiCallback.operation()) {
            Operation callbackNewOperation = new Operation();

            setOperationObjectFromApiOperationAnnotation(callbackNewOperation, callbackOperation);

            setPathItemOperation(pathItemObject, callbackOperation.method(), callbackNewOperation);
        }
        pathItemObject.setDescription(apiCallback.name());
        pathItemObject.setSummary(apiCallback.name());

        callbackObject.addPathItem(apiCallback.name(), pathItemObject);
        callbacksObject.addCallback(apiCallback.name(), callbackObject);

        return Optional.of(callbacksObject);
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
        ReaderUtils.getStringListFromStringArray(apiOperation.tags()).ifPresent(tags -> operation.setTags(tags));
        OperationParser.getTags(apiOperation.tags()).ifPresent(tag -> openApiTags.addAll(tag));
        operation.setSummary(apiOperation.summary());
        operation.setDescription(apiOperation.description());
        OperationParser.getExternalDocumentation(apiOperation.externalDocs()).ifPresent(externalDocumentation -> operation.setExternalDocs(externalDocumentation));
        operation.setOperationId(getOperationId(apiOperation.operationId()));
        OperationParser.getParametersList(apiOperation.parameters()).ifPresent(parameters -> operation.setParameters(parameters));
        OperationParser.getRequestBody(apiOperation.requestBody()).ifPresent(requestBody -> operation.setRequestBody(requestBody));
        operation.setDeprecated(apiOperation.deprecated());
        OperationParser.getServers(apiOperation.servers()).ifPresent(servers -> operation.setServers(servers));
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