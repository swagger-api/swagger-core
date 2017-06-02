package io.swagger.jaxrs2.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import io.swagger.jaxrs2.config.ReaderConfig;
import io.swagger.jaxrs2.ext.OpenAPIExtension;
import io.swagger.oas.models.Operation;
import io.swagger.util.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.DELETE;
import javax.ws.rs.HttpMethod;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class ReaderUtils {
    private static final String GET_METHOD = "get";
    private static final String POST_METHOD = "post";
    private static final String PUT_METHOD = "put";
    private static final String DELETE_METHOD = "delete";
    private static final String PATCH_METHOD = "patch";
    private static final String TRACE_METHOD = "trace";
    private static final String HEAD_METHOD = "head";
    private static final String OPTIONS_METHOD = "options";
    private static final String PATH_DELIMITER = "/";

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
        for (String value : array) {
            list.add(value);
        }
        return Optional.of(list);
    }

    public static boolean isIgnored(String path, ReaderConfig config) {
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
