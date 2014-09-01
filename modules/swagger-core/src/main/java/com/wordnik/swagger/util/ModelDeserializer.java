package com.wordnik.swagger.util;

import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;

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
      ModelImpl model = Json.mapper().convertValue(node, ModelImpl.class);
      if(model != null) {
        List<String> enums = model.getEnum();
        if(enums != null) {
          for(String name: enums) {
            Property p = model.getProperties().get(name);
            if(p != null) {
              p.setRequired(true);
            }
          }
        }
      }

      return model;
    }
    // ModelImpl m = new ModelImpl();
    // return this.deserialize(jp, ctxt, m);
  }
}