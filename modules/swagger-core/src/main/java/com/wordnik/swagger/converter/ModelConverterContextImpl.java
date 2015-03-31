package com.wordnik.swagger.converter;

import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.ModelImpl;
import com.wordnik.swagger.models.properties.Property;
import com.wordnik.swagger.util.Json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.lang.reflect.Type;

public class ModelConverterContextImpl implements ModelConverterContext {
  private static final Logger LOGGER = LoggerFactory.getLogger(ModelConverterContextImpl.class);
  
  private final List<ModelConverter> converters;
  private final Map<String, Model> modelByName;
  private final HashMap<Type,Model> modelByType;
  private final Set<Type> processedTypes;

  public ModelConverterContextImpl(List<ModelConverter> converters ) {
    this.converters = converters;
    modelByName = new TreeMap<String, Model>();
    modelByType = new HashMap<Type, Model>();
    processedTypes = new HashSet<Type>();
  }
  
  public ModelConverterContextImpl(ModelConverter converter) {
    this(new ArrayList<ModelConverter>());
    converters.add(converter);
  }
  
  public Iterator<ModelConverter> getConverters() {
    return converters.iterator();
  }

  @Override
  public void defineModel(String name, Model model) {
    if(LOGGER.isDebugEnabled()){
      LOGGER.debug(String.format("defineModel %s %s", name, model));
    }
    modelByName.put(name, model);
  }
  
  public Map<String,Model> getDefinedModels(){
    return Collections.unmodifiableMap(modelByName);
  }

  @Override
  public Property resolveProperty(Type type, Annotation[] annotations) {
    if(LOGGER.isDebugEnabled()){
      LOGGER.debug(String.format("resolveProperty %s", type));
    }
    Iterator<ModelConverter> converters = this.getConverters();
    if(converters.hasNext()) {
      ModelConverter converter = converters.next();
      return converter.resolveProperty(type, this, annotations, converters);
    }
    return null;
  }

  @Override
  public Model resolve(Type type) {    
    if(processedTypes.contains(type)){
      return modelByType.get(type);
    } else {
      processedTypes.add(type);
    }
    if(LOGGER.isDebugEnabled()){
      LOGGER.debug(String.format("resolve %s", type));
    }
    Iterator<ModelConverter> converters = this.getConverters();
    Model resolved = null;
    if(converters.hasNext()) {
      ModelConverter converter = converters.next();
      LOGGER.debug("trying extension " + converter);
      resolved = converter.resolve(type, this, converters);
    }
    if(resolved != null) {
      modelByType.put(type, resolved);
      if(resolved instanceof ModelImpl) {
        ModelImpl impl = (ModelImpl) resolved;
        if(impl.getName() != null)
          modelByName.put(impl.getName(), resolved);
      }
    }

    return resolved;
  }
}