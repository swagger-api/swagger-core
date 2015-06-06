package io.swagger.model.override;

import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.jackson.AbstractModelConverter;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.Property;
import io.swagger.util.Json;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map.Entry;

public class GericModelConverter extends AbstractModelConverter {

    protected GericModelConverter() {
        super(Json.mapper());
    }

    @Override
    public Property resolveProperty(Type type, ModelConverterContext context, Annotation[] annotations, Iterator<ModelConverter> chain) {
        return chain.next().resolveProperty(type, context, annotations, chain);
    }

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> next) {
        if (type instanceof Class<?>) {
            Class<?> cls = (Class<?>) type;
            if (GenericModel.class.isAssignableFrom(cls)) {
                ModelImpl impl = new ModelImpl();
                impl.setName(cls.getSimpleName());
                for (Entry<String, Class<?>> entry : GenericModel.getDeclaredProperties().entrySet()) {
                    impl.addProperty(entry.getKey(), context.resolveProperty(entry.getValue(), null));
                }
                context.defineModel(impl.getName(), impl);
                return impl;
            }
        }
        return null;
    }
}