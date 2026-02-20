package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SecuritySchemeDeserializer extends JsonDeserializer<SecurityScheme> {

    protected boolean openapi31;

    @Override
    public SecurityScheme deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        ObjectMapper mapper = null;
        if (openapi31) {
            mapper = Json31.mapper();
        } else {
            mapper = Json.mapper();
        }
        SecurityScheme result = null;

        JsonNode node = jp.getCodec().readTree(jp);

        JsonNode inNode = node.get("type");

        if (inNode != null) {
            String type = inNode.asText();
            if (Arrays.stream(SecurityScheme.Type.values()).noneMatch(t -> t.toString().equals(type))) {
                // wrong type, throw exception
                throw new JsonParseException(jp, String.format("SecurityScheme type %s not allowed", type));
            }
            result = new SecurityScheme()
                    .description(getFieldText("description", node));

            if ("http".equals(type)) {
                result
                        .type(SecurityScheme.Type.HTTP)
                        .scheme(getFieldText("scheme", node))
                        .bearerFormat(getFieldText("bearerFormat", node));
            } else if ("apiKey".equals(type)) {
                result
                        .type(SecurityScheme.Type.APIKEY)
                        .name(getFieldText("name", node))
                        .in(getIn(getFieldText("in", node)));
            } else if ("openIdConnect".equals(type)) {
                result
                        .type(SecurityScheme.Type.OPENIDCONNECT)
                        .openIdConnectUrl(getFieldText("openIdConnectUrl", node));
            } else if ("oauth2".equals(type)) {
                result
                        .type(SecurityScheme.Type.OAUTH2)
                        .flows(mapper.convertValue(node.get("flows"), OAuthFlows.class));
            } else if ("mutualTLS".equals(type)) {
                result
                        .type(SecurityScheme.Type.MUTUALTLS);
            }
            final Iterator<String> fieldNames = node.fieldNames();
            while(fieldNames.hasNext()) {
                final String fieldName = fieldNames.next();
                if(fieldName.startsWith("x-")) {
                    final JsonNode fieldValue = node.get(fieldName);
                    final Object value = Json.mapper().treeToValue(fieldValue, Object.class);
                    result.addExtension(fieldName, value);
                }
            }
        }

        return result;
    }

    private SecurityScheme.In getIn(String value) {
        return Arrays.stream(SecurityScheme.In.values()).filter(i -> i.toString().equals(value)).findFirst().orElse(null);
    }

    private String getFieldText(String fieldName, JsonNode node) {
        JsonNode inNode = node.get(fieldName);
        if (inNode != null) {
            return inNode.asText();
        }
        return null;
    }
}
