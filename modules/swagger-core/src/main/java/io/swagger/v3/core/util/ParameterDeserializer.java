package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.swagger.v3.oas.models.parameters.CookieParameter;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.PathParameter;
import io.swagger.v3.oas.models.parameters.QueryParameter;

import java.io.IOException;

public class ParameterDeserializer extends JsonDeserializer<Parameter> {

    protected boolean openapi31;

    @Override
    public Parameter deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        Parameter result = null;

        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode sub = node.get("$ref");
        JsonNode inNode = node.get("in");
        JsonNode desc = node.get("description");

        if (sub != null) {
            result = new Parameter().$ref(sub.asText());
            if (desc != null && openapi31) {
                result.description(desc.asText());
            }

        } else if (inNode != null) {
            String in = inNode.asText();

            ObjectReader reader = null;
            ObjectMapper mapper = null;
            if (openapi31) {
                mapper = Json31.mapper();
            } else {
                mapper = Json.mapper();
            }


            if ("query".equals(in)) {
                reader = mapper.readerFor(QueryParameter.class);
            } else if ("header".equals(in)) {
                reader = mapper.readerFor(HeaderParameter.class);
            } else if ("path".equals(in)) {
                reader = mapper.readerFor(PathParameter.class);
            } else if ("cookie".equals(in)) {
                reader = mapper.readerFor(CookieParameter.class);
            }
            if (reader != null) {
                result = reader.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(node);
            }
        }

        return result;
    }
}
