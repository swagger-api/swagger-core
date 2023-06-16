package io.swagger.v3.core.converting.override;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.resolving.resources.BidimensionalArray;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Iterator;

public class CustomAnnotationConverter extends ModelResolver {

    public CustomAnnotationConverter(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        Schema s = chain.next().resolve(type, context, chain);
        if (s != null) {
            if ("array".equals(s.getType()) && s.getItems() != null && "array".equals(s.getItems().getType())) {
                BidimensionalArray.MySizeAnnotation size = AnnotationsUtils.getAnnotation(BidimensionalArray.MySizeAnnotation.class, type.getCtxAnnotations());
                if (size != null) {
                    s.getItems().maxItems(size.maxItems());
                }
            }
            return s;
        }
        return null;
    }
}
