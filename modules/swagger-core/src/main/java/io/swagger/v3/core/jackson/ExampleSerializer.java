package io.swagger.v3.core.jackson;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import io.swagger.v3.oas.models.examples.Example;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class ExampleSerializer extends ValueSerializer<Example> {

    private ValueSerializer<Object> defaultSerializer;

    public ExampleSerializer(ValueSerializer<Object> serializer) {
        defaultSerializer = serializer;
    }

    @Override
    public void resolve(SerializationContext serializerProvider) throws DatabindException {
        defaultSerializer.resolve(serializerProvider);
    }

    @Override
    public void serialize(
            Example example, JsonGenerator jgen, SerializationContext provider)
            throws JacksonException {

            if (example.getValueSetFlag() && example.getValue() == null) {
                jgen.writeStartObject();
                defaultSerializer.unwrappingSerializer(null).serialize(example, jgen, provider);
                jgen.writeNullProperty("value");
                jgen.writeEndObject();
            } else {
                defaultSerializer.serialize(example, jgen, provider);
            }
    }
}
