package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SecuritySchemeDeserializer extends JsonDeserializer<SecurityScheme> {

    /**
     * @deprecated kept for subclasses compiled against it; the version is read through
     * {@link #specVersion()} so that subclasses can also express versions newer than 3.1.
     */
    @Deprecated
    protected boolean openapi31;

    @Override
    public SecurityScheme deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        ObjectMapper mapper = mapper();
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
            if (specVersion() == SpecVersion.V32) {
                JsonNode deprecatedNode = node.get("deprecated");
                if (deprecatedNode != null && deprecatedNode.isBoolean()) {
                    result.setDeprecated(deprecatedNode.asBoolean());
                }
                result.setOauth2MetadataUrl(getFieldText("oauth2MetadataUrl", node));
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

    /**
     * Returns the spec version this deserializer targets. "3.1 or later" semantics are
     * shared by 3.2; subclasses override this to support newer versions.
     */
    protected SpecVersion specVersion() {
        return openapi31 ? SpecVersion.V31 : SpecVersion.V30;
    }

    protected ObjectMapper mapper() {
        return SpecVersionMappers.mapper(specVersion());
    }
}
