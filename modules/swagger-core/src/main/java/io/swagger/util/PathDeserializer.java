package io.swagger.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.swagger.models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathDeserializer extends JsonDeserializer<Path> {
    @Override
    public Path deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode sub = node.get("$ref");

        if (sub != null) {
            return Json.mapper().convertValue(sub, RefPath.class);
        } else {
            Path path = Json.mapper().convertValue(node, PathImpl.class);
            return path;
        }
    }
}
