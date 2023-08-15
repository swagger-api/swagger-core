package io.swagger.v3.core.util;

import java.lang.annotation.Annotation;

public class KotlinDetector {
    private static final Boolean kotlinAvailable;
    private static final Class<? extends Annotation> kotlinDeprecated;

    static {
        kotlinAvailable = loadByClassOrNull("kotlin.Metadata") != null;
        kotlinDeprecated = loadByClassOrNull("kotlin.Deprecated");
    }

    private static <T> Class<T> loadByClassOrNull(String className) {
        try {
            return (Class<T>) ReflectionUtils.loadClassByName(className);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    public static boolean isKotlinPresent() {
        return kotlinAvailable;
    }

    public static Class<? extends Annotation> getKotlinDeprecated() {
        return kotlinDeprecated;
    }
}
