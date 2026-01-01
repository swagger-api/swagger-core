package io.swagger.v3.core.util;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import io.swagger.v3.oas.models.media.Encoding;
import tools.jackson.databind.ValueDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EncodingStyleEnumDeserializer extends ValueDeserializer<Encoding.StyleEnum> {
    @Override
    public Encoding.StyleEnum deserialize(JsonParser jp, DeserializationContext ctxt)
            throws JacksonException {
        JsonNode node = jp.objectReadContext().readTree(jp);
        if (node != null) {
            String value = node.asString();
            return getStyleEnum(value);
        }
        return null;
    }

    private Encoding.StyleEnum getStyleEnum(String value) {
        return Arrays.stream(
                Encoding.StyleEnum.values())
                .filter(i -> i.toString().equals(value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        String.format("Can not deserialize value of type Encoding.StyleEnum from String \"%s\": value not one of declared Enum instance names: %s",
                                value,
                                Arrays.stream(Encoding.StyleEnum.values()).map(Encoding.StyleEnum::toString).collect(Collectors.joining(", ", "[", "]")))));
    }
}
