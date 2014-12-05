package com.wordnik.swagger.util;

import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.Property;
import com.wordnik.swagger.models.properties.RefProperty;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*; 
import com.fasterxml.jackson.databind.node.*;

import java.util.List;
import java.util.Map;
import java.io.IOException;

public class ModelDeserializer extends JsonDeserializer<Model> {
  @Override
  public Model deserialize(JsonParser jp, DeserializationContext ctxt) 
    throws IOException, JsonProcessingException {
    JsonNode node = jp.getCodec().readTree(jp);
    JsonNode sub = node.get("$ref");
    JsonNode allOf = node.get("allOf");
    
    if(sub != null) {
      return Json.mapper().convertValue(sub, RefModel.class);
    }
    else if (allOf != null) {
      ComposedModel model = null;
      // we only support one parent, no multiple inheritance or composition
      model = Json.mapper().convertValue(node, ComposedModel.class); 
      return model;
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
