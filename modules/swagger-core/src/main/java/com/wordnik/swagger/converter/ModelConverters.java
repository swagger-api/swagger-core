package com.wordnik.swagger.converter;

import com.wordnik.swagger.jackson.ModelResolver;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.util.Json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JavaType;

import java.io.File;
import java.util.*;
import java.lang.reflect.Type;

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


  public static Map<String, Model> readAll(Type type) {
    JavaType javaType = mapper.getTypeFactory().constructType(type);
    Map<String, Model> output = new LinkedHashMap<String, Model>();
    if(shouldProcess(type)) {
      ModelResolver resolver = new ModelResolver(mapper);
      resolver.resolve(javaType);
      Map<String, Model> models = resolver.getDetectedTypes();
      List<String> keys = new ArrayList<String>();
      Iterator<String> iter = models.keySet().iterator();
      while (iter.hasNext())
        keys.add(iter.next());

      Collections.sort(keys);

      for(String key : keys) {
        Model m = models.get(key);
        output.put(key, m);
      }
      return output;
    }
    else return output;
  }

  public static Map<String, Model> readAll(Class<?> cls) {
    Map<String, Model> output = new LinkedHashMap<String, Model>();
    if(shouldProcess(cls)) {
      ModelResolver resolver = new ModelResolver(mapper);
      resolver.resolve(cls);
      Map<String, Model> models = resolver.getDetectedTypes();

      List<String> keys = new ArrayList<String>();
      Iterator<String> iter = models.keySet().iterator();
      while (iter.hasNext())
        keys.add(iter.next());
      Collections.sort(keys);

      for(String key : keys) {
        Model m = models.get(key);
        output.put(key, m);
      }
      return output;
    }
    else return output;
  }

  static boolean shouldProcess(Type t) {
    return true;
  }

  static boolean shouldProcess(Class<?> cls) {
    if(cls.getName().startsWith("java.lang"))
      return false;
    // if(cls.isEnum())
    //   return false;
    return true;
  }
}