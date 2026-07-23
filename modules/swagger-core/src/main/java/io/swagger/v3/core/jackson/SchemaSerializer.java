package io.swagger.v3.core.jackson;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class SchemaSerializer extends ValueSerializer<Schema> {

    private ValueSerializer<Object> defaultSerializer;

    public SchemaSerializer(ValueSerializer<Object> serializer) {
        defaultSerializer = serializer;
    }

    @Override
    public void resolve(SerializationContext serializerProvider) throws DatabindException {
        defaultSerializer.resolve(serializerProvider);
    }

    @Override
    public void serialize(
            Schema value, JsonGenerator jgen, SerializationContext provider)
            throws JacksonException {

        if (StringUtils.isBlank(value.get$ref())) {

            boolean hasNullExample = value.getExampleSetFlag() && value.getExample() == null;
            boolean hasNullDefault = value.getDefaultSetFlag() && value.getDefault() == null;

            if (hasNullExample || hasNullDefault) {
                jgen.writeStartObject();
                defaultSerializer.unwrappingSerializer(null).serialize(value, jgen, provider);
                if (hasNullExample) {
                    jgen.writeNullProperty("example");
                }
                if (hasNullDefault) {
                    jgen.writeNullProperty("default");
                }
                jgen.writeEndObject();
            } else {
                defaultSerializer.serialize(value, jgen, provider);
            }

        } else {
            // handle ref schema serialization skipping all other props
            jgen.writeStartObject();
            jgen.writeStringProperty("$ref", value.get$ref());
            jgen.writeEndObject();
        }
    }
}
