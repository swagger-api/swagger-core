package io.swagger.v3.jaxrs2.util;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.util.Configuration;
import io.swagger.v3.core.util.ParameterProcessor;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtensions;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Context;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ReaderUtils {
    private static final String GET_METHOD = "get";
    private static final String POST_METHOD = "post";
    private static final String PUT_METHOD = "put";
    private static final String DELETE_METHOD = "delete";
    private static final String HEAD_METHOD = "head";
    private static final String OPTIONS_METHOD = "options";
    private static final String PATH_DELIMITER = "/";

    public static List<Parameter> collectConstructorParameters(Class<?> cls, Components components, javax.ws.rs.Consumes classConsumes, JsonView jsonViewAnnotation) {
        return collectConstructorParameters(cls, components, classConsumes, jsonViewAnnotation, null);
    }

    public static List<Parameter> collectConstructorParameters(Class<?> cls, Components components, javax.ws.rs.Consumes classConsumes, JsonView jsonViewAnnotation, Schema.SchemaResolution schemaResolution) {
        return collectConstructorParameters(cls, components, classConsumes, jsonViewAnnotation, schemaResolution, false);
    }
    /**
     * Collects constructor-level parameters from class.
     *
     * @param cls        is a class for collecting
     * @param components
     * @return the collection of supported parameters
     */
    public static List<Parameter> collectConstructorParameters(Class<?> cls, Components components, javax.ws.rs.Consumes classConsumes, JsonView jsonViewAnnotation, Schema.SchemaResolution schemaResolution, boolean openapi31) {
        return collectConstructorParameters(cls, components, classConsumes, jsonViewAnnotation, schemaResolution, openapi31, null);
    }

    public static List<Parameter> collectConstructorParameters(Class<?> cls, Components components, javax.ws.rs.Consumes classConsumes, JsonView jsonViewAnnotation, Schema.SchemaResolution schemaResolution, boolean openapi31, Configuration configuration) {
        if (cls.isLocalClass() || (cls.isMemberClass() && !Modifier.isStatic(cls.getModifiers()))) {
            return Collections.emptyList();
        }
        final Configuration effectiveConfiguration;
        if (configuration != null) {
            effectiveConfiguration = configuration;
        } else {
            effectiveConfiguration = new Configuration();
            effectiveConfiguration.setOpenAPI31(openapi31);
            effectiveConfiguration.setSchemaResolution(schemaResolution);
        }

        List<Parameter> selected = Collections.emptyList();
        int maxParamsCount = 0;

        for (Constructor<?> constructor : cls.getDeclaredConstructors()) {
            if (!ReflectionUtils.isConstructorCompatible(constructor)
                    && !ReflectionUtils.isInject(Arrays.asList(constructor.getDeclaredAnnotations()))) {
                continue;
            }

            final Type[] genericParameterTypes = constructor.getGenericParameterTypes();
            final Annotation[][] annotations = constructor.getParameterAnnotations();

            int paramsCount = 0;
            final List<Parameter> parameters = new ArrayList<>();
            for (int i = 0; i < genericParameterTypes.length; i++) {
                final List<Annotation> tmpAnnotations = Arrays.asList(annotations[i]);
                if (isContext(tmpAnnotations)) {
                    paramsCount++;
                } else {
                    final Type genericParameterType = genericParameterTypes[i];
                    final List<Parameter> tmpParameters = collectParameters(genericParameterType, tmpAnnotations, components, classConsumes, jsonViewAnnotation, effectiveConfiguration);
                    if (! tmpParameters.isEmpty()) {
                        for (Parameter tmpParameter : tmpParameters) {
                            Parameter processedParameter = ParameterProcessor.applyAnnotations(
                                    tmpParameter,
                                    genericParameterType,
                                    tmpAnnotations,
                                    components,
                                    classConsumes == null ? new String[0] : classConsumes.value(),
                                    null,
                                    jsonViewAnnotation,
                                    effectiveConfiguration);
                            if (processedParameter != null) {
                                parameters.add(processedParameter);
                            }
                        }
                        paramsCount++;
                    }
                }
            }

            if (paramsCount >= maxParamsCount) {
                maxParamsCount = paramsCount;
                selected = parameters;
            }
        }

        return selected;
    }

    /**
     * Collects field-level parameters from class.
     *
     * @param cls        is a class for collecting
     * @param components
     * @return the collection of supported parameters
     */
    public static List<Parameter> collectFieldParameters(Class<?> cls, Components components, javax.ws.rs.Consumes classConsumes, JsonView jsonViewAnnotation) {
        return collectFieldParameters(cls, components, classConsumes, jsonViewAnnotation, null);
    }

    public static List<Parameter> collectFieldParameters(Class<?> cls, Components components, javax.ws.rs.Consumes classConsumes, JsonView jsonViewAnnotation, Configuration configuration) {
        final List<Parameter> parameters = new ArrayList<>();
        for (Field field : ReflectionUtils.getDeclaredFields(cls)) {
            final List<Annotation> annotations = Arrays.asList(field.getAnnotations());
            final Type genericType = field.getGenericType();
            parameters.addAll(collectParameters(genericType, annotations, components, classConsumes, jsonViewAnnotation, configuration));
        }
        return parameters;
    }

    private static List<Parameter> collectParameters(Type type, List<Annotation> annotations, Components components, javax.ws.rs.Consumes classConsumes, JsonView jsonViewAnnotation) {
        return collectParameters(type, annotations, components, classConsumes, jsonViewAnnotation, null);
    }

    private static List<Parameter> collectParameters(Type type, List<Annotation> annotations, Components components, javax.ws.rs.Consumes classConsumes, JsonView jsonViewAnnotation, Configuration configuration) {
        final Iterator<OpenAPIExtension> chain = OpenAPIExtensions.chain();
        if (!chain.hasNext()) {
            return Collections.emptyList();
        }
        OpenAPIExtension extension = chain.next();
        // the extension keeps its Configuration across calls; align it with the current
        // scan so version-gated annotation values (e.g. in: querystring) see the right version
        if (configuration != null) {
            extension.setConfiguration(configuration);
        }
        return extension.extractParameters(annotations, type, new HashSet<>(), components, classConsumes, null, false, jsonViewAnnotation, chain).parameters;
    }

    private static boolean isContext(List<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Context) {
                return true;
            }
        }
        return false;
    }

    public static Optional<List<String>> getStringListFromStringArray(String[] array) {
        if (array == null) {
            return Optional.empty();
        }
        List<String> list = new ArrayList<>();
        boolean isEmpty = true;
        for (String value : array) {
            if (StringUtils.isNotBlank(value)) {
                isEmpty = false;
            }
            list.add(value);
        }
        if (isEmpty) {
            return Optional.empty();
        }
        return Optional.of(list);
    }

    public static boolean isIgnored(String path, OpenAPIConfiguration config) {
        if (config.getIgnoredRoutes() == null) {
            return false;
        }
        for (String item : config.getIgnoredRoutes()) {
            final int length = item.length();
            if (path.startsWith(item) && (path.length() == length || path.startsWith(PATH_DELIMITER, length))) {
                return true;
            }
        }
        return false;
    }

    public static String getPath(javax.ws.rs.Path classLevelPath, javax.ws.rs.Path methodLevelPath, String parentPath, boolean isSubresource) {
        if (classLevelPath == null && methodLevelPath == null && StringUtils.isEmpty(parentPath)) {
            return null;
        }
        StringBuilder b = new StringBuilder();
        appendPathComponent(parentPath, b);
        if (classLevelPath != null && !isSubresource) {
            appendPathComponent(classLevelPath.value(), b);
        }
        if (methodLevelPath != null) {
            appendPathComponent(methodLevelPath.value(), b);
        }
        return b.length() == 0 ? "/" : b.toString();
    }

    /**
     * appends a path component string to a StringBuilder
     * guarantees:
     * <ul>
     *     <li>nulls, empty strings and "/" are nops</li>
     *     <li>output will always start with "/" and never end with "/"</li>
     * </ul>
     * @param component component to be added
     * @param to output
     */
    private static void appendPathComponent(String component, StringBuilder to) {
        if (component == null || component.isEmpty() || "/".equals(component)) {
            return;
        }
        if (!component.startsWith("/") && (to.length() == 0 || '/' != to.charAt(to.length() - 1))) {
            to.append("/");
        }
        if (component.endsWith("/")) {
            to.append(component, 0, component.length() - 1);
        } else {
            to.append(component);
        }
    }

    public static String extractOperationMethod(Method method, Iterator<OpenAPIExtension> chain) {
        if (method.getAnnotation(javax.ws.rs.GET.class) != null) {
            return GET_METHOD;
        } else if (method.getAnnotation(javax.ws.rs.PUT.class) != null) {
            return PUT_METHOD;
        } else if (method.getAnnotation(javax.ws.rs.POST.class) != null) {
            return POST_METHOD;
        } else if (method.getAnnotation(javax.ws.rs.DELETE.class) != null) {
            return DELETE_METHOD;
        } else if (method.getAnnotation(javax.ws.rs.OPTIONS.class) != null) {
            return OPTIONS_METHOD;
        } else if (method.getAnnotation(javax.ws.rs.HEAD.class) != null) {
            return HEAD_METHOD;
        } else if (method.getAnnotation(HttpMethod.class) != null) {
            HttpMethod httpMethod = method.getAnnotation(HttpMethod.class);
            // keep the declared case: fixed methods are matched case-insensitively
            // downstream and custom methods land in additionalOperations, whose
            // keys preserve original case (OpenAPI 3.2)
            return httpMethod.value();
        } else if (!StringUtils.isEmpty(getHttpMethodFromCustomAnnotations(method))) {
            return getHttpMethodFromCustomAnnotations(method);
        } else if ((ReflectionUtils.getOverriddenMethod(method)) != null) {
            return extractOperationMethod(ReflectionUtils.getOverriddenMethod(method), chain);
        } else if (chain != null && chain.hasNext()) {
            return chain.next().extractOperationMethod(method, chain);
        } else {
            return null;
        }
    }

    public static String getHttpMethodFromCustomAnnotations(Method method) {
        for (Annotation methodAnnotation : method.getAnnotations()) {
            HttpMethod httpMethod = methodAnnotation.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                return httpMethod.value();
            }
        }
        return null;
    }
}
