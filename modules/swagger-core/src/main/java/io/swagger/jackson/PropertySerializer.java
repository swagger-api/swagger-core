package io.swagger.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import io.swagger.models.properties.Property;

import java.io.IOException;

public class PropertySerializer extends JsonSerializer<Property> implements ResolvableSerializer {

    private JsonSerializer<Object> defaultSerializer;

    public PropertySerializer(JsonSerializer<Object> serializer) {
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
            Property value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        if (value.getBooleanValue() != null) {
            jgen.writeBoolean(value.getBooleanValue());
        } else {
            defaultSerializer.serialize(value, jgen, provider);
        }
    }
}
