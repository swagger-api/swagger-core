package com.wordnik.swagger.models;

import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.models.properties.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.types.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

public class ModelFactory {
  static ObjectMapper mapper = Json.mapper();

  public static Model convert(JsonSchema schema) {
    Model model = new Model();
    if(schema.isObjectSchema()) {
      ObjectSchema obj = schema.asObjectSchema();
      model.setSimple(false);

      for(String key: obj.getProperties().keySet()) {
        Property property = convertProperty(obj.getProperties().get(key));
        if(property != null)
          model.addProperty(key, property);
      }
    }

    return model;
  }

  public static Property convertProperty(JsonSchema schema) {
    System.out.println("converting ");
    Json.printPretty(schema);

    if(schema.isNumberSchema() && !schema.isIntegerSchema()) {
      String format = null;
      NumberSchema s = schema.asNumberSchema();
      if(s.getFormat() != null) {
        format = s.getFormat().toString();
      }
      return new DoubleProperty();
    }
    if(schema.isIntegerSchema()) {
      String format = null;
      IntegerSchema s = schema.asIntegerSchema();
      if(s.getFormat() != null) {
        format = s.getFormat().toString();
        if("int32".equals(format))
          return new IntegerProperty();
        if("int64".equals(format))
          return new LongProperty();
      }
      return new IntegerProperty();
    }

    if(schema.isBooleanSchema()) {
      return new BooleanProperty();
    }
    if(schema.isStringSchema()) {
      String format = null;
      StringSchema s = schema.asStringSchema();
      if(s.getFormat() != null) {
        format = s.getFormat().toString();
        if("date-time".equals(format))
          return new DateTimeProperty();
        if("date".equals(format))
          return new DateProperty();
      }
      return new StringProperty();
    }
    if(schema.isArraySchema()) {
      ArraySchema arraySchema = (ArraySchema) schema;
      ArrayProperty a = new ArrayProperty();
      if(arraySchema.getItems() != null) {
        if(arraySchema.getItems().isSingleItems()) {
          Property items = convertProperty(arraySchema.getItems().asSingleItems().getSchema());
          a.setItems(items);
          return a;        
        }
      }
      else {
        System.out.println("oops! ");
        Json.printPretty(schema);
        return a;
      }
    }
    if(schema.get$ref() != null) {
      return new RefProperty().asDefault(
        com.wordnik.swagger.converter.ModelConverters.nameFromId(schema.get$ref())
      );
    }
    try{
      if(schema.isObjectSchema()) {
        ObjectSchema o = schema.asObjectSchema();
        ObjectSchema.AdditionalProperties add = o.getAdditionalProperties();

        if(add instanceof ObjectSchema.SchemaAdditionalProperties) {
          ObjectSchema.SchemaAdditionalProperties ap = (ObjectSchema.SchemaAdditionalProperties) add;
          Property p = convertProperty(ap.getJsonSchema());
          if(p != null) {
            return new MapProperty(p);
          }
        }
        else if(schema.getId() != null ){
          return new RefProperty().asDefault(
            com.wordnik.swagger.converter.ModelConverters.nameFromId(schema.getId())
          );
        }
        System.out.println("unmatched object");
        Json.printPretty(schema);
        return null;
      }

      System.out.println("unknown: " + mapper.writeValueAsString(schema));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}