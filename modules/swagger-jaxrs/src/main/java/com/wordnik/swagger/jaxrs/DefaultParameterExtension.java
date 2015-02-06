package com.wordnik.swagger.jaxrs;

import com.wordnik.swagger.jaxrs.ext.SwaggerExtension;
import com.wordnik.swagger.models.parameters.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.converter.ModelConverters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.lang.annotation.Annotation;

import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

public class DefaultParameterExtension implements SwaggerExtension {
  Logger LOGGER = LoggerFactory.getLogger(DefaultParameterExtension.class);

  public List<Parameter> extractParameters(Annotation[] annotations, Class<?> cls, boolean isArray, Iterator<SwaggerExtension> chain) {
    String defaultValue = null;

    if(this.shouldIgnoreClass(cls))
      return new ArrayList<Parameter>();

    List<Parameter> parameters = new ArrayList<Parameter>();
    Parameter parameter = null;
    for(Annotation annotation : annotations) {
      if(annotation instanceof QueryParam) {
        QueryParam param = (QueryParam) annotation;
        QueryParameter qp = new QueryParameter()
          .name(param.value());
        qp.setDefaultValue(defaultValue);
        Property schema = ModelConverters.getInstance().readAsProperty(cls);
        if(schema != null)
          qp.setProperty(schema);
        parameter = qp;
      }
      else if(annotation instanceof PathParam) {
        PathParam param = (PathParam) annotation;
        PathParameter pp = new PathParameter()
          .name(param.value());
        pp.setDefaultValue(defaultValue);
        Property schema = ModelConverters.getInstance().readAsProperty(cls);
        if(schema != null)
          pp.setProperty(schema);
        parameter = pp;
      }
      else if(annotation instanceof HeaderParam) {
        HeaderParam param = (HeaderParam) annotation;
        HeaderParameter hp = new HeaderParameter()
          .name(param.value());
        hp.setDefaultValue(defaultValue);
        Property schema = ModelConverters.getInstance().readAsProperty(cls);
        if(schema != null)
          hp.setProperty(schema);
        parameter = hp;
      }
      else if(annotation instanceof CookieParam) {
        CookieParam param = (CookieParam) annotation;
        CookieParameter cp = new CookieParameter()
          .name(param.value());
        cp.setDefaultValue(defaultValue);
        Property schema = ModelConverters.getInstance().readAsProperty(cls);
        if(schema != null)
          cp.setProperty(schema);
        parameter = cp;
      }
      else if(annotation instanceof FormParam) {
        FormParam param = (FormParam) annotation;
        FormParameter fp = new FormParameter()
          .name(param.value());
        fp.setDefaultValue(defaultValue);
        Property schema = ModelConverters.getInstance().readAsProperty(cls);
        if(schema != null)
          fp.setProperty(schema);
        parameter = fp;
      }
      else if(annotation instanceof DefaultValue) {
        DefaultValue defaultValueAnnotation = (DefaultValue) annotation;
        // TODO: not supported yet
        defaultValue = defaultValueAnnotation.value();
      }
    }
    if(parameter != null) {
      parameters.add(parameter);
    }

    return parameters;
  }

  public boolean shouldIgnoreClass(Class<?> cls) {
    return false;
  }
}