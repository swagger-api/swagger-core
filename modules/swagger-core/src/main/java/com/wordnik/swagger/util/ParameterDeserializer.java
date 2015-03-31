package com.wordnik.swagger.util;

import com.wordnik.swagger.models.parameters.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

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
      result = Json.mapper().convertValue(sub, RefParameter.class);
    } else if (inNode != null) {
      String in = inNode.asText();
      if ("query".equals(in)) {
        result = Json.mapper().convertValue(node, QueryParameter.class);
      } else if ("header".equals(in)) {
        result = Json.mapper().convertValue(node, HeaderParameter.class);
      } else if ("path".equals(in)) {
        result = Json.mapper().convertValue(node, PathParameter.class);
      } else if ("formData".equals(in)) {
        result = Json.mapper().convertValue(node, FormParameter.class);
      } else if ("body".equals(in)) {
        result = Json.mapper().convertValue(node, BodyParameter.class);
      } else if ("cookie".equals(in)) {
        result = Json.mapper().convertValue(node, CookieParameter.class);
      }
    }

    return result;
  }
}
