package io.swagger.model.override;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.oas.models.media.DateTimeSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.util.Json;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;

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
}