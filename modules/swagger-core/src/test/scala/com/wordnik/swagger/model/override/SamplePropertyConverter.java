package com.wordnik.swagger.model.override;

import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;
import com.wordnik.swagger.models.properties.DateTimeProperty;
import com.wordnik.swagger.converter.ModelConverter;
import com.wordnik.swagger.converter.ModelConverterContext;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Sample converter implementation which turns "MyCustomClass" into a DateTime property
 */
public class SamplePropertyConverter implements ModelConverter {
  @Override
  public Property resolveProperty(Type type, ModelConverterContext context, Annotation[] annotations, Iterator<ModelConverter> chain) {  
    JavaType _type = Json.mapper().constructType(type);
    if(_type != null){
      Class<?> cls = _type.getRawClass();
      if(MyCustomClass.class.isAssignableFrom(cls)) {
        return new DateTimeProperty();
      }
    }
    if(chain.hasNext())
      return chain.next().resolveProperty(type, context, annotations, chain);
    else
      return null;
  }

  @Override
  public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
    if(chain.hasNext())
      return chain.next().resolve(type, context, chain);
    else
      return null;
  }
}