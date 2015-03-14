package com.wordnik.swagger.converter;

import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;

public interface ModelConverterContext {
  
  /**
   * needs to be called whenever a Model is defined which can be referenced from another 
   * Model or Property
   * 
   * @param name the name of the model
   * @param model the Model
   */
  public void defineModel(String name, Model model);
  
  /**
   * @param type the property Class
   * @return a property representation of the Class. Any referenced models will be defined already.
   */
  public   Property resolveProperty(Type type, Annotation[] annotations);

  /** 
   * @param type The Model Class
   * @return a Model representation of the Class. Any referenced models will be defined already.
   */
  public Model resolve(Type type);

  /**
   * @return an Iterator of ModelConverters.  This iterator is not reused
   */
  public Iterator<ModelConverter> getConverters();
}