package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import io.swagger.v3.oas.models.parameters.CookieParameter;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.PathParameter;
import io.swagger.v3.oas.models.parameters.QueryParameter;

import java.io.IOException;

public class ParameterDeserializer extends JsonDeserializer<Parameter> {
    @Override
    public Parameter deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        Parameter result = null;

        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode sub = node.get("$ref");
        JsonNode inNode = node.get("in");

        if (sub != null) {
            result = new Parameter().$ref(sub.asText());
        } else if (inNode != null) {
            String in = inNode.asText();

            ObjectReader reader = null;

            if ("query".equals(in)) {
                reader = Json.mapper().readerFor(QueryParameter.class);
            } else if ("header".equals(in)) {
                reader = Json.mapper().readerFor(HeaderParameter.class);
            } else if ("path".equals(in)) {
                reader = Json.mapper().readerFor(PathParameter.class);
            } else if ("cookie".equals(in)) {
                reader = Json.mapper().readerFor(CookieParameter.class);
            }
            if (reader != null) {
                result = reader.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(node);
            }
        }

        return result;
    }
}
