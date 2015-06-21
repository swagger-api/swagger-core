package io.swagger.models.deserializers.auth;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.OAuth2Definition;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.deserializers.CustomAsTypeDeserializer;

/**
 * @author <a href="mailto:dilip.gundu@wavemaker.com">Dilip Kumar</a>
 * @since 21/6/15
 */
public class SecuritySchemeTypeDeserializer extends CustomAsTypeDeserializer {

    private static final Map<String, Class<? extends SecuritySchemeDefinition>> subTypesMap = new HashMap<String,
            Class<? extends SecuritySchemeDefinition>>();

    static {
        subTypesMap.put("basic", BasicAuthDefinition.class);
        subTypesMap.put("oauth2", OAuth2Definition.class);
        subTypesMap.put("apiKey", ApiKeyAuthDefinition.class);
    }

    public SecuritySchemeTypeDeserializer(
            final JavaType bt, final TypeIdResolver idRes, final String typePropertyName,
            final boolean typeIdVisible, final Class<?> defaultImpl) {
        super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    public SecuritySchemeTypeDeserializer(final AsPropertyTypeDeserializer src, final BeanProperty property) {
        super(src, property);
    }

    @Override
    protected Class<?> findSubType(final JsonNode jsonNode) {
        Class<?> subType;

        if (jsonNode.get("type") != null) { // it won't come
            String type = jsonNode.get("type").asText();
            subType = subTypesMap.get(type);
        } else {
            throw new IllegalStateException("Parameter must contain 'in' field:" + jsonNode);
        }

        return subType;
    }

    @Override
    protected TypeDeserializer newInstance(final BeanProperty property) {
        return new SecuritySchemeTypeDeserializer(this, property);
    }
}
