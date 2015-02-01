package com.wordnik.swagger.util;

import com.wordnik.swagger.models.properties.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*; 
import com.fasterxml.jackson.databind.node.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.io.IOException;

public class PropertyDeserializer extends JsonDeserializer<Property> {
  Logger LOGGER = LoggerFactory.getLogger(PropertyDeserializer.class);

  @Override
  public Property deserialize(JsonParser jp, DeserializationContext ctxt) 
    throws IOException, JsonProcessingException {
    JsonNode node = jp.getCodec().readTree(jp);
    return propertyFromNode(node);
  }

  Property propertyFromNode(JsonNode node) {
    List<String> _enum = null;
    String type = null, format = null, title = null, description = null, _default = null,
      pattern = null, discriminator = null;
    
    Integer minItems = null, maxItems = null, minProperties = null, maxProperties = null, 
      maxLength = null, minLength = null;

    Double minimum = null, maximum = null, exclusiveMinimum = null, exclusiveMaximum = null;

    Boolean uniqueItems = null;
    // externalDocs
    // example
    JsonNode detailNode = node.get("example");
    String example;
    if(detailNode != null) {
      example = detailNode.asText();
    } else {
      example = null;
    }

    // enum properties
    detailNode = node.get("enum");
    if(detailNode != null) {
      ArrayNode an = (ArrayNode)detailNode;
      List<String> output = new ArrayList<String>();
      for(int i = 0; i < an.size(); i++) {
        JsonNode child = an.get(i);
        if(child instanceof TextNode) {
          output.add((String)((TextNode) child).asText());
        }
      }
      _enum = output;
    }

    // string properties
    detailNode = node.get("type");
    if(detailNode != null)
      type = (String) ((TextNode) detailNode).asText();
    detailNode = node.get("format");
    if(detailNode != null)
      format = (String) ((TextNode) detailNode).asText();
    detailNode = node.get("title");
    if(detailNode != null)
      title = (String) ((TextNode) detailNode).asText();
    detailNode = node.get("description");
    if(detailNode != null)
      description = (String) ((TextNode) detailNode).asText();
    detailNode = node.get("default");
    if(detailNode != null)
      _default = (String) ((TextNode) detailNode).asText();
    detailNode = node.get("pattern");
    if(detailNode != null)
      pattern = (String) ((TextNode) detailNode).asText();
    detailNode = node.get("discriminator");
    if(detailNode != null)
      discriminator = (String) ((TextNode) detailNode).asText();

    // integer properties
    detailNode = node.get("minItems");
    if(detailNode != null)
      minItems = new Integer(((NumericNode) detailNode).intValue());
    detailNode = node.get("maxItems");
    if(detailNode != null)
      maxItems = new Integer(((NumericNode) detailNode).intValue());
    detailNode = node.get("minProperties");
    if(detailNode != null)
      minProperties = new Integer(((NumericNode) detailNode).intValue());
    detailNode = node.get("maxProperties");
    if(detailNode != null)
      maxProperties = new Integer(((NumericNode) detailNode).intValue());
    detailNode = node.get("maxLength");
    if(detailNode != null)
      maxLength = new Integer(((NumericNode) detailNode).intValue());
    detailNode = node.get("minLength");
    if(detailNode != null)
      minLength = new Integer(((NumericNode) detailNode).intValue());

    // number properties
    detailNode = node.get("minimum");
    if(detailNode != null)
      minimum = new Double(((NumericNode) detailNode).doubleValue());
    detailNode = node.get("maximum");
    if(detailNode != null)
      maximum = new Double(((NumericNode) detailNode).doubleValue());
    detailNode = node.get("exclusiveMinimum");
    if(detailNode != null)
      exclusiveMinimum = new Double(((NumericNode) detailNode).doubleValue());
    detailNode = node.get("exclusiveMaximum");
    if(detailNode != null)
      exclusiveMaximum = new Double(((NumericNode) detailNode).doubleValue());

    // boolean properties
    detailNode = node.get("uniqueItems");
    if(detailNode != null)
      uniqueItems = (Boolean) ((BooleanNode) detailNode).booleanValue();

    Map<String, Object> args = new HashMap<String, Object>();
    args.put("example", example);
    args.put("enum", _enum);
    args.put("type", type);
    args.put("format", format);
    args.put("title", title);
    args.put("description", description);
    args.put("default", _default);
    args.put("pattern", pattern);
    args.put("discriminator", discriminator);
    args.put("minItems", minItems);
    args.put("maxItems", maxItems);
    args.put("minProperties", minProperties);
    args.put("maxProperties", maxProperties);
    args.put("minLength", minLength);
    args.put("maxLength", maxLength);
    args.put("minimum", minimum);
    args.put("maximum", maximum);
    args.put("exclusiveMinimum", exclusiveMinimum);
    args.put("exclusiveMaximum", exclusiveMinimum);
    args.put("uniqueItems", uniqueItems);

    detailNode = node.get("$ref");
    if(detailNode != null) {
      String ref = (String) ((TextNode) detailNode).asText();
      return new RefProperty(ref).description(description);
    }

    if("object".equals(type)) {
      detailNode = node.get("additionalProperties");
      if(detailNode != null) {
        Property items = propertyFromNode(detailNode);
        if(items != null) {
          return new MapProperty(items).description(description);
        }
      }
    }
    if("array".equals(type)) {
      detailNode = node.get("items");
      if(detailNode != null) {
        Property subProperty = propertyFromNode(detailNode);
        return new ArrayProperty().items(subProperty).description(description);
      }
    }

    Property output = PropertyBuilder.build(type, format, args);
    if(output == null) {
      LOGGER.warn("no property from " + type + ", " + format + ", " + args);
      return null;
    }
    output.setDescription(description);
    return output;
  }
}