package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import io.swagger.v3.oas.models.responses.ApiResponses;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class ApiResponsesSerializer extends JsonSerializer<ApiResponses> {

    @Override
    public void serialize(
            ApiResponses value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        if (value != null && value.getExtensions() != null && !value.getExtensions().isEmpty()) {
            jgen.writeStartObject();

            if (!value.isEmpty()) {
                for (Entry<String, io.swagger.v3.oas.models.responses.ApiResponse> entry: value.entrySet()) {
                    jgen.writeObjectField(entry.getKey() , entry.getValue());
                }
            }
            for (Map.Entry<String, Object> entry: value.getExtensions().entrySet()) {
                jgen.writeObjectField(entry.getKey(), entry.getValue());
            }
            jgen.writeEndObject();
        } else {
            provider.defaultSerializeValue(value, jgen);
        }
    }
}
