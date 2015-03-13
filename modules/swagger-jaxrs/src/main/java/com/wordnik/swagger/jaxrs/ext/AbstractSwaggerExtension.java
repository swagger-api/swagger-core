package com.wordnik.swagger.jaxrs.ext;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.jaxrs.PATCH;

import com.wordnik.swagger.jaxrs.ext.SwaggerExtension;
import com.wordnik.swagger.models.parameters.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.converter.ModelConverters;

import java.util.*;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;

public abstract class AbstractSwaggerExtension {
  public String extractOperationMethod(ApiOperation apiOperation, Method method, Iterator<SwaggerExtension> chain) {
    if(chain.hasNext())
      return chain.next().extractOperationMethod(apiOperation, method, chain);
    else
      return null;
  }

  public List<Parameter> extractParameters(Annotation[] annotations, Class<?> cls, boolean isArray, Set<Class<?>> classesToSkip, Iterator<SwaggerExtension> chain) {
    if(chain.hasNext())
      return chain.next().extractParameters(annotations, cls, isArray, classesToSkip, chain);
    else
      return null;
  }

  public boolean shouldIgnoreClass(Class<?> cls) {
    return false;
  }
}