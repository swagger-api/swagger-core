/**
 * Copyright 2016 SmartBear Software
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

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiKeyAuthDefinition;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import io.swagger.annotations.BasicAuthDefinition;
import io.swagger.annotations.Info;
import io.swagger.annotations.OAuth2Definition;
import io.swagger.annotations.ResponseHeader;
import io.swagger.annotations.Scope;
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
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.auth.In;
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
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
        this.swagger = (swagger == null) ? new Swagger() : swagger;
        this.config = new DefaultReaderConfig(config);
    }

    public Swagger getSwagger() {
        return swagger;
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
        Set<Class<?>> sortedClasses = new TreeSet<Class<?>>(new Comparator<Class<?>>() {
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

        Map<Class<?>, ReaderListener> listeners = new HashMap<Class<?>, ReaderListener>();

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
                listener.beforeScan(this, swagger);
            } catch (Exception e) {
                LOGGER.error("Unexpected error invoking beforeScan listener [" + listener.getClass().getName() + "]", e);
            }
        }

        // process SwaggerDefinitions first - so we get tags in desired order
        for (Class<?> cls : sortedClasses) {
            SwaggerDefinition swaggerDefinition = cls.getAnnotation(SwaggerDefinition.class);
            if (swaggerDefinition != null) {
                readSwaggerConfig(cls, swaggerDefinition);
            }
        }

        for (Class<?> cls : sortedClasses) {
            read(cls, "", null, false, new String[0], new String[0], new LinkedHashMap<String, Tag>(), new ArrayList<Parameter>(), new HashSet<Class<?>>());
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

    /**
     * Scans a single class for Swagger annotations - does not invoke ReaderListeners
     */
    public Swagger read(Class<?> cls) {
        SwaggerDefinition swaggerDefinition = cls.getAnnotation(SwaggerDefinition.class);
        if (swaggerDefinition != null) {
            readSwaggerConfig(cls, swaggerDefinition);
        }

        return read(cls, "", null, false, new String[0], new String[0], new LinkedHashMap<String, Tag>(), new ArrayList<Parameter>(), new HashSet<Class<?>>());
    }

    protected Swagger read(Class<?> cls, String parentPath, String parentMethod, boolean isSubresource, String[] parentConsumes, String[] parentProduces, Map<String, Tag> parentTags, List<Parameter> parentParameters) {
        return read(cls, parentPath, parentMethod, isSubresource, parentConsumes, parentProduces, parentTags, parentParameters, new HashSet<Class<?>>());
    }

    private Swagger read(Class<?> cls, String parentPath, String parentMethod, boolean isSubresource, String[] parentConsumes, String[] parentProduces, Map<String, Tag> parentTags, List<Parameter> parentParameters, Set<Class<?>> scannedResources) {
        Map<String, Tag> tags = new LinkedHashMap<String, Tag>();
        List<SecurityRequirement> securities = new ArrayList<SecurityRequirement>();

        String[] consumes = new String[0];
        String[] produces = new String[0];
        final Set<Scheme> globalSchemes = EnumSet.noneOf(Scheme.class);

        Api api = ReflectionUtils.getAnnotation(cls, Api.class);

        boolean hasPathAnnotation = (ReflectionUtils.getAnnotation(cls, javax.ws.rs.Path.class) != null);
        boolean hasApiAnnotation = (api != null);
        boolean isApiHidden = hasApiAnnotation && api.hidden();

        // class readable only if annotated with ((@Path and @Api) or isSubresource ) - and @Api not hidden
        boolean classReadable = ((hasPathAnnotation && hasApiAnnotation) || isSubresource) && !isApiHidden;

        // with scanAllResources true in config and @Api not hidden scan only if it has also @Path annotation or is subresource
        boolean scanAll = !isApiHidden && config.isScanAllResources() && (hasPathAnnotation || isSubresource);

        // readable if classReadable or scanAll
        boolean readable = classReadable || scanAll;

        if (!readable) {
            return swagger;
        }

        // api readable only if @Api present; cannot be hidden because checked in classReadable.
        boolean apiReadable = hasApiAnnotation;

        if (apiReadable) {
            // the value will be used as a tag for 2.0 UNLESS a Tags annotation is present
            Set<String> tagStrings = extractTags(api);
            for (String tagString : tagStrings) {
                Tag tag = new Tag().name(tagString);
                tags.put(tagString, tag);
            }
            for (String tagName : tags.keySet()) {
                swagger.tag(tags.get(tagName));
            }

            if (!api.produces().isEmpty()) {
                produces = ReaderUtils.splitContentValues(new String[]{api.produces()});
            }
            if (!api.consumes().isEmpty()) {
                consumes = ReaderUtils.splitContentValues(new String[]{api.consumes()});
            }
            globalSchemes.addAll(parseSchemes(api.protocols()));

            for (Authorization auth : api.authorizations()) {
                if (auth.value() != null && !auth.value().isEmpty()) {
                    SecurityRequirement security = new SecurityRequirement();
                    security.setName(auth.value());
                    for (AuthorizationScope scope : auth.scopes()) {
                        if (scope.scope() != null && !scope.scope().isEmpty()) {
                            security.addScope(scope.scope());
                        }
                    }
                    securities.add(security);
                }
            }
        }

        if (readable) {
            if (isSubresource) {
                if (parentTags != null) {
                    tags.putAll(parentTags);
                }
            }
            // merge consumes, produces
            if (consumes.length == 0 && cls.getAnnotation(Consumes.class) != null) {
                consumes = ReaderUtils.splitContentValues(cls.getAnnotation(Consumes.class).value());
            }
            if (produces.length == 0 && cls.getAnnotation(Produces.class) != null) {
                produces = ReaderUtils.splitContentValues(cls.getAnnotation(Produces.class).value());
            }
            // look for method-level annotated properties

            // handle sub-resources by looking at return type

            final List<Parameter> globalParameters = new ArrayList<Parameter>();

            // look for constructor-level annotated properties
            globalParameters.addAll(ReaderUtils.collectConstructorParameters(cls, swagger));

            // look for field-level annotated properties
            globalParameters.addAll(ReaderUtils.collectFieldParameters(cls, swagger));

            // build class/interface level @ApiResponse list
            ApiResponses classResponseAnnotation = ReflectionUtils.getAnnotation(cls, ApiResponses.class);
            List<ApiResponse> classApiResponses = new ArrayList<ApiResponse>();
            if (classResponseAnnotation != null) {
                classApiResponses.addAll(Arrays.asList(classResponseAnnotation.value()));
            }

            // parse the method
            final javax.ws.rs.Path apiPath = ReflectionUtils.getAnnotation(cls, javax.ws.rs.Path.class);
            JavaType classType = TypeFactory.defaultInstance().constructType(cls);
            BeanDescription bd = new ObjectMapper().getSerializationConfig().introspect(classType);
            Method methods[] = cls.getMethods();
            for (Method method : methods) {
                AnnotatedMethod annotatedMethod = bd.findMethod(method.getName(), method.getParameterTypes());
                if (ReflectionUtils.isOverriddenMethod(method, cls)) {
                    continue;
                }
                javax.ws.rs.Path methodPath = ReflectionUtils.getAnnotation(method, javax.ws.rs.Path.class);

                String operationPath = getPath(apiPath, methodPath, parentPath);
                Map<String, String> regexMap = new LinkedHashMap<String, String>();
                operationPath = PathUtils.parsePath(operationPath, regexMap);
                if (operationPath != null) {
                    if (isIgnored(operationPath)) {
                        continue;
                    }

                    final ApiOperation apiOperation = ReflectionUtils.getAnnotation(method, ApiOperation.class);
                    String httpMethod = extractOperationMethod(apiOperation, method, SwaggerExtensions.chain());

                    Operation operation = null;
                    if (apiOperation != null || config.isScanAllResources() || httpMethod != null || methodPath != null) {
                        operation = parseMethod(cls, method, annotatedMethod, globalParameters, classApiResponses);
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
                        Set<String> both = new LinkedHashSet<String>(Arrays.asList(apiConsumes));
                        both.addAll(new LinkedHashSet<String>(Arrays.asList(parentConsumes)));
                        if (operation.getConsumes() != null) {
                            both.addAll(new LinkedHashSet<String>(operation.getConsumes()));
                        }
                        apiConsumes = both.toArray(new String[both.size()]);
                    }

                    String[] apiProduces = produces;
                    if (parentProduces != null) {
                        Set<String> both = new LinkedHashSet<String>(Arrays.asList(apiProduces));
                        both.addAll(new LinkedHashSet<String>(Arrays.asList(parentProduces)));
                        if (operation.getProduces() != null) {
                            both.addAll(new LinkedHashSet<String>(operation.getProduces()));
                        }
                        apiProduces = both.toArray(new String[both.size()]);
                    }
                    final Class<?> subResource = getSubResourceWithJaxRsSubresourceLocatorSpecs(method);
                    if (subResource != null && !scannedResources.contains(subResource)) {
                        scannedResources.add(subResource);
                        read(subResource, operationPath, httpMethod, true, apiConsumes, apiProduces, tags, operation.getParameters(), scannedResources);
                        // remove the sub resource so that it can visit it later in another path
                        // but we have a room for optimization in the future to reuse the scanned result
                        // by caching the scanned resources in the reader instance to avoid actual scanning
                        // the the resources again
                        scannedResources.remove(subResource);
                    }

                    // can't continue without a valid http method
                    httpMethod = (httpMethod == null) ? parentMethod : httpMethod;
                    if (httpMethod != null) {
                        if (apiOperation != null) {
                            for (String tag : apiOperation.tags()) {
                                if (!"".equals(tag)) {
                                    operation.tag(tag);
                                    swagger.tag(new Tag().name(tag));
                                }
                            }

                            operation.getVendorExtensions().putAll(BaseReaderUtils.parseExtensions(apiOperation.extensions()));
                        }

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

                        readExternalDocs(method, operation);
                    }
                }
            }
        }

        return swagger;
    }

    private void readImplicitParameters(Method method, Operation operation) {
        processImplicitParams(ReflectionUtils.getAnnotation(method, ApiImplicitParams.class), operation);
        processImplicitParams(ReflectionUtils.getAnnotation(method.getDeclaringClass(), ApiImplicitParams.class), operation);
    }

    private void processImplicitParams(ApiImplicitParams implicitParams, Operation operation) {
        if (implicitParams != null) {
            for (ApiImplicitParam param : implicitParams.value()) {
                Parameter p = readImplicitParam(param);
                if (p != null) {
                    operation.addParameter(p);
                }
            }
        }
    }

    private void readExternalDocs(Method method, Operation operation) {
        io.swagger.annotations.ExternalDocs externalDocs = ReflectionUtils.getAnnotation(method, io.swagger.annotations.ExternalDocs.class);
        if(externalDocs != null) {
            operation.setExternalDocs(new ExternalDocs(externalDocs.value(), externalDocs.url()));
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
            LOGGER.warn("Unknown implicit parameter type: [{}]", param.paramType());
            return null;
        }
        final Type type = param.dataTypeClass() == Void.class ? ReflectionUtils.typeFromString(param.dataType())
                : param.dataTypeClass();
        return ParameterProcessor.applyAnnotations(swagger, p, (type == null) ? String.class : type,
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

        for (OAuth2Definition oAuth2Config : config.securityDefinition().oAuth2Definitions()) {
            io.swagger.models.auth.OAuth2Definition oAuth2Definition = new io.swagger.models.auth.OAuth2Definition();
            OAuth2Definition.Flow flow = oAuth2Config.flow();

            if (flow.equals(OAuth2Definition.Flow.ACCESS_CODE)) {
                oAuth2Definition = oAuth2Definition.accessCode(oAuth2Config.authorizationUrl(), oAuth2Config.tokenUrl());
            } else if (flow.equals(OAuth2Definition.Flow.APPLICATION)) {
                oAuth2Definition = oAuth2Definition.application(oAuth2Config.tokenUrl());
            } else if (flow.equals(OAuth2Definition.Flow.IMPLICIT)) {
                oAuth2Definition = oAuth2Definition.implicit(oAuth2Config.authorizationUrl());
            } else {
                oAuth2Definition = oAuth2Definition.password(oAuth2Config.tokenUrl());
            }

            for (Scope scope : oAuth2Config.scopes()) {
                oAuth2Definition.addScope(scope.name(), scope.description());
            }

            oAuth2Definition.setDescription(oAuth2Config.description());
            swagger.addSecurityDefinition(oAuth2Config.key(), oAuth2Definition);
        }

        for (ApiKeyAuthDefinition[] apiKeyAuthConfigs : new ApiKeyAuthDefinition[][] {
                config.securityDefinition().apiKeyAuthDefintions(), config.securityDefinition().apiKeyAuthDefinitions() }) {
            for (ApiKeyAuthDefinition apiKeyAuthConfig : apiKeyAuthConfigs) {
                io.swagger.models.auth.ApiKeyAuthDefinition apiKeyAuthDefinition = new io.swagger.models.auth.ApiKeyAuthDefinition();

                apiKeyAuthDefinition.setName(apiKeyAuthConfig.name());
                apiKeyAuthDefinition.setIn(In.forValue(apiKeyAuthConfig.in().toValue()));
                apiKeyAuthDefinition.setDescription(apiKeyAuthConfig.description());

                swagger.addSecurityDefinition(apiKeyAuthConfig.key(), apiKeyAuthDefinition);
            }
        }

        for (BasicAuthDefinition[] basicAuthConfigs : new BasicAuthDefinition[][] {
                config.securityDefinition().basicAuthDefinions(), config.securityDefinition().basicAuthDefinitions() }) {
            for (BasicAuthDefinition basicAuthConfig : basicAuthConfigs) {
                io.swagger.models.auth.BasicAuthDefinition basicAuthDefinition = new io.swagger.models.auth.BasicAuthDefinition();

                basicAuthDefinition.setDescription(basicAuthConfig.description());

                swagger.addSecurityDefinition(basicAuthConfig.key(), basicAuthDefinition);
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

        // For sub-resources that are not annotated with  @Api, look for any HttpMethods.
        for (Method m : type.getMethods()) {
            if (extractOperationMethod(null, m, null) != null) {
                return type;
            }
        }

        return null;
    }

    protected Class<?> getSubResourceWithJaxRsSubresourceLocatorSpecs(Method method) {
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

        if (method.getAnnotation(javax.ws.rs.Path.class) != null) {
            if (extractOperationMethod(null, method, null) == null) {
                return type;
            }
        }
        return null;
    }

    private static Class<?> getClassArgument(Type cls) {
        if (cls instanceof ParameterizedType) {
            final ParameterizedType parameterized = (ParameterizedType) cls;
            final Type[] args = parameterized.getActualTypeArguments();
            if (args.length != 1) {
                LOGGER.error("Unexpected class definition: {}", cls);
                return null;
            }
            final Type first = args[0];
            if (first instanceof Class) {
                return (Class<?>) first;
            } else {
                return null;
            }
        } else {
            LOGGER.error("Unknown class definition: {}", cls);
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
        if (headers != null) {
            for (ResponseHeader header : headers) {
                String name = header.name();
                if (!"".equals(name)) {
                    if (responseHeaders == null) {
                        responseHeaders = new LinkedHashMap<String, Property>();
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
        JavaType classType = TypeFactory.defaultInstance().constructType(method.getDeclaringClass());
        BeanDescription bd = new ObjectMapper().getSerializationConfig().introspect(classType);
        return parseMethod(classType.getClass(), method, bd.findMethod(method.getName(), method.getParameterTypes()),
                Collections.<Parameter> emptyList(), Collections.<ApiResponse> emptyList());
    }

    private Operation parseMethod(Class<?> cls, Method method, AnnotatedMethod annotatedMethod,
            List<Parameter> globalParameters, List<ApiResponse> classApiResponses) {
        Operation operation = new Operation();
        if (annotatedMethod != null) {
            method = annotatedMethod.getAnnotated();
        }
        ApiOperation apiOperation = ReflectionUtils.getAnnotation(method, ApiOperation.class);
        ApiResponses responseAnnotation = ReflectionUtils.getAnnotation(method, ApiResponses.class);

        String operationId = null;
        // check if it's an inherited or implemented method.
        boolean methodInSuperType = false;
        if (!cls.isInterface()) {
            methodInSuperType = ReflectionUtils.findMethod(method, cls.getSuperclass()) != null;
        }
        if (!methodInSuperType) {
            for (Class<?> implementedInterface : cls.getInterfaces()) {
                methodInSuperType = ReflectionUtils.findMethod(method, implementedInterface) != null;
                if (methodInSuperType) {
                    break;
                }
            }
        }
        if (!methodInSuperType) {
            operationId = method.getName();
        } else {
            operationId = this.getOperationId(method.getName());
        }
        String responseContainer = null;

        Type responseType = null;
        Map<String, Property> defaultResponseHeaders = new LinkedHashMap<String, Property>();

        if (apiOperation != null) {
            if (apiOperation.hidden()) {
                return null;
            }
            if (!apiOperation.nickname().isEmpty()) {
                operationId = apiOperation.nickname();
            }

            defaultResponseHeaders = parseResponseHeaders(apiOperation.responseHeaders());

            operation.summary(apiOperation.value()).description(apiOperation.notes());

            if (!isVoid(apiOperation.response())) {
                responseType = apiOperation.response();
            }
            if (!apiOperation.responseContainer().isEmpty()) {
                responseContainer = apiOperation.responseContainer();
            }
            List<SecurityRequirement> securities = new ArrayList<SecurityRequirement>();
            for (Authorization auth : apiOperation.authorizations()) {
                if (!auth.value().isEmpty()) {
                    SecurityRequirement security = new SecurityRequirement();
                    security.setName(auth.value());
                    for (AuthorizationScope scope : auth.scopes()) {
                        if (!scope.scope().isEmpty()) {
                            security.addScope(scope.scope());
                        }
                    }
                    securities.add(security);
                }
            }
            for (SecurityRequirement sec : securities) {
                operation.security(sec);
            }
            if (!apiOperation.consumes().isEmpty()) {
                String[] consumesAr = ReaderUtils.splitContentValues(new String[]{apiOperation.consumes()});
                for (String consume : consumesAr) {
                    operation.consumes(consume);
                }
            }
            if (!apiOperation.produces().isEmpty()) {
                String[] producesAr = ReaderUtils.splitContentValues(new String[]{apiOperation.produces()});
                for (String produce : producesAr) {
                    operation.produces(produce);
                }
            }
        }

        if (apiOperation != null && StringUtils.isNotEmpty(apiOperation.responseReference())) {
            Response response = new Response().description(SUCCESSFUL_OPERATION);
            response.schema(new RefProperty(apiOperation.responseReference()));
            operation.addResponse(String.valueOf(apiOperation.code()), response);
        } else if (responseType == null) {
            // pick out response from method declaration
            LOGGER.debug("picking up response class from method {}", method);
            responseType = method.getGenericReturnType();
        }
        if (isValidResponse(responseType)) {
            final Property property = ModelConverters.getInstance().readAsProperty(responseType);
            if (property != null) {
                final Property responseProperty = ContainerWrapper.wrapContainer(responseContainer, property);
                final int responseCode = (apiOperation == null) ? 200 : apiOperation.code();
                operation.response(responseCode, new Response().description(SUCCESSFUL_OPERATION).schema(responseProperty)
                        .headers(defaultResponseHeaders));
                appendModels(responseType);
            }
        }

        operation.operationId(operationId);

        if (operation.getConsumes() == null || operation.getConsumes().isEmpty()) {
            final Consumes consumes = ReflectionUtils.getAnnotation(method, Consumes.class);
            if (consumes != null) {
                for (String mediaType : ReaderUtils.splitContentValues(consumes.value())) {
                    operation.consumes(mediaType);
                }
            }
        }

        if (operation.getProduces() == null || operation.getProduces().isEmpty()) {
            final Produces produces = ReflectionUtils.getAnnotation(method, Produces.class);
            if (produces != null) {
                for (String mediaType : ReaderUtils.splitContentValues(produces.value())) {
                    operation.produces(mediaType);
                }
            }
        }

        List<ApiResponse> apiResponses = new ArrayList<ApiResponse>();
        if (responseAnnotation != null) {
            apiResponses.addAll(Arrays.asList(responseAnnotation.value()));
        }

        Class<?>[] exceptionTypes = method.getExceptionTypes();
        for (Class<?> exceptionType : exceptionTypes) {
            ApiResponses exceptionResponses = ReflectionUtils.getAnnotation(exceptionType, ApiResponses.class);
            if (exceptionResponses != null) {
                apiResponses.addAll(Arrays.asList(exceptionResponses.value()));
            }
        }

        for (ApiResponse apiResponse : apiResponses) {
            addResponse(operation, apiResponse);
        }
        // merge class level @ApiResponse
        for (ApiResponse apiResponse : classApiResponses) {
            String key = (apiResponse.code() == 0) ? "default" : String.valueOf(apiResponse.code());
            if (operation.getResponses() != null && operation.getResponses().containsKey(key)) {
                continue;
            }
            addResponse(operation, apiResponse);
        }

        if (ReflectionUtils.getAnnotation(method, Deprecated.class) != null) {
            operation.setDeprecated(true);
        }

        // process parameters
        for (Parameter globalParameter : globalParameters) {
            operation.parameter(globalParameter);
        }

        Annotation[][] paramAnnotations = ReflectionUtils.getParameterAnnotations(method);
        if (annotatedMethod == null) {
            Type[] genericParameterTypes = method.getGenericParameterTypes();
            for (int i = 0; i < genericParameterTypes.length; i++) {
                final Type type = TypeFactory.defaultInstance().constructType(genericParameterTypes[i], cls);
                List<Parameter> parameters = getParameters(type, Arrays.asList(paramAnnotations[i]));

                for (Parameter parameter : parameters) {
                    operation.parameter(parameter);
                }
            }
        } else {
            for (int i = 0; i < annotatedMethod.getParameterCount(); i++) {
                AnnotatedParameter param = annotatedMethod.getParameter(i);
                final Type type = TypeFactory.defaultInstance().constructType(param.getParameterType(), cls);
                List<Parameter> parameters = getParameters(type, Arrays.asList(paramAnnotations[i]));

                for (Parameter parameter : parameters) {
                    operation.parameter(parameter);
                }
            }
        }

        if (operation.getResponses() == null) {
            Response response = new Response().description(SUCCESSFUL_OPERATION);
            operation.defaultResponse(response);
        }

        processOperationDecorator(operation, method);

        return operation;
    }

    private void processOperationDecorator(Operation operation, Method method) {
        final Iterator<SwaggerExtension> chain = SwaggerExtensions.chain();
        if (chain.hasNext()) {
            SwaggerExtension extension = chain.next();
            LOGGER.debug("trying to decorate operation: {}", extension);
            extension.decorateOperation(operation, method, chain);
        }
    }

    private void addResponse(Operation operation, ApiResponse apiResponse) {
        Map<String, Property> responseHeaders = parseResponseHeaders(apiResponse.responseHeaders());

        Response response = new Response()
        .description(apiResponse.message()).headers(responseHeaders);

        if (apiResponse.code() == 0) {
            operation.defaultResponse(response);
        } else {
            operation.response(apiResponse.code(), response);
        }

        if (StringUtils.isNotEmpty(apiResponse.reference())) {
            response.schema(new RefProperty(apiResponse.reference()));
        } else if (!isVoid(apiResponse.response())) {
            Type responseType = apiResponse.response();
            final Property property = ModelConverters.getInstance().readAsProperty(responseType);
            if (property != null) {
                response.schema(ContainerWrapper.wrapContainer(apiResponse.responseContainer(), property));
                appendModels(responseType);
            }
        }
    }

    private List<Parameter> getParameters(Type type, List<Annotation> annotations) {
        final Iterator<SwaggerExtension> chain = SwaggerExtensions.chain();
        if (!chain.hasNext()) {
            return Collections.emptyList();
        }
        LOGGER.debug("getParameters for {}", type);
        Set<Type> typesToSkip = new HashSet<Type>();
        final SwaggerExtension extension = chain.next();
        LOGGER.debug("trying extension {}", extension);

        final List<Parameter> parameters = extension.extractParameters(annotations, type, typesToSkip, chain);
        if (!parameters.isEmpty()) {
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
        if (apiOperation != null && !"".equals(apiOperation.httpMethod())) {
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
            HttpMethod httpMethod = method.getAnnotation(HttpMethod.class);
            return httpMethod.value().toLowerCase();
        } else if (!StringUtils.isEmpty(getHttpMethodFromCustomAnnotations(method))) {
            return getHttpMethodFromCustomAnnotations(method);
        } else if ((ReflectionUtils.getOverriddenMethod(method)) != null) {
            return extractOperationMethod(apiOperation, ReflectionUtils.getOverriddenMethod(method), chain);
        } else if (chain != null && chain.hasNext()) {
            return chain.next().extractOperationMethod(apiOperation, method, chain);
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
            final Set<ContainerWrapper> tmp = (allowed.length > 0) ? EnumSet.copyOf(Arrays.asList(allowed))
                    : EnumSet.allOf(ContainerWrapper.class);
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
        if (swagger == null) {
            return false;
        }
        if (swagger.getPaths() == null || swagger.getPaths().isEmpty()) {
            return false;
        }
        for (Path path : swagger.getPaths().values()) {
            for (Operation op : path.getOperations()) {
                if (operationId.equalsIgnoreCase(op.getOperationId())) {
                    return true;
                }
            }
        }
        return false;
    }
}