package com.wordnik.swagger.jaxrs.ext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.models.parameters.Parameter;

public interface SwaggerExtension {
  String extractOperationMethod(ApiOperation apiOperation, Method method, Iterator<SwaggerExtension> chain);
  List<Parameter> extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip, Iterator<SwaggerExtension> chain);
}
