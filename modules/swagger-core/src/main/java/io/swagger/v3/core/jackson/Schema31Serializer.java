package io.swagger.v3.core.jackson;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import io.swagger.v3.oas.models.media.Schema;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class Schema31Serializer extends ValueSerializer<Schema> {

    private ValueSerializer<Object> defaultSerializer;

    public Schema31Serializer(ValueSerializer<Object> serializer) {
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

        if (value.getBooleanSchemaValue() != null) {
            jgen.writeBoolean(value.getBooleanSchemaValue());
            return;
        }
        if (value.getExampleSetFlag() && value.getExample() == null) {
            jgen.writeStartObject();
            defaultSerializer.unwrappingSerializer(null).serialize(value, jgen, provider);
            jgen.writeNullProperty("example");
            jgen.writeEndObject();
        } else {
            defaultSerializer.serialize(value, jgen, provider);
        }
    }
}
