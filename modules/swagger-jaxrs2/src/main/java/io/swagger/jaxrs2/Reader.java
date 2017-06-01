package io.swagger.jaxrs2;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.jaxrs2.config.DefaultReaderConfig;
import io.swagger.jaxrs2.config.ReaderConfig;
import io.swagger.jaxrs2.ext.OpenAPIExtension;
import io.swagger.jaxrs2.ext.OpenAPIExtensions;
import io.swagger.jaxrs2.util.ReaderUtils;
import io.swagger.oas.annotations.*;
import io.swagger.oas.models.*;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.callbacks.Callback;
import io.swagger.oas.models.callbacks.Callbacks;
import io.swagger.oas.models.info.Contact;
import io.swagger.oas.models.info.Info;
import io.swagger.oas.models.info.License;
import io.swagger.oas.models.security.SecurityScheme;
import io.swagger.oas.models.servers.Server;
import io.swagger.oas.models.tags.Tag;
import io.swagger.util.PathUtils;
import io.swagger.util.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.DELETE;
import javax.ws.rs.HttpMethod;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class Reader {
    private static final Logger LOGGER = LoggerFactory.getLogger(Reader.class);
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
    private static final String PATH_DELIMITER = "/";


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

        return openAPI;
    }

    public OpenAPI read(Class<?> cls) {
        io.swagger.oas.annotations.security.SecurityScheme apiSecurityScheme = ReflectionUtils.getAnnotation(cls, io.swagger.oas.annotations.security.SecurityScheme.class);
        io.swagger.oas.annotations.servers.Server server = ReflectionUtils.getAnnotation(cls, io.swagger.oas.annotations.servers.Server.class);
        io.swagger.oas.annotations.ExternalDocumentation apiExternalDocs = ReflectionUtils.getAnnotation(cls, io.swagger.oas.annotations.ExternalDocumentation.class);
        io.swagger.oas.annotations.info.Info apiInfo = ReflectionUtils.getAnnotation(cls, io.swagger.oas.annotations.info.Info.class);

        String parentPath = "";

        Optional<SecurityScheme> securityScheme = SecurityParser.getSecurityScheme(apiSecurityScheme);
        Components components = new Components();
        if (securityScheme.isPresent()) {
            Map<String, SecurityScheme> securitySchemeMap = new HashMap();
            securitySchemeMap.put(securityScheme.get().getName(), securityScheme.get());
            components.setSecuritySchemes(securitySchemeMap);
        }
        openAPI.setComponents(components);
        final javax.ws.rs.Path apiPath = ReflectionUtils.getAnnotation(cls, javax.ws.rs.Path.class);


        Optional<Server> serverOptional = OperationParser.getServer(server);
        JavaType classType = TypeFactory.defaultInstance().constructType(cls);
        BasicBeanDescription bd = new ObjectMapper().getSerializationConfig().introspect(classType);

        Method methods[] = cls.getMethods();
        for (Method method : methods) {
            AnnotatedMethod annotatedMethod = bd.findMethod(method.getName(), method.getParameterTypes());
            if (ReflectionUtils.isOverriddenMethod(method, cls)) {
                continue;
            }
            javax.ws.rs.Path methodPath = ReflectionUtils.getAnnotation(method, javax.ws.rs.Path.class);

            String operationPath = getPath(apiPath, methodPath, parentPath);

            Map<String, String> regexMap = new LinkedHashMap<>();
            operationPath = PathUtils.parsePath(operationPath, regexMap);
            if (operationPath != null) {
                if (isIgnored(operationPath)) {
                    continue;
                }

                Operation operation = parseMethod(method);
                PathItem pathItemObject = new PathItem();
                pathItemObject.set$ref(operation.getOperationId());
                pathItemObject.setSummary(operation.getSummary());
                pathItemObject.setDescription(operation.getDescription());
                switch (extractOperationMethod(operation, method, OpenAPIExtensions.chain())) {
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

                paths.addPathItem(pathItemObject.get$ref(), pathItemObject);
            }
        }

        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.addAll(openApiTags);
        openAPI.setTags(tagList);

        OperationParser.getExternalDocumentation(apiExternalDocs).ifPresent(externalDocumentation -> openAPI.setExternalDocs(externalDocumentation));
        getInfo(apiInfo).ifPresent(info -> openAPI.setInfo(info));

        openAPI.setPaths(paths);

        return openAPI;
    }

    private boolean isIgnored(String path) {
        for (String item : config.getIgnoredRoutes()) {
            final int length = item.length();
            if (path.startsWith(item) && (path.length() == length || path.startsWith(PATH_DELIMITER, length))) {
                return true;
            }
        }
        return false;
    }

    String getPath(javax.ws.rs.Path classLevelPath, javax.ws.rs.Path methodLevelPath, String parentPath) {
        if (classLevelPath == null && methodLevelPath == null && StringUtils.isEmpty(parentPath)) {
            return null;
        }
        StringBuilder b = new StringBuilder();
        if (parentPath != null && !"".equals(parentPath) && !"/".equals(parentPath)) {
            if (!parentPath.startsWith("/")) {
                parentPath = "/" + parentPath;
            }
            if (parentPath.endsWith("/")) {
                parentPath = parentPath.substring(0, parentPath.length() - 1);
            }

            b.append(parentPath);
        }
        if (classLevelPath != null) {
            b.append(classLevelPath.value());
        }
        if (methodLevelPath != null && !"/".equals(methodLevelPath.value())) {
            String methodPath = methodLevelPath.value();
            if (!methodPath.startsWith("/") && !b.toString().endsWith("/")) {
                b.append("/");
            }
            if (methodPath.endsWith("/")) {
                methodPath = methodPath.substring(0, methodPath.length() - 1);
            }
            b.append(methodPath);
        }
        String output = b.toString();
        if (!output.startsWith("/")) {
            output = "/" + output;
        }
        if (output.endsWith("/") && output.length() > 1) {
            return output.substring(0, output.length() - 1);
        } else {
            return output;
        }
    }

    public String extractOperationMethod(Operation operation, Method method, Iterator<OpenAPIExtension> chain) {
        if (method.getAnnotation(javax.ws.rs.GET.class) != null) {
            return "get";
        } else if (method.getAnnotation(javax.ws.rs.PUT.class) != null) {
            return "put";
        } else if (method.getAnnotation(javax.ws.rs.POST.class) != null) {
            return "post";
        } else if (method.getAnnotation(javax.ws.rs.DELETE.class) != null) {
            return "delete";
        } else if (method.getAnnotation(javax.ws.rs.OPTIONS.class) != null) {
            return "options";
        } else if (method.getAnnotation(javax.ws.rs.HEAD.class) != null) {
            return "head";
        } else if (method.getAnnotation(DELETE.class) != null) {
            return "delete";
        } else if (method.getAnnotation(HttpMethod.class) != null) {
            HttpMethod httpMethod = method.getAnnotation(HttpMethod.class);
            return httpMethod.value().toLowerCase();
        } else if (!StringUtils.isEmpty(getHttpMethodFromCustomAnnotations(method))) {
            return getHttpMethodFromCustomAnnotations(method);
        } else if ((ReflectionUtils.getOverriddenMethod(method)) != null) {
            return extractOperationMethod(operation, ReflectionUtils.getOverriddenMethod(method), chain);
        } else if (chain != null && chain.hasNext()) {
            return chain.next().extractOperationMethod(operation, method, chain);
        } else {
            return null;
        }
    }

    private String getHttpMethodFromCustomAnnotations(Method method) {
        for (Annotation methodAnnotation : method.getAnnotations()) {
            HttpMethod httpMethod = methodAnnotation.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                return httpMethod.value().toLowerCase();
            }
        }
        return null;
    }

    public Optional<Info> getInfo(io.swagger.oas.annotations.info.Info info) {
        if (info == null) {
            return Optional.empty();
        }
        Info infoObject = new Info();
        infoObject.setDescription(info.description());
        infoObject.setTermsOfService(info.termsOfService());
        infoObject.setTitle(info.title());
        infoObject.setVersion(info.version());
        infoObject.setContact(getContact(info.contact()).get());
        infoObject.setLicense(getLicense(info.license()).get());

        return Optional.of(infoObject);
    }

    public Optional<Contact> getContact(io.swagger.oas.annotations.info.Contact contact) {
        if (contact == null) {
            return Optional.empty();
        }
        Contact contactObject = new Contact();
        contactObject.setEmail(contact.email());
        contactObject.setName(contact.name());
        contactObject.setUrl(contact.url());
        return Optional.of(contactObject);
    }

    public Optional<License> getLicense(io.swagger.oas.annotations.info.License license) {
        if (license == null) {
            return Optional.empty();
        }
        License licenseObject = new License();
        licenseObject.setName(license.name());
        licenseObject.setUrl(license.url());

        return Optional.of(licenseObject);
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
            operation.setResponses(OperationParser.getApiResponses(apiOperation.responses(), apiLinks).get());

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
        operation.setTags(ReaderUtils.getStringListFromStringArray(apiOperation.tags()).get());
        openApiTags.addAll(OperationParser.getTags(apiOperation.tags()).get());
        operation.setSummary(apiOperation.summary());
        operation.setDescription(apiOperation.description());
        operation.setExternalDocs(OperationParser.getExternalDocumentation(apiOperation.externalDocs()).get());
        operation.setOperationId(apiOperation.operationId());
        operation.setParameters(OperationParser.getParametersList(apiOperation.parameters()).get());
        operation.setRequestBody(OperationParser.getRequestBody(apiOperation.requestBody()).get());
        operation.setDeprecated(apiOperation.deprecated());
        operation.setServers(OperationParser.getServers(apiOperation.servers()).get());
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
        // TODO improve existOperationId validation
        for (PathItem path : openAPI.getPaths().values()) {
            if (operationId.equalsIgnoreCase(path.getGet().getOperationId())) {
                return true;
            }

        }
        return false;
    }
}