package com.wordnik.swagger.jersey;

import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.jaxrs.ext.*;
import com.wordnik.swagger.models.parameters.*;
import com.wordnik.swagger.models.properties.*;

import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.api.core.InjectParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.lang.annotation.Annotation;

public class SwaggerJerseyJaxrs extends AbstractSwaggerExtension implements SwaggerExtension {
  static Logger LOGGER = LoggerFactory.getLogger(SwaggerJerseyJaxrs.class);

  public List<Parameter> extractParameters(Annotation[] annotations, Class<?> cls, boolean isArray, Set<Class<?>> classesToSkip, Iterator<SwaggerExtension> chain) {
    List<Parameter> output = new ArrayList<Parameter>();
    if(shouldIgnoreClass(cls) || classesToSkip.contains(cls)) {
      // stop the processing chain
      classesToSkip.add(cls);
      return output;
    }
    for(Annotation annotation : annotations) {
      if(annotation instanceof FormDataParam) {
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
      return chain.next().extractParameters(annotations, cls, isArray, classesToSkip, chain);
    return null;
  }

  public boolean shouldIgnoreClass(Class<?> cls) {
    boolean output = false;
    if(com.sun.jersey.core.header.FormDataContentDisposition.class.equals(cls))
      output = true;
    else
      output = false;
    LOGGER.debug("should ignore " + cls + ": " + output);
    return output;
  }
}