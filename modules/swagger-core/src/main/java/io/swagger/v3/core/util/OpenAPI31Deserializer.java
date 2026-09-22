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
    private final SpecVersion specVersion;

    public OpenAPI31Deserializer(JsonDeserializer<?> defaultDeserializer)
    {
        this(defaultDeserializer, SpecVersion.V31);
    }

    protected OpenAPI31Deserializer(JsonDeserializer<?> defaultDeserializer, SpecVersion specVersion)
    {
        super(OpenAPI.class);
        this.defaultDeserializer = defaultDeserializer;
        this.specVersion = specVersion;
    }

    @Override
    public OpenAPI deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        OpenAPI openAPI = (OpenAPI) defaultDeserializer.deserialize(jp, ctxt);
        openAPI.setSpecVersion(specVersion);
        return openAPI;
    }
    @Override public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        ((ResolvableDeserializer) defaultDeserializer).resolve(ctxt);
    }
}

