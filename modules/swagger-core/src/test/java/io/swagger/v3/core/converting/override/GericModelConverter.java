package io.swagger.v3.core.converting.override;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converting.override.resources.GenericModel;
import io.swagger.v3.core.jackson.AbstractModelConverter;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map.Entry;

public class GericModelConverter extends AbstractModelConverter {

    protected GericModelConverter() {
        super(Json.mapper());
    }

    @Override
    public Schema resolve(Type type, ModelConverterContext context, Annotation[] annotations, Iterator<ModelConverter> chain) {
        return chain.next().resolve(type, context, annotations, chain);
    }

    @Override
    public Schema resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> next) {
        if (type instanceof Class<?>) {
            Class<?> cls = (Class<?>) type;
            if (GenericModel.class.isAssignableFrom(cls)) {
                Schema impl = new Schema();
                impl.title(cls.getSimpleName());
                for (Entry<String, Class<?>> entry : GenericModel.getDeclaredProperties().entrySet()) {
                    impl.addProperties(entry.getKey(), context.resolve(entry.getValue(), null));
                }
                context.defineModel(impl.getTitle(), impl);
                return impl;
            }
        }
        return null;
    }
}