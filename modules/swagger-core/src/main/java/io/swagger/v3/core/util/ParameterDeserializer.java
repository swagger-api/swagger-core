package io.swagger.v3.core.util;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectReader;
import io.swagger.v3.oas.models.parameters.CookieParameter;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.PathParameter;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.cfg.EnumFeature;

import java.io.IOException;

public class ParameterDeserializer extends ValueDeserializer<Parameter> {

    protected boolean openapi31;

    @Override
    public Parameter deserialize(JsonParser jp, DeserializationContext ctxt)
            throws JacksonException {
        Parameter result = null;

        JsonNode node = jp.objectReadContext().readTree(jp);
        JsonNode sub = node.get("$ref");
        JsonNode inNode = node.get("in");
        JsonNode desc = node.get("description");

        if (sub != null) {
            result = new Parameter().$ref(sub.asString());
            if (desc != null && openapi31) {
                result.description(desc.asString());
            }

        } else if (inNode != null) {
            String in = inNode.asString();

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
                result = reader.with(EnumFeature.READ_ENUMS_USING_TO_STRING).readValue(node);
            }
        }

        return result;
    }
}
