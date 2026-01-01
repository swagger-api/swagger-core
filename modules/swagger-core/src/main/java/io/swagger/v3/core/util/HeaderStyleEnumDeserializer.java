package io.swagger.v3.core.util;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import io.swagger.v3.oas.models.headers.Header;
import tools.jackson.databind.ValueDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HeaderStyleEnumDeserializer extends ValueDeserializer<Header.StyleEnum> {
    @Override
    public Header.StyleEnum deserialize(JsonParser jp, DeserializationContext ctxt)
            throws JacksonException {
        JsonNode node = jp.objectReadContext().readTree(jp);
        if (node != null) {
            String value = node.asString();
            return getStyleEnum(value);
        }
        return null;
    }

    private Header.StyleEnum getStyleEnum(String value) {
        return Arrays.stream(
                Header.StyleEnum.values())
                .filter(i -> i.toString().equals(value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        String.format("Can not deserialize value of type Header.StyleEnum from String \"%s\": value not one of declared Enum instance names: %s",
                                value,
                                Arrays.stream(Header.StyleEnum.values()).map(Header.StyleEnum::toString).collect(Collectors.joining(", ", "[", "]")))));
    }
}
