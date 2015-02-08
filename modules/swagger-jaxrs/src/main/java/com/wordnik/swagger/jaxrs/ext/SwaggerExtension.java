package com.wordnik.swagger.jaxrs.ext;

import com.wordnik.swagger.models.parameters.Parameter;

import java.lang.annotation.Annotation;
import java.util.*;

public interface SwaggerExtension {
  List<Parameter> extractParameters(Annotation[] annotations, Class<?> cls, boolean isArray, Set<Class<?>> classesToSkip, Iterator<SwaggerExtension> chain);
  boolean shouldIgnoreClass(Class<?> cls);
}