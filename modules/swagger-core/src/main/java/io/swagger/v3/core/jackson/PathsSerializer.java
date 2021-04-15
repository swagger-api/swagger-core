package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;

import java.io.IOException;
import java.util.Map.Entry;

public class PathsSerializer extends JsonSerializer<Paths> {

    @Override
    public void serialize(
            Paths value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        if (value != null && value.getExtensions() != null && !value.getExtensions().isEmpty()) {
            jgen.writeStartObject();

            if (!value.isEmpty()) {
                for (Entry<String, PathItem> entry: value.entrySet()) {
                    jgen.writeObjectField(entry.getKey() , entry.getValue());
                }
            }
            for (Entry<String, Object> entry: value.getExtensions().entrySet()) {
                jgen.writeObjectField(entry.getKey() , entry.getValue());
            }
            jgen.writeEndObject();
        } else {
            provider.defaultSerializeValue(value, jgen);
        }
    }
}
