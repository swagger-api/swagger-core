package com.wordnik.swagger.jersey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.sun.jersey.multipart.FormDataParam;
import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.jaxrs.ext.AbstractSwaggerExtension;
import com.wordnik.swagger.jaxrs.ext.SwaggerExtension;
import com.wordnik.swagger.models.parameters.FormParameter;
import com.wordnik.swagger.models.parameters.Parameter;
import com.wordnik.swagger.models.properties.Property;

public class SwaggerJerseyJaxrs extends AbstractSwaggerExtension {

  @Override
  public List<Parameter> extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip,
      Iterator<SwaggerExtension> chain) {
    if(shouldIgnoreType(type, typesToSkip)) {
      // stop the processing chain
      return Collections.emptyList();
    }
    for(Annotation annotation : annotations) {
      if(annotation instanceof FormDataParam) {
        final FormDataParam fd = (FormDataParam) annotation;
        final Class<?> cls = TypeFactory.defaultInstance().constructType(type).getRawClass();
        final Parameter param;
        if(java.io.InputStream.class.isAssignableFrom(cls)) {
          param = new FormParameter().type("file").name(fd.value());
        }
        else {
          FormParameter fp = new FormParameter().name(fd.value());
          Property schema = ModelConverters.getInstance().readAsProperty(type);
          if(schema != null)
            fp.setProperty(schema);
          param = fp;
        }
        return Collections.singletonList(param);
      }
    }
    return super.extractParameters(annotations, type, typesToSkip, chain);
  }

  @Override
  protected boolean shouldIgnoreClass(Class<?> cls) {
    return com.sun.jersey.core.header.FormDataContentDisposition.class.isAssignableFrom(cls);
  }
}
