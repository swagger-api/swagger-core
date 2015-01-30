package com.wordnik.swagger.jersey;

import com.wordnik.swagger.jaxrs.ext.SwaggerExtension;
import com.wordnik.swagger.models.parameters.Parameter;

import java.util.*;
import java.lang.annotation.Annotation;

public class SwaggerJersey2Jaxrs implements SwaggerExtension {
  public List<Parameter> extractParameters(Annotation[] annotations, Class<?> cls, boolean isArray, Iterator<SwaggerExtension> chain) {
    if(chain.hasNext())
      return chain.next().extractParameters(annotations, cls, isArray, chain);
    return null;
  }

  public boolean shouldIgnoreClass(Class<?> cls) {
    return false;
  }
}