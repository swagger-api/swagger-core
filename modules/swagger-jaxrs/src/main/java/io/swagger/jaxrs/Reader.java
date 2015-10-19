/**
 * Copyright 2015 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.jaxrs;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import io.swagger.annotations.Info;
import io.swagger.annotations.ResponseHeader;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.config.DefaultReaderConfig;
import io.swagger.jaxrs.config.ReaderConfig;
import io.swagger.jaxrs.config.ReaderListener;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtensions;
import io.swagger.jaxrs.utils.ReaderUtils;
import io.swagger.models.Contact;
import io.swagger.models.ExternalDocs;
import io.swagger.models.License;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.SecurityScope;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.util.BaseReaderUtils;
import io.swagger.util.ParameterProcessor;
import io.swagger.util.PathUtils;
import io.swagger.util.ReflectionUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Produces;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Reader {
    private static final Logger LOGGER = LoggerFactory.getLogger(Reader.class);
    private static final String SUCCESSFUL_OPERATION = "successful operation";
    private static final String PATH_DELIMITER = "/";

    private final ReaderConfig config;
    private Swagger swagger;

    public Reader(Swagger swagger) {
        this(swagger, null);
    }

    public Reader(Swagger swagger, ReaderConfig config) {
        this.swagger = swagger == null ? new Swagger() : swagger;
        this.config = new DefaultReaderConfig(config);
    }

    /**
     * Scans a set of classes for both ReaderListeners and Swagger annotations. All found listeners will
     * be instantiated before any of the classes are scanned for Swagger annotations - so they can be invoked
     * accordingly.
     *
     * @param classes a set of classes to scan
     * @return the generated Swagger definition
     */

    public Swagger read(Set<Class<?>> classes) {

        Map<Class<?>, ReaderListener> listeners = new HashMap<Class<?>, ReaderListener>();

        for (Class<?> cls : classes) {
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
                listener.beforeScan(this, swagger);
            } catch (Exception e) {
                LOGGER.error("Unexpected error invoking beforeScan listener [" + listener.getClass().getName() + "]", e);
            }
        }

        // process SwaggerDefinitions first - so we get tags in desired order
        for (Class<?> cls : classes) {
            SwaggerDefinition swaggerDefinition = cls.getAnnotation(SwaggerDefinition.class);
            if (swaggerDefinition != null) {
                readSwaggerConfig(cls, swaggerDefinition);
            }
        }

        for (Class<?> cls : classes) {
            read(cls);
        }

        for (ReaderListener listener : listeners.values()) {
            try {
                listener.afterScan(this, swagger);
            } catch (Exception e) {
                LOGGER.error("Unexpected error invoking afterScan listener [" + listener.getClass().getName() + "]", e);
            }
        }

        return swagger;
    }

    public Swagger getSwagger() {
        return this.swagger;
    }

    /**
     * Scans a single class for Swagger annotations - does not invoke ReaderListeners
     */

    public Swagger read(Class<?> cls) {
        return read(cls, "", null, false, new String[0], new String[0], new HashMap<String, Tag>(), new ArrayList<Parameter>(), new HashSet<Class<?>>());
    }
    protected Swagger read(Class<?> cls, String parentPath, String parentMethod, boolean readHidden, String[] parentConsumes, String[] parentProduces, Map<String, Tag> parentTags, List<Parameter> parentParameters) {
        return read(cls, parentPath, parentMethod, readHidden, parentConsumes, parentProduces, parentTags, parentParameters, new HashSet<Class<?>>());
    }

    private Swagger read(Class<?> cls, String parentPath, String parentMethod, boolean readHidden, String[] parentConsumes, String[] parentProduces, Map<String, Tag> parentTags, List<Parameter> parentParameters, Set<Class<?>> scannedResources) {
        Api api = (Api) cls.getAnnotation(Api.class);
        Map<String, SecurityScope> globalScopes = new HashMap<String, SecurityScope>();

        Map<String, Tag> tags = new HashMap<String, Tag>();
        List<SecurityRequirement> securities = new ArrayList<SecurityRequirement>();

        String[] consumes = new String[0];
        String[] produces = new String[0];
        final Set<Scheme> globalSchemes = EnumSet.noneOf(Scheme.class);

        // only read if allowing hidden apis OR api is not marked as hidden
        final boolean readable = (api != null && readHidden) || (api != null && !api.hidden());
        if (readable) {
            // the value will be used as a tag for 2.0 UNLESS a Tags annotation is present
            Set<String> tagStrings = extractTags(api);
            for (String tagString : tagStrings) {
                Tag tag = new Tag().name(tagString);
                tags.put(tagString, tag);
            }
            if (parentTags != null) {
                tags.putAll(parentTags);
            }
            for (String tagName : tags.keySet()) {
                swagger.tag(tags.get(tagName));
            }

            if (!api.produces().isEmpty()) {
                produces = new String[]{api.produces()};
            } else if (cls.getAnnotation(Produces.class) != null) {
                produces = ReaderUtils.splitContentValues(cls.getAnnotation(Produces.class).value());
            }
            if (!api.consumes().isEmpty()) {
                consumes = new String[]{api.consumes()};
            } else if (cls.getAnnotation(Consumes.class) != null) {
                consumes = ReaderUtils.splitContentValues(cls.getAnnotation(Consumes.class).value());
            }
            globalSchemes.addAll(parseSchemes(api.protocols()));
            Authorization[] authorizations = api.authorizations();

            for (Authorization auth : authorizations) {
                if (auth.value() != null && !"".equals(auth.value())) {
                    SecurityRequirement security = new SecurityRequirement();
                    security.setName(auth.value());
                    AuthorizationScope[] scopes = auth.scopes();
                    for (AuthorizationScope scope : scopes) {
                        if (scope.scope() != null && !"".equals(scope.scope())) {
                            security.addScope(scope.scope());
                        }
                    }
                    securities.add(security);
                }
            }
        }

        // allow reading the JAX-RS APIs without @Api annotation
        if (readable || (api == null && config.isScanAllResources())) {
            // merge consumes, produces

            // look for method-level annotated properties

            // handle sub-resources by looking at return type

            final List<Parameter> globalParameters = new ArrayList<Parameter>();

            // look for constructor-level annotated properties
            globalParameters.addAll(ReaderUtils.collectConstructorParameters(cls, swagger));

            // look for field-level annotated properties
            globalParameters.addAll(ReaderUtils.collectFieldParameters(cls, swagger));

            // parse the method
            final javax.ws.rs.Path apiPath = ReflectionUtils.getAnnotation(cls, javax.ws.rs.Path.class);
            Method methods[] = cls.getMethods();
            for (Method method : methods) {
                if (ReflectionUtils.isOverriddenMethod(method, cls)) {
                    continue;
                }
                javax.ws.rs.Path methodPath = ReflectionUtils.getAnnotation(method, javax.ws.rs.Path.class);

                String operationPath = getPath(apiPath, methodPath, parentPath);
                Map<String, String> regexMap = new HashMap<String, String>();
                operationPath = PathUtils.parsePath(operationPath, regexMap);
                if (operationPath != null) {
                    if (isIgnored(operationPath)) {
                        continue;
                    }

                    final ApiOperation apiOperation = ReflectionUtils.getAnnotation(method, ApiOperation.class);
                    String httpMethod = extractOperationMethod(apiOperation, method, SwaggerExtensions.chain());

                    Operation operation = null;
                    if(apiOperation != null || config.isScanAllResources() || httpMethod != null || methodPath != null) {
                        operation = parseMethod(cls, method, globalParameters);
                    }
                    if (operation == null) {
                        continue;
                    }
                    if (parentParameters != null) {
                        for (Parameter param : parentParameters) {
                            operation.parameter(param);
                        }
                    }
                    for (Parameter param : operation.getParameters()) {
                        if (regexMap.get(param.getName()) != null) {
                            String pattern = regexMap.get(param.getName());
                            param.setPattern(pattern);
                        }
                    }

                    if (apiOperation != null) {
                        for (Scheme scheme : parseSchemes(apiOperation.protocols())) {
                            operation.scheme(scheme);
                        }
                    }

                    if (operation.getSchemes() == null || operation.getSchemes().isEmpty()) {
                        for (Scheme scheme : globalSchemes) {
                            operation.scheme(scheme);
                        }
                    }

                    String[] apiConsumes = consumes;
                    if (parentConsumes != null) {
                        Set<String> both = new HashSet<String>(Arrays.asList(apiConsumes));
                        both.addAll(new HashSet<String>(Arrays.asList(parentConsumes)));
                        if (operation.getConsumes() != null) {
                            both.addAll(new HashSet<String>(operation.getConsumes()));
                        }
                        apiConsumes = both.toArray(new String[both.size()]);
                    }

                    String[] apiProduces = produces;
                    if (parentProduces != null) {
                        Set<String> both = new HashSet<String>(Arrays.asList(apiProduces));
                        both.addAll(new HashSet<String>(Arrays.asList(parentProduces)));
                        if (operation.getProduces() != null) {
                            both.addAll(new HashSet<String>(operation.getProduces()));
                        }
                        apiProduces = both.toArray(new String[both.size()]);
                    }
                    final Class<?> subResource = getSubResource(method);
                    if (subResource != null && !scannedResources.contains(subResource)) {
                        scannedResources.add(subResource);
                        read(subResource, operationPath, httpMethod, true, apiConsumes, apiProduces, tags, operation.getParameters(), scannedResources);
                    }

                    // can't continue without a valid http method
                    httpMethod = httpMethod == null ? parentMethod : httpMethod;
                    if (httpMethod != null) {
                        if (apiOperation != null) {
                            boolean hasExplicitTag = false;
                            for (String tag : apiOperation.tags()) {
                                if (!"".equals(tag)) {
                                    operation.tag(tag);
                                    swagger.tag(new Tag().name(tag));
                                }
                            }

                            if (operation != null) {
                                operation.getVendorExtensions().putAll(BaseReaderUtils.parseExtensions(apiOperation.extensions()));
                            }
                        }
                        if (operation != null) {
                            if (operation.getConsumes() == null) {
                                for (String mediaType : apiConsumes) {
                                    operation.consumes(mediaType);
                                }
                            }
                            if (operation.getProduces() == null) {
                                for (String mediaType : apiProduces) {
                                    operation.produces(mediaType);
                                }
                            }

                            if (operation.getTags() == null) {
                                for (String tagString : tags.keySet()) {
                                    operation.tag(tagString);
                                }
                            }
                            // Only add global @Api securities if operation doesn't already have more specific securities
                            if (operation.getSecurity() == null) {
                                for (SecurityRequirement security : securities) {
                                    operation.security(security);
                                }
                            }

                            Path path = swagger.getPath(operationPath);
                            if (path == null) {
                                path = new Path();
                                swagger.path(operationPath, path);
                            }
                            path.set(httpMethod, operation);

                            readImplicitParameters(method, operation);
                        }
                    }
                }
            }
        }

        return swagger;
    }

    private void readImplicitParameters(Method method, Operation operation) {
        ApiImplicitParams implicitParams = method.getAnnotation(ApiImplicitParams.class);
        if (implicitParams != null && implicitParams.value().length > 0) {
            for (ApiImplicitParam param : implicitParams.value()) {
                Parameter p = readImplicitParam(param);
                if (p != null) {
                    operation.addParameter(p);
                }
            }
        }
    }

    protected Parameter readImplicitParam(ApiImplicitParam param) {
        final Parameter p;
        if (param.paramType().equalsIgnoreCase("path")) {
            p = new PathParameter();
        } else if (param.paramType().equalsIgnoreCase("query")) {
            p = new QueryParameter();
        } else if (param.paramType().equalsIgnoreCase("form") || param.paramType().equalsIgnoreCase("formData")) {
            p = new FormParameter();
        } else if (param.paramType().equalsIgnoreCase("body")) {
            p = null;
        } else if (param.paramType().equalsIgnoreCase("header")) {
            p = new HeaderParameter();
        } else {
            LOGGER.warn("Unkown implicit parameter type: [" + param.paramType() + "]");
            return null;
        }
        final Type type = ReflectionUtils.typeFromString(param.dataType());
        return ParameterProcessor.applyAnnotations(swagger, p, type == null ? String.class : type,
                Arrays.<Annotation>asList(param));
    }

    protected void readSwaggerConfig(Class<?> cls, SwaggerDefinition config) {

        if (!config.basePath().isEmpty()) {
            swagger.setBasePath(config.basePath());
        }

        if (!config.host().isEmpty()) {
            swagger.setHost(config.host());
        }

        readInfoConfig(config);

        for (String consume : config.consumes()) {
            if (StringUtils.isNotEmpty(consume)) {
                swagger.addConsumes(consume);
            }
        }

        for (String produce : config.produces()) {
            if (StringUtils.isNotEmpty(produce)) {
                swagger.addProduces(produce);
            }
        }

        if (!config.externalDocs().value().isEmpty()) {
            ExternalDocs externalDocs = swagger.getExternalDocs();
            if (externalDocs == null) {
                externalDocs = new ExternalDocs();
                swagger.setExternalDocs(externalDocs);
            }

            externalDocs.setDescription(config.externalDocs().value());

            if (!config.externalDocs().url().isEmpty()) {
                externalDocs.setUrl(config.externalDocs().url());
            }
        }

        for (io.swagger.annotations.Tag tagConfig : config.tags()) {
            if (!tagConfig.name().isEmpty()) {
                Tag tag = new Tag();
                tag.setName(tagConfig.name());
                tag.setDescription(tagConfig.description());

                if (!tagConfig.externalDocs().value().isEmpty()) {
                    tag.setExternalDocs(new ExternalDocs(tagConfig.externalDocs().value(),
                            tagConfig.externalDocs().url()));
                }

                tag.getVendorExtensions().putAll(BaseReaderUtils.parseExtensions(tagConfig.extensions()));

                swagger.addTag(tag);
            }
        }

        for (SwaggerDefinition.Scheme scheme : config.schemes()) {
            if (scheme != SwaggerDefinition.Scheme.DEFAULT) {
                swagger.addScheme(Scheme.forValue(scheme.name()));
            }
        }
    }

    protected void readInfoConfig(SwaggerDefinition config) {
        Info infoConfig = config.info();
        io.swagger.models.Info info = swagger.getInfo();
        if (info == null) {
            info = new io.swagger.models.Info();
            swagger.setInfo(info);
        }

        if (!infoConfig.description().isEmpty()) {
            info.setDescription(infoConfig.description());
        }

        if (!infoConfig.termsOfService().isEmpty()) {
            info.setTermsOfService(infoConfig.termsOfService());
        }

        if (!infoConfig.title().isEmpty()) {
            info.setTitle(infoConfig.title());
        }

        if (!infoConfig.version().isEmpty()) {
            info.setVersion(infoConfig.version());
        }

        if (!infoConfig.contact().name().isEmpty()) {
            Contact contact = info.getContact();
            if (contact == null) {
                contact = new Contact();
                info.setContact(contact);
            }

            contact.setName(infoConfig.contact().name());
            if (!infoConfig.contact().email().isEmpty()) {
                contact.setEmail(infoConfig.contact().email());
            }

            if (!infoConfig.contact().url().isEmpty()) {
                contact.setUrl(infoConfig.contact().url());
            }
        }

        if (!infoConfig.license().name().isEmpty()) {
            License license = info.getLicense();
            if (license == null) {
                license = new License();
                info.setLicense(license);
            }

            license.setName(infoConfig.license().name());
            if (!infoConfig.license().url().isEmpty()) {
                license.setUrl(infoConfig.license().url());
            }
        }

        info.getVendorExtensions().putAll(BaseReaderUtils.parseExtensions(infoConfig.extensions()));
    }

    protected Class<?> getSubResource(Method method) {
        final Class<?> rawType = method.getReturnType();
        final Class<?> type;
        if (Class.class.equals(rawType)) {
            type = getClassArgument(method.getGenericReturnType());
            if (type == null) {
                return null;
            }
        } else {
            type = rawType;
        }

        if (type.getAnnotation(Api.class) != null) {
            return type;
        }

        if (config.isScanAllResources()) {
            // For sub-resources that are not annotated with  @Api, look for any HttpMethods.
            for (Method m : type.getMethods()) {
                if (extractOperationMethod(null, m, null) != null) {
                    return type;
                }
            }
        }

        return null;
    }

    private static Class<?> getClassArgument(Type cls) {
        if (cls instanceof ParameterizedType) {
            final ParameterizedType parameterized = (ParameterizedType) cls;
            final Type[] args = parameterized.getActualTypeArguments();
            if (args.length != 1) {
                LOGGER.error(String.format("Unexpected class definition: %s", cls));
                return null;
            }
            final Type first = args[0];
            if (first instanceof Class) {
                return (Class<?>) first;
            } else {
                return null;
            }
        } else {
            LOGGER.error(String.format("Unknown class definition: %s", cls));
            return null;
        }
    }

    protected Set<String> extractTags(Api api) {
        Set<String> output = new LinkedHashSet<String>();

        boolean hasExplicitTags = false;
        for (String tag : api.tags()) {
            if (!"".equals(tag)) {
                hasExplicitTags = true;
                output.add(tag);
            }
        }
        if (!hasExplicitTags) {
            // derive tag from api path + description
            String tagString = api.value().replace("/", "");
            if (!"".equals(tagString)) {
                output.add(tagString);
            }
        }
        return output;
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

    private Map<String, Property> parseResponseHeaders(ResponseHeader[] headers) {
        Map<String, Property> responseHeaders = null;
        if (headers != null && headers.length > 0) {
            for (ResponseHeader header : headers) {
                String name = header.name();
                if (!"".equals(name)) {
                    if (responseHeaders == null) {
                        responseHeaders = new HashMap<String, Property>();
                    }
                    String description = header.description();
                    Class<?> cls = header.response();

                    if (!isVoid(cls)) {
                        final Property property = ModelConverters.getInstance().readAsProperty(cls);
                        if (property != null) {
                            Property responseProperty = ContainerWrapper.wrapContainer(header.responseContainer(), property,
                                    ContainerWrapper.ARRAY, ContainerWrapper.LIST, ContainerWrapper.SET);
                            responseProperty.setDescription(description);
                            responseHeaders.put(name, responseProperty);
                            appendModels(cls);
                        }
                    }
                }
            }
        }
        return responseHeaders;
    }

    public Operation parseMethod(Method method) {
        return parseMethod(method.getDeclaringClass(), method, Collections.<Parameter>emptyList());
    }

    private Operation parseMethod(Class<?> cls, Method method, List<Parameter> globalParameters) {
        Operation operation = new Operation();

        ApiOperation apiOperation = ReflectionUtils.getAnnotation(method, ApiOperation.class);
        ApiResponses responseAnnotation = ReflectionUtils.getAnnotation(method, ApiResponses.class);

        String operationId = method.getName();
        String responseContainer = null;

        Type responseType = null;
        Map<String, Property> defaultResponseHeaders = new HashMap<String, Property>();

        if (apiOperation != null) {
            if (apiOperation.hidden()) {
                return null;
            }
            if (!"".equals(apiOperation.nickname())) {
                operationId = apiOperation.nickname();
            }

            defaultResponseHeaders = parseResponseHeaders(apiOperation.responseHeaders());

            operation
                    .summary(apiOperation.value())
                    .description(apiOperation.notes());

            if (apiOperation.response() != null && !isVoid(apiOperation.response())) {
                responseType = apiOperation.response();
            }
            if (!"".equals(apiOperation.responseContainer())) {
                responseContainer = apiOperation.responseContainer();
            }
            if (apiOperation.authorizations() != null) {
                List<SecurityRequirement> securities = new ArrayList<SecurityRequirement>();
                for (Authorization auth : apiOperation.authorizations()) {
                    if (auth.value() != null && !"".equals(auth.value())) {
                        SecurityRequirement security = new SecurityRequirement();
                        security.setName(auth.value());
                        AuthorizationScope[] scopes = auth.scopes();
                        for (AuthorizationScope scope : scopes) {
                            if (scope.scope() != null && !"".equals(scope.scope())) {
                                security.addScope(scope.scope());
                            }
                        }
                        securities.add(security);
                    }
                }
                if (securities.size() > 0) {
                    for (SecurityRequirement sec : securities) {
                        operation.security(sec);
                    }
                }
            }
            if (apiOperation.consumes() != null && !apiOperation.consumes().isEmpty()) {
                operation.consumes(apiOperation.consumes());
            }
            if (apiOperation.produces() != null && !apiOperation.produces().isEmpty()) {
                operation.produces(apiOperation.produces());
            }
        }

        if (apiOperation != null && StringUtils.isNotEmpty(apiOperation.responseReference())) {
            Response response = new Response().description(SUCCESSFUL_OPERATION);
            response.schema(new RefProperty(apiOperation.responseReference()));
            operation.addResponse(String.valueOf(apiOperation.code()), response);
        } else if (responseType == null) {
            // pick out response from method declaration
            LOGGER.debug("picking up response class from method " + method);
            responseType = method.getGenericReturnType();
        }
        if (isValidResponse(responseType)) {
            final Property property = ModelConverters.getInstance().readAsProperty(responseType);
            if (property != null) {
                final Property responseProperty = ContainerWrapper.wrapContainer(responseContainer, property);
                final int responseCode = apiOperation == null ? 200 : apiOperation.code();
                operation.response(responseCode, new Response().description(SUCCESSFUL_OPERATION).schema(responseProperty)
                        .headers(defaultResponseHeaders));
                appendModels(responseType);
            }
        }

        operation.operationId(operationId);

        if (apiOperation != null && apiOperation.consumes() != null && apiOperation.consumes().isEmpty()) {
            final Consumes consumes = ReflectionUtils.getAnnotation(method, Consumes.class);
            if (consumes != null) {
                for (String mediaType : ReaderUtils.splitContentValues(consumes.value())) {
                    operation.consumes(mediaType);
                }
            }
        }

        if (apiOperation != null && apiOperation.produces() != null && apiOperation.produces().isEmpty()) {
            final Produces produces = ReflectionUtils.getAnnotation(method, Produces.class);
            if (produces != null) {
                for (String mediaType : ReaderUtils.splitContentValues(produces.value())) {
                    operation.produces(mediaType);
                }
            }
        }

        List<ApiResponse> apiResponses = new ArrayList<ApiResponse>();
        if (responseAnnotation != null) {
            for (ApiResponse apiResponse : responseAnnotation.value()) {
                Map<String, Property> responseHeaders = parseResponseHeaders(apiResponse.responseHeaders());

                Response response = new Response()
                        .description(apiResponse.message())
                        .headers(responseHeaders);

                if (apiResponse.code() == 0) {
                    operation.defaultResponse(response);
                } else {
                    operation.response(apiResponse.code(), response);
                }

                if (StringUtils.isNotEmpty(apiResponse.reference())) {
                    response.schema(new RefProperty(apiResponse.reference()));
                } else if (!isVoid(apiResponse.response())) {
                    responseType = apiResponse.response();
                    final Property property = ModelConverters.getInstance().readAsProperty(responseType);
                    if (property != null) {
                        response.schema(ContainerWrapper.wrapContainer(apiResponse.responseContainer(), property));
                        appendModels(responseType);
                    }
                }
            }
        }
        if (ReflectionUtils.getAnnotation(method, Deprecated.class) != null) {
            operation.setDeprecated(true);
        }

        // process parameters
        for (Parameter globalParameter : globalParameters) {
            operation.parameter(globalParameter);
        }

        Type[] genericParameterTypes = method.getGenericParameterTypes();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < genericParameterTypes.length; i++) {
            final Type type = TypeFactory.defaultInstance().constructType(genericParameterTypes[i], cls);
            List<Parameter> parameters = getParameters(type, Arrays.asList(paramAnnotations[i]));

            for (Parameter parameter : parameters) {
                operation.parameter(parameter);
            }
        }

        if (operation.getResponses() == null) {
            Response response = new Response().description(SUCCESSFUL_OPERATION);
            operation.defaultResponse(response);
        }
        return operation;
    }

    private List<Parameter> getParameters(Type type, List<Annotation> annotations) {
        final Iterator<SwaggerExtension> chain = SwaggerExtensions.chain();
        if (!chain.hasNext()) {
            return Collections.emptyList();
        }
        LOGGER.debug("getParameters for " + type);
        Set<Type> typesToSkip = new HashSet<Type>();
        final SwaggerExtension extension = chain.next();
        LOGGER.debug("trying extension " + extension);

        final List<Parameter> parameters = extension.extractParameters(annotations, type, typesToSkip, chain);
        if (parameters.size() > 0) {
            final List<Parameter> processed = new ArrayList<Parameter>(parameters.size());
            for (Parameter parameter : parameters) {
                if (ParameterProcessor.applyAnnotations(swagger, parameter, type, annotations) != null) {
                    processed.add(parameter);
                }
            }
            return processed;
        } else {
            LOGGER.debug("no parameter found, looking at body params");
            final List<Parameter> body = new ArrayList<Parameter>();
            if (!typesToSkip.contains(type)) {
                Parameter param = ParameterProcessor.applyAnnotations(swagger, null, type, annotations);
                if (param != null) {
                    body.add(param);
                }
            }
            return body;
        }
    }

    public String extractOperationMethod(ApiOperation apiOperation, Method method, Iterator<SwaggerExtension> chain) {
        if (apiOperation != null && apiOperation.httpMethod() != null && !"".equals(apiOperation.httpMethod())) {
            return apiOperation.httpMethod().toLowerCase();
        } else if (method.getAnnotation(javax.ws.rs.GET.class) != null) {
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
        } else if (method.getAnnotation(PATCH.class) != null) {
            return "patch";
        } else if (method.getAnnotation(HttpMethod.class) != null) {
            HttpMethod httpMethod = (HttpMethod) method.getAnnotation(HttpMethod.class);
            return httpMethod.value().toLowerCase();
        } else if ((ReflectionUtils.getOverriddenMethod(method)) != null) {
            return extractOperationMethod(apiOperation, ReflectionUtils.getOverriddenMethod(method), chain);
        } else if (chain != null && chain.hasNext()) {
            return chain.next().extractOperationMethod(apiOperation, method, chain);
        } else {
            return null;
        }
    }

    private static Set<Scheme> parseSchemes(String schemes) {
        final Set<Scheme> result = EnumSet.noneOf(Scheme.class);
        for (String item : StringUtils.trimToEmpty(schemes).split(",")) {
            final Scheme scheme = Scheme.forValue(StringUtils.trimToNull(item));
            if (scheme != null) {
                result.add(scheme);
            }
        }
        return result;
    }

    private void appendModels(Type type) {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(type);
        for (Map.Entry<String, Model> entry : models.entrySet()) {
            swagger.model(entry.getKey(), entry.getValue());
        }
    }

    private static boolean isVoid(Type type) {
        final Class<?> cls = TypeFactory.defaultInstance().constructType(type).getRawClass();
        return Void.class.isAssignableFrom(cls) || Void.TYPE.isAssignableFrom(cls);
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

    private static boolean isValidResponse(Type type) {
        if (type == null) {
            return false;
        }
        final JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        if (isVoid(javaType)) {
            return false;
        }
        final Class<?> cls = javaType.getRawClass();
        return !javax.ws.rs.core.Response.class.isAssignableFrom(cls) && !isResourceClass(cls);
    }

    private static boolean isResourceClass(Class<?> cls) {
        return cls.getAnnotation(Api.class) != null;
    }

    public ReaderConfig getConfig() {
        return config;
    }

    enum ContainerWrapper {
        LIST("list") {
            @Override
            protected Property doWrap(Property property) {
                return new ArrayProperty(property);
            }
        },
        ARRAY("array") {
            @Override
            protected Property doWrap(Property property) {
                return new ArrayProperty(property);
            }
        },
        MAP("map") {
            @Override
            protected Property doWrap(Property property) {
                return new MapProperty(property);
            }
        },
        SET("set") {
            @Override
            protected Property doWrap(Property property) {
                ArrayProperty arrayProperty = new ArrayProperty(property);
                arrayProperty.setUniqueItems(true);
                return arrayProperty;
            }
        };

        private final String container;

        ContainerWrapper(String container) {
            this.container = container;
        }

        public static Property wrapContainer(String container, Property property, ContainerWrapper... allowed) {
            final Set<ContainerWrapper> tmp = allowed.length > 0 ? EnumSet.copyOf(Arrays.asList(allowed)) : EnumSet.allOf(ContainerWrapper.class);
            for (ContainerWrapper wrapper : tmp) {
                final Property prop = wrapper.wrap(container, property);
                if (prop != null) {
                    return prop;
                }
            }
            return property;
        }

        public Property wrap(String container, Property property) {
            if (this.container.equalsIgnoreCase(container)) {
                return doWrap(property);
            }
            return null;
        }

        protected abstract Property doWrap(Property property);
    }
}
