package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.callbacks.Callback;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map.Entry;

public class CallbackSerializer extends JsonSerializer<Callback> {

    protected boolean openapi31;

    @Override
    public void serialize(
            Callback value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        if (value == null) {
            provider.defaultSerializeValue(value, jgen);
            return;
        }
        if (StringUtils.isNotBlank(value.get$ref())) {
            jgen.writeStartObject();
            jgen.writeStringField("$ref", value.get$ref());
            if (openapi31) {
                if (StringUtils.isNotBlank(value.getDescription())) {
                    jgen.writeStringField("description", value.getDescription());
                }
                if (StringUtils.isNotBlank(value.getSummary())) {
                    jgen.writeStringField("summary", value.getSummary());
                }
            }
            jgen.writeEndObject();
            return;
        }
        if (!value.isEmpty()) {
            jgen.writeStartObject();
            for (Entry<String, PathItem> entry: value.entrySet()) {
                if (StringUtils.isBlank(entry.getValue().get$ref())) {
                    jgen.writeObjectField(entry.getKey(), entry.getValue());
                } else {
                    PathItem pathItem = entry.getValue();

                    jgen.writeObjectFieldStart(entry.getKey());
                    jgen.writeStringField("$ref", pathItem.get$ref());
                    if (openapi31) {
                        if (StringUtils.isNotBlank(pathItem.getDescription())) {
                            jgen.writeStringField("description", pathItem.getDescription());
                        }
                        if (StringUtils.isNotBlank(pathItem.getSummary())) {
                            jgen.writeStringField("summary", pathItem.getSummary());
                        }
                    }
                    jgen.writeEndObject();
                }
            }
            if (value.getExtensions() != null && !value.getExtensions().isEmpty()) {
                for (String ext : value.getExtensions().keySet()) {
                    jgen.writeObjectField(ext, value.getExtensions().get(ext));
                }
            }
            jgen.writeEndObject();
            return;
        }
        if (value.getExtensions() != null && !value.getExtensions().isEmpty()) {
            for (String ext : value.getExtensions().keySet()) {
                jgen.writeObjectField(ext, value.getExtensions().get(ext));
            }
            return;
        }
        provider.defaultSerializeValue(value, jgen);
    }
}
