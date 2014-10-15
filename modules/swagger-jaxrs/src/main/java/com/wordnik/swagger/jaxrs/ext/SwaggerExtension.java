package com.wordnik.swagger.jaxrs.ext;

import com.wordnik.swagger.models.parameters.Parameter;

import java.lang.annotation.Annotation;

public interface SwaggerExtension {
  Parameter processParameter(Annotation[] annotations, Class<?> cls, boolean isArray);
  boolean shouldIgnoreClass(Class<?> cls);
}