package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class ApiResponsesSerializer extends JsonSerializer<ApiResponses> {

    protected boolean openapi31;

    @Override
    public void serialize(
            ApiResponses value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        if (value == null) {
            provider.defaultSerializeValue(value, jgen);
            return;
        }
        if (!value.isEmpty()) {
            jgen.writeStartObject();
            for (Entry<String, ApiResponse> entry : value.entrySet()) {
                final ApiResponse response = entry.getValue();
                if (StringUtils.isBlank(response.get$ref())) {
                    jgen.writeObjectField(entry.getKey(), response);
                } else {
                    jgen.writeObjectFieldStart(entry.getKey());
                    if (openapi31) {
                        if (StringUtils.isNotBlank(response.getDescription())) {
                            jgen.writeStringField("description", response.getDescription());
                        }
                        if (StringUtils.isNotBlank(response.getSummary())) {
                            jgen.writeStringField("summary", response.getSummary());
                        }
                    }
                    jgen.writeStringField("$ref", response.get$ref());
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
            jgen.writeStartObject();
            for (Map.Entry<String, Object> entry: value.getExtensions().entrySet()) {
                jgen.writeObjectField(entry.getKey(), entry.getValue());
            }
            jgen.writeEndObject();
            return;
        }
        provider.defaultSerializeValue(value, jgen);
    }
}
