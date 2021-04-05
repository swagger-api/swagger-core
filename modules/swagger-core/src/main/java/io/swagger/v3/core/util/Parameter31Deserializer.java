package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class Parameter31Deserializer extends ParameterDeserializer {

    public Parameter31Deserializer() {
        this.openapi31 = true;
    }

    @Override
    public Parameter deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        Parameter result = super.deserialize(jp, ctxt);

        if (result != null && StringUtils.isNotBlank(result.get$ref())) {
            JsonNode node = jp.getCodec().readTree(jp);
            JsonNode desc = node.get("description");
            if (desc != null) {
                result.description(desc.asText());
            }
        }
        return result;
    }
}
