package com.wordnik.swagger.converter;

import com.wordnik.swagger.jackson.ModelResolver;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;
import com.wordnik.swagger.util.Json;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModelConverters {
  static Logger LOGGER = LoggerFactory.getLogger(ModelConverters.class);

  private static final ModelConverters SINGLETON = new ModelConverters();
  private final List<ModelConverter> converters;
  private final Set<String> skippedPackages = new HashSet<String>();
  private final Set<String> skippedClasses = new HashSet<String>();

  static {
    SINGLETON.skippedPackages.add("java.lang");

    ServiceLoader<ModelConverter> loader = ServiceLoader.load(ModelConverter.class);
    Iterator<ModelConverter> itr = loader.iterator();
    while(itr.hasNext()) {
      ModelConverter ext = itr.next();
      if(ext == null)
        LOGGER.error("failed to load extension " + ext);
      else {
        SINGLETON.addConverter(ext);
        LOGGER.debug("adding ModelConverter: " + ext);
      }
    }
  }

  public static ModelConverters getInstance() {
    return SINGLETON;
  }

  public ModelConverters() {
    converters = new CopyOnWriteArrayList<ModelConverter>();
    converters.add(new ModelResolver(Json.mapper()));
  }
  
  public void addConverter(ModelConverter converter){
    converters.add(0,converter);
  }

  public void removeConverter(ModelConverter converter){
    converters.remove(converter);
  }

  public void addPackageToSkip(String pkg) {
    this.skippedPackages.add(pkg);
  }

  public void addClassToSkip(String cls) {
    LOGGER.warn("skipping class " + cls);
    this.skippedClasses.add(cls);
  }

  public Property readAsProperty(Type type) {
    ModelConverterContextImpl context = new ModelConverterContextImpl(
        converters);
    return context.resolveProperty(type, null);
  }

  public Map<String, Model> read(Type type) {
    Map<String, Model> modelMap = new HashMap<String, Model>();
    if (shouldProcess(type)) {
      ModelConverterContextImpl context = new ModelConverterContextImpl(
          converters);
      Model resolve = context.resolve(type);
      for (Entry<String, Model> entry : context.getDefinedModels()
          .entrySet()) {
        if (entry.getValue().equals(resolve)) {
          modelMap.put(entry.getKey(), entry.getValue());
        }
      }
    }
    return modelMap;
  }

  public Map<String, Model> readAll(Type type) {
    if (shouldProcess(type)) {
      ModelConverterContextImpl context = new ModelConverterContextImpl(
          converters);

      LOGGER.debug("ModelConverters readAll from " + type);
      context.resolve(type);
      return context.getDefinedModels();
    }
    return new HashMap<String, Model>();
  }

  private boolean shouldProcess(Type type) {
    boolean process = true;
    if(type instanceof Class<?>){
      Class<?> cls = (Class<?>) type;
      String className = cls.getName();
      for(String packageName: skippedPackages) {
        if(className.startsWith(packageName)) {
          process = false;
        }
      }
      for(String classToSkip: skippedClasses) {
        if(className.equals(classToSkip)) {
          process = false;
        }
      }
    }
    return process;
  }
}