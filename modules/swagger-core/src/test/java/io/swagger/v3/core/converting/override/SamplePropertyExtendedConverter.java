package io.swagger.v3.core.converting.override;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converting.override.resources.MyCustomClass;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Iterator;

/**
 * Sample converter implementation which turns "MyCustomClass" into a DateTime property
 */
public class SamplePropertyExtendedConverter extends ModelResolver {

    public SamplePropertyExtendedConverter(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (type.isSchemaProperty()) {
            JavaType _type = Json.mapper().constructType(type.getType());
            if (_type != null) {
                Class<?> cls = _type.getRawClass();
                if (MyCustomClass.class.isAssignableFrom(cls)) {
                    Schema schema = new DateTimeSchema();
                    super.resolveSchemaMembers(schema, type);
                    return schema;
                }
            }
        }
        if (chain.hasNext()) {
            return chain.next().resolve(type, context, chain);
        } else {
            return null;
        }
    }
}