package com.wordnik.swagger.jersey;

import com.wordnik.swagger.jaxrs.ext.*;
import com.wordnik.swagger.models.parameters.*;

import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.api.core.InjectParam;

import java.lang.annotation.Annotation;

public class SwaggerJerseyJaxrs implements SwaggerExtension {
  public Parameter processParameter(Annotation[] annotations, Class<?> cls, boolean isArray) {
    for(Annotation annotation : annotations) {
      if(annotation instanceof FormDataParam) {
        FormDataParam fd = (FormDataParam) annotation;
        if(java.io.InputStream.class.equals(cls))
          return new FormParameter().type("file").name(fd.value());
        else {
          FormParameter fp = new FormParameter().name(fd.value());
          Property schema = ModelConverters.readAsProperty(cls);
          if(schema != null)
            fp.setProperty(schema);
          return fp;
        }
      }
    }
    return null;
  }

  public boolean shouldIgnoreClass(Class<?> cls) {
    if(com.sun.jersey.core.header.FormDataContentDisposition.class.equals(cls))
      return true;
    else
      return false;
  }
}