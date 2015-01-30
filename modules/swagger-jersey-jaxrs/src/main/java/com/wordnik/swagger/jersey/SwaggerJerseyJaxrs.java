package com.wordnik.swagger.jersey;

import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.jaxrs.ext.*;
import com.wordnik.swagger.models.parameters.*;
import com.wordnik.swagger.models.properties.*;

import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.api.core.InjectParam;

import java.util.*;
import java.lang.annotation.Annotation;

public class SwaggerJerseyJaxrs implements SwaggerExtension {
  public List<Parameter> extractParameters(Annotation[] annotations, Class<?> cls, boolean isArray, Iterator<SwaggerExtension> chain) {
    for(Annotation annotation : annotations) {
      if(annotation instanceof FormDataParam) {
        List<Parameter> output = new ArrayList<Parameter>();
        FormDataParam fd = (FormDataParam) annotation;
        if(java.io.InputStream.class.equals(cls)) {
          Parameter param = new FormParameter().type("file").name(fd.value());
          output.add(param);
          return output;
        }
        else {
          FormParameter fp = new FormParameter().name(fd.value());
          Property schema = ModelConverters.getInstance().readAsProperty(cls);
          if(schema != null)
            fp.setProperty(schema);
          output.add(fp);
          return output;
        }
      }
    }
    if(chain.hasNext())
      return chain.next().extractParameters(annotations, cls, isArray, chain);
    return null;
  }

  public boolean shouldIgnoreClass(Class<?> cls) {
    if(com.sun.jersey.core.header.FormDataContentDisposition.class.equals(cls))
      return true;
    else
      return false;
  }
}