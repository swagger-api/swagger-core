package com.wordnik.swagger.converter;

import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.fasterxml.jackson.module.jsonSchema.customProperties.TitleSchemaFactoryWrapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.types.*;

import java.util.*;

public class ModelConverters {
  static ObjectMapper mapper = Json.mapper();

  public static Property readAsProperty(Class cls) {
    SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
    try {
      mapper.acceptJsonFormatVisitor(cls, visitor);
      JsonSchema schema = visitor.finalSchema();
      if(schema.isObjectSchema()) {
        return null;
      }
      return ModelFactory.convertProperty(schema);
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Map<String, Model> read(Class cls) {
    Map<String, JsonSchema> schemas = new HashMap<String, JsonSchema>();
    Map<String, Model> models = new HashMap<String, Model>();
    SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
    try {
      mapper.acceptJsonFormatVisitor(cls, visitor);

      JsonSchema schema = visitor.finalSchema();
      ObjectSchema objectSchema = schema.asObjectSchema();

      if(objectSchema != null) {
        Map<String, JsonSchema> properties = new java.util.HashMap<String, JsonSchema>(objectSchema.getProperties());
        objectSchema.setProperties(properties);

        String schemaName = nameFromId(objectSchema.getId());
        objectSchema.setId(null);
        schemas.put(schemaName, objectSchema);

        for(String key: schemas.keySet()) {
          models.put(key, ModelFactory.convert(schemas.get(key)));
        }
      }
      else {
        Property property = ModelFactory.convertProperty(schema);
        Model model = new Model();
        model.addProperty("default", property);
        models.put(cls.getName(), model);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return models;
  }

  public static Map<String, Model> readAll(Class cls) {
    Map<String, JsonSchema> schemas = new HashMap<String, JsonSchema>();
    Map<String, Model> models = new HashMap<String, Model>();
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

      for(String key: schemas.keySet()) {
        models.put(key, ModelFactory.convert(schemas.get(key)));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return models;
  }

  static String nameFromId(String name) {
    String parts[] = name.split(":");
    return parts[parts.length - 1];
  }
}