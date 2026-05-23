package io.swagger.v3.core.util;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import tools.jackson.databind.ValueDeserializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApiResponsesDeserializer extends ValueDeserializer<ApiResponses> {

    protected boolean openapi31;

    @Override
    public ApiResponses deserialize(JsonParser jp, DeserializationContext ctxt)
            throws JacksonException {

        final ObjectMapper mapper;
        if (openapi31) {
            mapper = Json31.mapper();
        } else {
            mapper = Json.mapper();
        }
        ApiResponses result = new ApiResponses();
        JsonNode node = jp.objectReadContext().readTree(jp);
        ObjectNode objectNode = (ObjectNode) node;
        Map<String, Object> extensions = new LinkedHashMap<>();
        for (Map.Entry<String, JsonNode> entry : objectNode.properties()) {
            String childName = entry.getKey();
            JsonNode child = entry.getValue();
            // if name start with `x-` consider it an extension
            if (childName.startsWith("x-")) {
                extensions.put(childName, mapper.convertValue(child, Object.class));
            } else {
                result.put(childName, mapper.convertValue(child, ApiResponse.class));
            }
        }
        if (!extensions.isEmpty()) {
            result.setExtensions(extensions);
        }
        return result;
    }
}
