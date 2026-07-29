package io.swagger.v3.core.util;

import java.lang.annotation.Annotation;

public class KotlinDetector {
    private static final Class<? extends Annotation> kotlinMetadata;
    private static final Class<? extends Annotation> kotlinDeprecated;

    static {
        kotlinMetadata = loadByClassOrNull("kotlin.Metadata");
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
        return kotlinMetadata != null;
    }

    public static Class<? extends Annotation> getKotlinDeprecated() {
        return kotlinDeprecated;
    }

    public static boolean isKotlinClass(Class<?> cls) {
        if (cls == null) {
            return false;
        }
        return kotlinMetadata != null && cls.getAnnotation(kotlinMetadata) != null;
    }
}
