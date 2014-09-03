package com.wordnik.swagger.util;

import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*; 
import com.fasterxml.jackson.databind.node.*;

import java.util.List;
import java.io.IOException;

public class ModelDeserializer extends JsonDeserializer<Model> {
  @Override
  public Model deserialize(JsonParser jp, DeserializationContext ctxt) 
    throws IOException, JsonProcessingException {
    JsonNode node = jp.getCodec().readTree(jp);
    JsonNode sub = node.get("$ref");
    
    if(sub != null) {
      return Json.mapper().convertValue(sub, RefModel.class);
    }
    else {
      sub = node.get("type");
      Model model = null;
      if(sub != null && "array".equals(((TextNode)sub).textValue())) {
        model = Json.mapper().convertValue(node, ArrayModel.class);
      }
      else {
        model = Json.mapper().convertValue(node, ModelImpl.class);
      }
      return model;
    }
  }
}