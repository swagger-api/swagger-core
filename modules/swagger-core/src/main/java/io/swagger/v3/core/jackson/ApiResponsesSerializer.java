package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.v3.oas.models.responses.ApiResponses;

import java.io.IOException;

public class ApiResponsesSerializer extends JsonSerializer<ApiResponses> {

    private JsonSerializer<Object> defaultSerializer;

    public ApiResponsesSerializer() {}

    @Override
    public void serialize(
            ApiResponses value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        if (value != null && value.getExtensions() != null && !value.getExtensions().isEmpty()) {
            jgen.writeStartObject();

            if (!value.isEmpty()) {
                for (String key: value.keySet()) {
                    jgen.writeObjectField(key , value.get(key));
                }
            }
            for (String ext: value.getExtensions().keySet()) {
                jgen.writeObjectField(ext , value.getExtensions().get(ext));
            }
            jgen.writeEndObject();
        } else {
            provider.defaultSerializeValue(value, jgen);
        }
    }
}
