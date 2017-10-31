package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.models.PathItem;

import java.io.IOException;

public class PathDeserializer extends JsonDeserializer<PathItem> {
    @Override
    public PathItem deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode sub = node.get("$ref");

        PathItem result;

        /*
        if(sub != null) {
            result = Json.mapper().convertValue(node, RefPath.class);
        } else*/ {
            result = Json.pathMapper().convertValue(node, PathItem.class);
        }

        return result;
    }
}
