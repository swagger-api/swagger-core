package com.wordnik.swagger.jersey;

import com.wordnik.swagger.jaxrs.ext.SwaggerExtension;
import com.wordnik.swagger.models.parameters.Parameter;

import java.util.*;
import java.lang.annotation.Annotation;

import javax.ws.rs.BeanParam;

public class SwaggerJersey2Jaxrs implements SwaggerExtension {
  public List<Parameter> extractParameters(Annotation[] annotations, Class<?> cls, boolean isArray, Iterator<SwaggerExtension> chain) {
    for(Annotation annotation : annotations) {
      if(annotation instanceof BeanParam) {
        /*
        if there are two parameters inside here, you can process them as such:

        List<Parameter> output = new ArrayList<Parameter>();
        for(Class<?> cls : YOUR_PARAMS) {
          Iterator<SwaggerExtension> extensions = SwaggerExtensions.chain();
          output.addAll(extensions.next().extractParameters(...));
        }
        return output;

        */
      }
    }

    if(chain.hasNext())
      return chain.next().extractParameters(annotations, cls, isArray, chain);
    return null;
  }

  public boolean shouldIgnoreClass(Class<?> cls) {
    return false;
  }
}