package com.wordnik.swagger.converter;

import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.swagger.ModelResolver;

import java.io.File;
import java.util.*;

public class ModelConverters {
  static ObjectMapper mapper = Json.mapper();
  static List<ModelConverter> converters = new ArrayList<ModelConverter>();

  public static Property readAsProperty(Class<?> cls) {
	  // if(File.class.equals(cls))
	  //   return new FileProperty();
    try {
      Property property = null;
      for(ModelConverter c : converters) {
        property = c.resolveProperty(cls);
        if(property != null)
          return property;
      }
      property = new ModelResolver(mapper).resolveProperty(cls);
      return property;
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Map<String, Model> read(Class<?> cls) {
    Map<String, Model> output = new HashMap<String, Model>();
    if(shouldProcess(cls)) {
      ModelResolver resolver = new ModelResolver(mapper);
      Model model = resolver.resolve(cls);
      if(model instanceof ModelImpl)
        output.put(((ModelImpl)model).getName(), model);
    }
    return output;
  }

  public static Map<String, Model> readAll(Class<?> cls) {
    Map<String, Model> output = new HashMap<String, Model>();
    if(shouldProcess(cls)) {
      ModelResolver resolver = new ModelResolver(mapper);
      resolver.resolve(cls);
      Map<String, Model> models = resolver.getDetectedTypes();
      Map<String, Model> o = new HashMap<String, Model>();
      for(String key : models.keySet()) {
        Model m = models.get(key);
        if(m.getProperties() != null) {
          for(String propertyName : m.getProperties().keySet()) {
            Property prop = m.getProperties().get(propertyName);
            if(prop instanceof RefProperty) {
              RefProperty rp = (RefProperty) prop;
            }
          }
        }
      }
      return models;
    }
    else return output;
  }

  static boolean shouldProcess(Class<?> cls) {
    // if(cls.getName().startsWith("java."))
    //   return false;
    // if(cls.isEnum())
    //   return false;
    return true;
  }
}