package io.swagger.v3.core.util;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.callbacks.Callback;
import tools.jackson.databind.ValueDeserializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CallbackDeserializer extends ValueDeserializer<Callback> {

    protected boolean openapi31;

    @Override
    public Callback deserialize(JsonParser jp, DeserializationContext ctxt)
            throws JacksonException {

        final ObjectMapper mapper;
        if (openapi31) {
            mapper = Json31.mapper();
        } else {
            mapper = Json.mapper();
        }
        Callback result = new Callback();
        JsonNode node = jp.objectReadContext().readTree(jp);
        ObjectNode objectNode = (ObjectNode)node;
        Map<String, Object> extensions = new LinkedHashMap<>();
        for (Map.Entry<String, JsonNode> entry : objectNode.properties()) {
            String childName = entry.getKey();
            JsonNode child = entry.getValue();
            // if name start with `x-` consider it an extension
            if (childName.startsWith("x-")) {
                extensions.put(childName, mapper.convertValue(child, Object.class));
            } else if (childName.equals("$ref")) {
                result.$ref(child.asString());
            } else {
                result.put(childName, mapper.convertValue(child, PathItem.class));
            }
        }
        if (!extensions.isEmpty()) {
            result.setExtensions(extensions);
        }
        return result;
    }
}
