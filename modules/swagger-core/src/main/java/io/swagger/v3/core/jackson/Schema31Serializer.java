package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import io.swagger.v3.oas.models.media.Schema;

import java.io.IOException;

public class Schema31Serializer extends JsonSerializer<Schema> implements ResolvableSerializer {

    private JsonSerializer<Object> defaultSerializer;

    public Schema31Serializer(JsonSerializer<Object> serializer) {
        defaultSerializer = serializer;
    }

    @Override
    public void resolve(SerializerProvider serializerProvider) throws JsonMappingException {
        if (defaultSerializer instanceof ResolvableSerializer) {
            ((ResolvableSerializer) defaultSerializer).resolve(serializerProvider);
        }
    }

    @Override
    public void serialize(
            Schema value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        if (value.getBooleanSchemaValue() != null) {
            jgen.writeBoolean(value.getBooleanSchemaValue());
            return;
        }
        
        boolean hasNullExample = value.getExampleSetFlag() && value.getExample() == null;
        boolean hasNullDefault = value.getDefaultSetFlag() && value.getDefault() == null;
        
        if (hasNullExample || hasNullDefault) {
            jgen.writeStartObject();
            defaultSerializer.unwrappingSerializer(null).serialize(value, jgen, provider);
            if (hasNullExample) {
                jgen.writeNullField("example");
            }
            if (hasNullDefault) {
                jgen.writeNullField("default");
            }
            jgen.writeEndObject();
        } else {
            defaultSerializer.serialize(value, jgen, provider);
        }
    }
}
