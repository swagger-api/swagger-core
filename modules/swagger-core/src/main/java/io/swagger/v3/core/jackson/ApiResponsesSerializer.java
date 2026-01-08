package io.swagger.v3.core.jackson;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;

import io.swagger.v3.oas.models.responses.ApiResponses;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class ApiResponsesSerializer extends ValueSerializer<ApiResponses> {

    @Override
    public void serialize(
            ApiResponses value, JsonGenerator jgen, SerializationContext provider)
            throws JacksonException {

        if (value != null && value.getExtensions() != null && !value.getExtensions().isEmpty()) {
            jgen.writeStartObject();

            if (!value.isEmpty()) {
                for (Entry<String, io.swagger.v3.oas.models.responses.ApiResponse> entry: value.entrySet()) {
                    jgen.writePOJOProperty(entry.getKey() , entry.getValue());
                }
            }
            for (Map.Entry<String, Object> entry: value.getExtensions().entrySet()) {
                jgen.writePOJOProperty(entry.getKey(), entry.getValue());
            }
            jgen.writeEndObject();
        } else {
            jgen.writePOJO(value);
        }
    }
}
