package com.wordnik.swagger.util;

import com.wordnik.swagger.models.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;

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
    else
      return Json.mapper().convertValue(node, ModelImpl.class);
    // ModelImpl m = new ModelImpl();
    // return this.deserialize(jp, ctxt, m);
  }
}