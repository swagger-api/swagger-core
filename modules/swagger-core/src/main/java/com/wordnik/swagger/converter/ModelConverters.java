package com.wordnik.swagger.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.fasterxml.jackson.module.jsonSchema.customProperties.TitleSchemaFactoryWrapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.types.*;

import java.util.*;

public class ModelConverters {
  static ObjectMapper mapper = new ObjectMapper();
  public static Map<String, JsonSchema> readAll(Class cls) {
    Map<String, JsonSchema> schemas = new HashMap<String, JsonSchema>();
    SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
    try {
      mapper.acceptJsonFormatVisitor(cls, visitor);

      JsonSchema schema = visitor.finalSchema();
      ObjectSchema objectSchema = schema.asObjectSchema();

      Map<String, JsonSchema> properties = new java.util.HashMap<String, JsonSchema>(objectSchema.getProperties());
      for(String key : objectSchema.getProperties().keySet()) {
        JsonSchema propertySchema = objectSchema.getProperties().get(key);
        if(propertySchema.isObjectSchema()) {
          if(propertySchema.getId() != null) {
            String name = nameFromId(propertySchema.getId());
            propertySchema.setId(null);
            schemas.put(name, propertySchema);
            ReferenceSchema ref = new ReferenceSchema("#/definitions/" + name);
            properties.put(name, ref);
          }
        }
      }
      objectSchema.setProperties(properties);

      String schemaName = nameFromId(objectSchema.getId());
      objectSchema.setId(null);
      schemas.put(schemaName, objectSchema);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return schemas;
  }

  static String nameFromId(String name) {
    String parts[] = name.split(":");
    return parts[parts.length - 1];
  }
}