package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.parameters.CookieParameter;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.PathParameter;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.parameters.QueryStringParameter;

import java.io.IOException;

public class ParameterDeserializer extends JsonDeserializer<Parameter> {

    /**
     * @deprecated kept for subclasses compiled against it; the version is read through
     * {@link #specVersion()} so that subclasses can also express versions newer than 3.1.
     */
    @Deprecated
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
            if (desc != null && specVersion() != SpecVersion.V30) {
                result.description(desc.asText());
            }

        } else if (inNode != null) {
            String in = inNode.asText();

            ObjectReader reader = null;
            ObjectMapper mapper = mapper();

            if ("query".equals(in)) {
                reader = mapper.readerFor(QueryParameter.class);
            } else if ("header".equals(in)) {
                reader = mapper.readerFor(HeaderParameter.class);
            } else if ("path".equals(in)) {
                reader = mapper.readerFor(PathParameter.class);
            } else if ("cookie".equals(in)) {
                reader = mapper.readerFor(CookieParameter.class);
            } else if ("querystring".equals(in) && specVersion() == SpecVersion.V32) {
                reader = mapper.readerFor(QueryStringParameter.class);
            }
            if (reader != null) {
                result = reader.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(node);
                if (result != null && result.getStyle() == Parameter.StyleEnum.COOKIE
                        && specVersion() != SpecVersion.V32) {
                    // 'cookie' parameter style was added in OpenAPI 3.2; reject it for
                    // earlier versions to preserve the previous unknown-enum failure
                    throw new JsonMappingException(jp,
                            "Parameter style 'cookie' requires OpenAPI 3.2");
                }
            }
        }

        return result;
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
