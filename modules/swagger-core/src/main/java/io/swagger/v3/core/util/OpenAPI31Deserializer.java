package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;

import java.io.IOException;

public class OpenAPI31Deserializer extends StdDeserializer<OpenAPI> implements ResolvableDeserializer {

    private final JsonDeserializer<?> defaultDeserializer;

    public OpenAPI31Deserializer(JsonDeserializer<?> defaultDeserializer)
    {
        super(OpenAPI.class);
        this.defaultDeserializer = defaultDeserializer;
    }

    @Override
    public OpenAPI deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        OpenAPI openAPI = (OpenAPI) defaultDeserializer.deserialize(jp, ctxt);
        openAPI.setSpecVersion(SpecVersion.V31);
        return openAPI;
    }
    @Override public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        ((ResolvableDeserializer) defaultDeserializer).resolve(ctxt);
    }
}

