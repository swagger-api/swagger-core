package com.wordnik.swagger.jaxrs.ext;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.models.parameters.Parameter;

import java.lang.annotation.Annotation;
import java.util.*;
import java.lang.reflect.Method;

public interface SwaggerExtension {
  String extractOperationMethod(ApiOperation apiOperation, Method method, Iterator<SwaggerExtension> chain);
  List<Parameter> extractParameters(Annotation[] annotations, Class<?> cls, boolean isArray, Set<Class<?>> classesToSkip, Iterator<SwaggerExtension> chain);
  boolean shouldIgnoreClass(Class<?> cls);
}