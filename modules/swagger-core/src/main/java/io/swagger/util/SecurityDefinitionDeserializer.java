package io.swagger.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.oas.models.security.SecurityScheme;

import java.io.IOException;

public class SecurityDefinitionDeserializer extends JsonDeserializer<SecurityScheme> {
    @Override
    public SecurityScheme deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        SecurityScheme result = null;

        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode inNode = node.get("type");

        if (inNode != null) {
            String type = inNode.asText();
/*            if ("basic".equals(type)) {
                result = Json.mapper().convertValue(node, BasicAuthDefinition.class);
            } else if ("apiKey".equals(type)) {
                result = Json.mapper().convertValue(node, ApiKeyAuthDefinition.class);
            } else if ("oauth2".equals(type)) {
                result = Json.mapper().convertValue(node, OAuth2Definition.class);
            }
            */
        }

        return result;
    }
}
