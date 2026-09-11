package io.swagger.v3.core.util;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.DelegatingDeserializer;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ValueDeserializer;

public class OpenAPI31Deserializer extends DelegatingDeserializer {

    public OpenAPI31Deserializer(ValueDeserializer<?> defaultDeserializer)
    {
        super(defaultDeserializer);
    }

    @Override
    protected ValueDeserializer<?> newDelegatingInstance(ValueDeserializer<?> newDelegatee) {
        return new OpenAPI31Deserializer(newDelegatee);
    }

    @Override
    public OpenAPI deserialize(JsonParser jp, DeserializationContext ctxt)
            throws JacksonException {
        OpenAPI openAPI = (OpenAPI) super.deserialize(jp, ctxt);
        openAPI.setSpecVersion(SpecVersion.V31);
        return openAPI;
    }
}

