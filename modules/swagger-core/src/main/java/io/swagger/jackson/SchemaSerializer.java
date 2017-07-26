package io.swagger.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;


public class SchemaSerializer extends JsonSerializer<Schema> {

    private JsonSerializer<Object> defaultSerializer;

    public SchemaSerializer(JsonSerializer<Object> serializer) {
        defaultSerializer = serializer;
    }

    @Override
    public void serialize(
            Schema value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        // handle ref schema serialization skipping all other props
        if (StringUtils.isBlank(value.get$ref())) {
            defaultSerializer.serialize(value, jgen, provider);
        } else {
            jgen.writeStartObject();
            jgen.writeStringField("$ref", value.get$ref());
            jgen.writeEndObject();
        }
    }
}