package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.models.media.Encoding;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EncodingStyleEnumDeserializer extends JsonDeserializer<Encoding.StyleEnum> {
    @Override
    public Encoding.StyleEnum deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        if (node != null) {
            String value = node.asText();
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
