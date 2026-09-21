package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.JsonDeserializer;
import io.swagger.v3.oas.models.SpecVersion;

public class OpenAPI32Deserializer extends OpenAPI31Deserializer {

    public OpenAPI32Deserializer(JsonDeserializer<?> defaultDeserializer)
    {
        super(defaultDeserializer, SpecVersion.V32);
    }
}
