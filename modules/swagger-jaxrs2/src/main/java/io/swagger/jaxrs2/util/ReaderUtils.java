package io.swagger.jaxrs2.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import io.swagger.jaxrs2.ext.OpenAPIExtension;
import io.swagger.jaxrs2.ext.OpenAPIExtensions;
import io.swagger.oas.integration.OpenAPIConfiguration;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.util.ParameterProcessor;
import io.swagger.util.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.DELETE;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Context;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

public class ReaderUtils {
    private static final String GET_METHOD = "get";
    private static final String POST_METHOD = "post";
    private static final String PUT_METHOD = "put";
    private static final String DELETE_METHOD = "delete";
    private static final String HEAD_METHOD = "head";
    private static final String OPTIONS_METHOD = "options";
    private static final String PATH_DELIMITER = "/";

    /**
     * Collects constructor-level parameters from class.
     *
     * @param cls     is a class for collecting
     * @param openAPI is the instance of the OpenAPI
     * @return the collection of supported parameters
     */
    public static List<Parameter> collectConstructorParameters(Class<?> cls, OpenAPI openAPI) {
        if (cls.isLocalClass() || (cls.isMemberClass() && !Modifier.isStatic(cls.getModifiers()))) {
            return Collections.emptyList();
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
            final List<Parameter> parameters = new ArrayList<Parameter>();
            for (int i = 0; i < genericParameterTypes.length; i++) {
                final List<Annotation> tmpAnnotations = Arrays.asList(annotations[i]);
                if (isContext(tmpAnnotations)) {
                    paramsCount++;
                } else {
                    final Type genericParameterType = genericParameterTypes[i];
                    final List<Parameter> tmpParameters = collectParameters(genericParameterType, tmpAnnotations);
                    if (tmpParameters.size() >= 1) {
                        for (Parameter tmpParameter : tmpParameters) {
                            if (ParameterProcessor.applyAnnotations(openAPI, tmpParameter, genericParameterType, tmpAnnotations) != null) {
                                parameters.add(tmpParameter);
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
     * @param cls     is a class for collecting
     * @param openAPI is the instance of the Swagger
     * @return the collection of supported parameters
     */
    public static List<Parameter> collectFieldParameters(Class<?> cls, OpenAPI openAPI) {
        final List<Parameter> parameters = new ArrayList<Parameter>();
        for (Field field : ReflectionUtils.getDeclaredFields(cls)) {
            final List<Annotation> annotations = Arrays.asList(field.getAnnotations());
            final Type genericType = field.getGenericType();
            for (Parameter parameter : collectParameters(genericType, annotations)) {
                if (ParameterProcessor.applyAnnotations(openAPI, parameter, genericType, annotations) != null) {
                    parameters.add(parameter);
                }
            }
        }
        return parameters;
    }

    private static List<Parameter> collectParameters(Type type, List<Annotation> annotations) {
        final Iterator<OpenAPIExtension> chain = OpenAPIExtensions.chain();
        return chain.hasNext() ? chain.next().extractParameters(annotations, type, new HashSet<>(), chain) :
                Collections.emptyList();
    }

    private static boolean isContext(List<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Context) {
                return true;
            }
        }
        return false;
    }

    /**
     * Splits the provided array of strings into an array, using comma as the separator.
     * Also removes leading and trailing whitespace and omits empty strings from the results.
     *
     * @param strings is the provided array of strings
     * @return the resulted array of strings
     */
    public static String[] splitContentValues(String[] strings) {
        final Set<String> result = new LinkedHashSet<String>();

        for (String string : strings) {
            Iterables.addAll(result, Splitter.on(",").trimResults().omitEmptyStrings().split(string));
        }

        return result.toArray(new String[result.size()]);
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

    public static String getPath(javax.ws.rs.Path classLevelPath, javax.ws.rs.Path methodLevelPath, String parentPath) {
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

    public static String extractOperationMethod(Operation operation, Method method, Iterator<OpenAPIExtension> chain) {
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
        } else if (method.getAnnotation(DELETE.class) != null) {
            return DELETE_METHOD;
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

    public static String getHttpMethodFromCustomAnnotations(Method method) {
        for (Annotation methodAnnotation : method.getAnnotations()) {
            HttpMethod httpMethod = methodAnnotation.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                return httpMethod.value().toLowerCase();
            }
        }
        return null;
    }
}
