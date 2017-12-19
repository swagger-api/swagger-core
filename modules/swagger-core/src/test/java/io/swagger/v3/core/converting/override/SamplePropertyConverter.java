package io.swagger.v3.core.converting.override;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.Annotated;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converting.override.resources.MyCustomClass;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.Schema;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.function.BiFunction;

/**
 * Sample converter implementation which turns "MyCustomClass" into a DateTime property
 */
public class SamplePropertyConverter implements ModelConverter {
    @Override
    public Schema resolve(Type type, ModelConverterContext context, Annotation[] annotations, Iterator<ModelConverter> chain) {
        JavaType _type = Json.mapper().constructType(type);
        if (_type != null) {
            Class<?> cls = _type.getRawClass();
            if (MyCustomClass.class.isAssignableFrom(cls)) {
                return new DateTimeSchema();
            }
        }
        if (chain.hasNext()) {
            return chain.next().resolve(type, context, annotations, chain);
        } else {
            return null;
        }
    }

    @Override
    public Schema resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (chain.hasNext()) {
            return chain.next().resolve(type, context, chain);
        } else {
            return null;
        }
    }

    @Override
    public Schema resolveAnnotatedType(Type type, Annotated member, String elementName, Schema parent, BiFunction<JavaType, Annotation[], Schema> jsonUnwrappedHandler, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (chain.hasNext()) {
            return chain.next().resolveAnnotatedType(type, member, elementName, parent, jsonUnwrappedHandler, context, chain);
        } else {
            return null;
        }
    }
}