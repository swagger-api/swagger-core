package io.swagger.v3.core.util;

import tools.jackson.databind.JsonNode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Deep copy of OpenAPI models ({@code io.swagger.v3.oas.models}), for example to modify a copy of an
 * {@link io.swagger.v3.oas.models.OpenAPI} or a {@link io.swagger.v3.oas.models.media.Schema} without touching the
 * original.
 * <p>
 * Unlike a JSON round trip, the copy is exact: it keeps the concrete model classes (e.g.
 * {@code StringSchema}), the state that isn't serialized ({@code Schema.getName()}, the "example set" flag, the spec
 * version), the fields of both OpenAPI 3.0 and 3.1, and the type of example, default, enum and extension values.
 * <p>
 * Models (including subclasses defined outside {@code io.swagger.v3.oas.models}), lists, sets and maps are copied;
 * {@link JsonNode}s are deep-copied; other values (strings, numbers, dates, enums, user objects) are shared, as they
 * are treated as immutable. Shared references within the copied graph are preserved.
 *
 * @since 3.0.0
 */
public final class ModelCopy {

    private static final String MODELS_PACKAGE = "io.swagger.v3.oas.models.";

    private ModelCopy() {
    }

    /**
     * @param model the model to copy, may be {@code null}
     * @param <T>   the model type
     * @return a deep copy of the model
     * @throws IllegalArgumentException if a model class has no no-arg constructor
     */
    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(T model) {
        return (T) copy(model, new IdentityHashMap<>());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object copy(Object value, Map<Object, Object> copies) {
        if (value == null || value instanceof Enum) {
            return value;
        }
        Object copy = copies.get(value);
        if (copy != null) {
            return copy;
        }
        if (value instanceof JsonNode) {
            return ((JsonNode) value).deepCopy();
        }
        if (isModel(value.getClass())) {
            copy = newInstance(value.getClass());
            copies.put(value, copy);
            if (value instanceof Map) {
                ((Map) copy).clear();
                copyEntries((Map<?, ?>) value, (Map) copy, copies);
            }
            for (Class<?> type = value.getClass(); isModel(type); type = type.getSuperclass()) {
                copyFields(type, value, copy, copies);
            }
            return copy;
        }
        if (value instanceof Map) {
            copy = new LinkedHashMap<>();
            copies.put(value, copy);
            copyEntries((Map<?, ?>) value, (Map) copy, copies);
            return copy;
        }
        if (value instanceof List || value instanceof Set) {
            copy = value instanceof List ? new ArrayList<>() : new LinkedHashSet<>();
            copies.put(value, copy);
            for (Object element : (Collection<?>) value) {
                ((Collection) copy).add(copy(element, copies));
            }
            return copy;
        }
        return value;
    }

    private static boolean isModel(Class<?> type) {
        for (Class<?> c = type; c != null && c != Object.class; c = c.getSuperclass()) {
            if (c.getName().startsWith(MODELS_PACKAGE)) {
                return true;
            }
        }
        return false;
    }

    private static void copyEntries(Map<?, ?> source, Map<Object, Object> target, Map<Object, Object> copies) {
        for (Map.Entry<?, ?> entry : source.entrySet()) {
            target.put(entry.getKey(), copy(entry.getValue(), copies));
        }
    }

    private static void copyFields(Class<?> type, Object source, Object target, Map<Object, Object> copies) {
        for (Field field : type.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            try {
                field.setAccessible(true);
                field.set(target, copy(field.get(source), copies));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Cannot copy " + field, e);
            }
        }
    }

    private static Object newInstance(Class<?> type) {
        try {
            Constructor<?> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Cannot copy " + type.getName() + ": a no-arg constructor is required", e);
        }
    }
}
