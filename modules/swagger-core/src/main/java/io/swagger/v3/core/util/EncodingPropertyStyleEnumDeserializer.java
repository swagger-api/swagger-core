package io.swagger.v3.core.util;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import io.swagger.v3.oas.models.media.EncodingProperty;
import tools.jackson.databind.ValueDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EncodingPropertyStyleEnumDeserializer extends ValueDeserializer<EncodingProperty.StyleEnum> {
    @Override
    public EncodingProperty.StyleEnum deserialize(JsonParser jp, DeserializationContext ctxt)
            throws JacksonException {
        JsonNode node = jp.objectReadContext().readTree(jp);
        if (node != null) {
            String value = node.asString();
            return getStyleEnum(value);
        }
        return null;
    }

    private EncodingProperty.StyleEnum getStyleEnum(String value) {
        return Arrays.stream(
                EncodingProperty.StyleEnum.values())
                .filter(i -> i.toString().equals(value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        String.format("Can not deserialize value of type EncodingProperty.StyleEnum from String \"%s\": value not one of declared Enum instance names: %s",
                                value,
                                Arrays.stream(EncodingProperty.StyleEnum.values()).map(EncodingProperty.StyleEnum::toString).collect(Collectors.joining(", ", "[", "]")))));
    }
}
