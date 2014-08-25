package com.wordnik.swagger.util;

import com.wordnik.swagger.models.properties.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.deser.*; 
import com.fasterxml.jackson.databind.node.*;

import java.io.IOException;

public class PropertyDeserializer extends JsonDeserializer<Property> {
  @Override
  public Property deserialize(JsonParser jp, DeserializationContext ctxt) 
    throws IOException, JsonProcessingException {
    JsonNode node = jp.getCodec().readTree(jp);
    return propertyFromNode(node);
  }

  Property propertyFromNode(JsonNode node) {
    String type = null;
    String format = null;

    JsonNode sub = node.get("$ref");
    if(sub != null) {
      String ref = (String) ((TextNode) node.get("$ref")).asText();
      return new RefProperty(ref);
    }

    sub = node.get("type");
    if(sub != null)
      type = (String) ((TextNode) node.get("type")).asText();

    if("object".equals(type)) {
      sub = node.get("additionalProperties");
      if(sub != null) {
        Property items = propertyFromNode(sub);
        if(items != null) {
          return new MapProperty(items);
        }
      }
    }

    sub = node.get("format");
    if(sub != null)
      format = (String) ((TextNode) node.get("format")).asText();

    return PropertyBuilder.build(type, format);
  }
}