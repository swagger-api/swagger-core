package io.swagger.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import io.swagger.models.Model;

import java.io.IOException;

public class ModelSerializer extends JsonSerializer<Model> implements ResolvableSerializer {

    private JsonSerializer<Object> defaultSerializer;

    public ModelSerializer(JsonSerializer<Object> serializer) {
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
            Model value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        if (value.getBooleanValue() != null) {
            jgen.writeBoolean(value.getBooleanValue());
        } else {
            defaultSerializer.serialize(value, jgen, provider);
        }
    }
}
