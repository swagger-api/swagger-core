package io.swagger.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.models.*;

import java.io.IOException;

public class PathDeserializer extends JsonDeserializer<Path> {
    @Override
    public Path deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode sub = node.get("$ref");

        Path result;

        if(sub != null) {
            result = Json.mapper().convertValue(node, RefPath.class);
        } else {
            result = Json.pathMapper().convertValue(node, Path.class);
        }

        return result;
    }
}
