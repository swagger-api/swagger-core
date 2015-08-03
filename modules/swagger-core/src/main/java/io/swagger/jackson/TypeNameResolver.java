package io.swagger.jackson;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.annotations.ApiModel;
import io.swagger.util.PrimitiveType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Helper class used for converting well-known (property) types into
 * Swagger type names.
 */
public class TypeNameResolver {
    public final static TypeNameResolver std = new TypeNameResolver();

    protected TypeNameResolver() {
    }

    public String nameForType(JavaType type) {
        if (type.hasGenericTypes()) {
            return nameForGenericType(type);
        }
        final String name = findStdName(type);
        return (name == null) ? nameForClass(type) : name;
    }

    protected String nameForClass(JavaType type) {
        return nameForClass(type.getRawClass());
    }

    protected String nameForClass(Class<?> cls) {
        final ApiModel model = cls.getAnnotation(ApiModel.class);
        final String modelName = model == null ? null : StringUtils.trimToNull(model.value());
        return modelName == null ? cls.getSimpleName() : modelName;
    }

    protected String nameForGenericType(JavaType type) {
        final StringBuilder generic = new StringBuilder(nameForClass(type));
        final int count = type.containedTypeCount();
        for (int i = 0; i < count; ++i) {
            final JavaType arg = type.containedType(i);
            final String argName = PrimitiveType.fromType(arg) != null ? nameForClass(arg) : nameForType(arg);
            generic.append(WordUtils.capitalize(argName));
        }
        return generic.toString();
    }

    protected String findStdName(JavaType type) {
        return PrimitiveType.getCommonName(type);
    }
}
