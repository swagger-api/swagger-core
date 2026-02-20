package io.swagger.v3.core.converter;

import io.swagger.v3.oas.models.media.Schema;

import java.util.Iterator;

public interface ModelConverter {

    /**
     * @param type
     * @param context
     * @param chain   the chain of model converters to try if this implementation cannot process
     * @return null if this ModelConverter cannot convert the given Type
     */
    Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain);

    default boolean isOpenapi31() {
        return false;
    }
}
