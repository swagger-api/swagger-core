package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.models.responses.ApiResponse;

import java.io.IOException;

public class ResponseDeserializer extends JsonDeserializer<ApiResponse> {
    @Override
    public ApiResponse deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode sub = node.get("$ref");

        /*if (sub != null) {
            return Json.mapper().convertValue(node, RefResponse.class);
        } else*/ {
            ApiResponse response = Json.responseMapper().convertValue(node, ApiResponse.class);
            return response;
        }
    }
}
