package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class SchemaSerializer extends JsonSerializer<Schema> implements ResolvableSerializer {

    private JsonSerializer<Object> defaultSerializer;

    public SchemaSerializer(JsonSerializer<Object> serializer) {
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

        if (StringUtils.isBlank(value.get$ref())) {

            if (value.getExampleSetFlag() && value.getExample() == null) {
                jgen.writeStartObject();
                defaultSerializer.unwrappingSerializer(null).serialize(value, jgen, provider);
                jgen.writeNullField("example");
                jgen.writeEndObject();
            } else {
                defaultSerializer.serialize(value, jgen, provider);
            }

        } else {
            // handle ref schema serialization skipping all other props
            jgen.writeStartObject();
            jgen.writeStringField("$ref", value.get$ref());
            jgen.writeEndObject();
        }
    }
}
