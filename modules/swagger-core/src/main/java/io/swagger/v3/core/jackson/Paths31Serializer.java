package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map.Entry;

public class Paths31Serializer extends JsonSerializer<Paths> {

    @Override
    public void serialize(Paths value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value == null || (value.isEmpty() && (value.getExtensions() == null || value.getExtensions().isEmpty()))) {
            provider.defaultSerializeValue(value, jgen);
            return;
        }

        jgen.writeStartObject();
        if (!value.isEmpty()) {
            for (Entry<String, PathItem> entry: value.entrySet()) {
                if (StringUtils.isBlank(entry.getValue().get$ref())) {
                    jgen.writeObjectField(entry.getKey(), entry.getValue());
                } else {
                    PathItem pathItem = entry.getValue();

                    jgen.writeObjectFieldStart(entry.getKey());
                    jgen.writeStringField("$ref", pathItem.get$ref());
                    if (StringUtils.isNotBlank(pathItem.getDescription())) {
                        jgen.writeStringField("description", pathItem.getDescription());
                    }
                    if (StringUtils.isNotBlank(pathItem.getSummary())) {
                        jgen.writeStringField("summary", pathItem.getSummary());
                    }
                    jgen.writeEndObject();
                }
            }
        }
        if (value.getExtensions() != null && !value.getExtensions().isEmpty()) {
            for (Entry<String, Object> entry: value.getExtensions().entrySet()) {
                jgen.writeObjectField(entry.getKey() , entry.getValue());
            }
        }
        jgen.writeEndObject();
    }
}
