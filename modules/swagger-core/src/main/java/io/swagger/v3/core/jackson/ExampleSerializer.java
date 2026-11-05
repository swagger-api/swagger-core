package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.examples.Example;

import java.io.IOException;

public class ExampleSerializer extends JsonSerializer<Example> implements ResolvableSerializer {

    private JsonSerializer<Object> defaultSerializer;
    private final SpecVersion specVersion;

    public ExampleSerializer(JsonSerializer<Object> serializer) {
        this(serializer, SpecVersion.V30);
    }

    public ExampleSerializer(JsonSerializer<Object> serializer, SpecVersion specVersion) {
        defaultSerializer = serializer;
        this.specVersion = specVersion;
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
            } else if (specVersion == SpecVersion.V32 && example.getDataValueSetFlag() && example.getDataValue() == null) {
                jgen.writeStartObject();
                defaultSerializer.unwrappingSerializer(null).serialize(example, jgen, provider);
                jgen.writeNullField("dataValue");
                jgen.writeEndObject();
            } else {
                defaultSerializer.serialize(example, jgen, provider);
            }
    }
}
