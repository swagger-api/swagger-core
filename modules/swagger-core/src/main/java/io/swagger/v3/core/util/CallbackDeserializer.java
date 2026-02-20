package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.callbacks.Callback;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class CallbackDeserializer extends JsonDeserializer<Callback> {

    protected boolean openapi31;

    @Override
    public Callback deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        final ObjectMapper mapper;
        if (openapi31) {
            mapper = Json31.mapper();
        } else {
            mapper = Json.mapper();
        }
        Callback result = new Callback();
        JsonNode node = jp.getCodec().readTree(jp);
        ObjectNode objectNode = (ObjectNode)node;
        Map<String, Object> extensions = new LinkedHashMap<>();
        for (Iterator<String> it = objectNode.fieldNames(); it.hasNext(); ) {
            String childName = it.next();
            JsonNode child = objectNode.get(childName);
            // if name start with `x-` consider it an extension
            if (childName.startsWith("x-")) {
                extensions.put(childName, mapper.convertValue(child, Object.class));
            } else if (childName.equals("$ref")) {
                result.$ref(child.asText());
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
