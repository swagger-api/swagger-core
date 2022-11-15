package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import io.swagger.v3.oas.models.examples.Example;

import java.io.IOException;

public class ExampleSerializer extends JsonSerializer<Example> implements ResolvableSerializer {

    private JsonSerializer<Object> defaultSerializer;

    public ExampleSerializer(JsonSerializer<Object> serializer) {
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
            Example example, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

            if (example.getValueSetFlag() && example.getValue() == null) {
                jgen.writeStartObject();
                defaultSerializer.unwrappingSerializer(null).serialize(example, jgen, provider);
                jgen.writeNullField("value");
                jgen.writeEndObject();
            } else {
                defaultSerializer.serialize(example, jgen, provider);
            }
    }
}
