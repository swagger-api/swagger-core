package com.wordnik.swagger.jaxrs;

import com.wordnik.swagger.jaxrs.ext.SwaggerExtension;
import com.wordnik.swagger.models.parameters.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.jaxrs.ext.AbstractSwaggerExtension;

import java.util.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.DefaultValue;

public class DefaultParameterExtension extends AbstractSwaggerExtension {

  @Override
  public List<Parameter> extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip, Iterator<SwaggerExtension> chain) {
    String defaultValue = "";

    if(shouldIgnoreType(type, typesToSkip))
      return new ArrayList<Parameter>();

    List<Parameter> parameters = new ArrayList<Parameter>();
    Parameter parameter = null;
    for(Annotation annotation : annotations) {
      if(annotation instanceof DefaultValue) {
        DefaultValue defaultValueAnnotation = (DefaultValue) annotation;
        defaultValue = defaultValueAnnotation.value();
      }
      if(annotation instanceof QueryParam) {
        QueryParam param = (QueryParam) annotation;
        QueryParameter qp = new QueryParameter()
          .name(param.value());

        if(!defaultValue.isEmpty()) {
          qp.setDefaultValue(defaultValue);
        }
        Property schema = createProperty(type);
        if(schema != null)
          qp.setProperty(schema);
        parameter = qp;
      }
      else if(annotation instanceof PathParam) {
        PathParam param = (PathParam) annotation;
        PathParameter pp = new PathParameter()
          .name(param.value());
        if(!defaultValue.isEmpty())
          pp.setDefaultValue(defaultValue);
        Property schema = createProperty(type);
        if(schema != null)
          pp.setProperty(schema);
        parameter = pp;
      }
      else if(annotation instanceof HeaderParam) {
        HeaderParam param = (HeaderParam) annotation;
        HeaderParameter hp = new HeaderParameter()
          .name(param.value());
        hp.setDefaultValue(defaultValue);
        Property schema = createProperty(type);
        if(schema != null)
          hp.setProperty(schema);
        parameter = hp;
      }
      else if(annotation instanceof CookieParam) {
        CookieParam param = (CookieParam) annotation;
        CookieParameter cp = new CookieParameter()
          .name(param.value());
        if(!defaultValue.isEmpty())
          cp.setDefaultValue(defaultValue);
        Property schema = createProperty(type);
        if(schema != null)
          cp.setProperty(schema);
        parameter = cp;
      }
      else if(annotation instanceof FormParam) {
        FormParam param = (FormParam) annotation;
        FormParameter fp = new FormParameter()
          .name(param.value());
        if(!defaultValue.isEmpty())
          fp.setDefaultValue(defaultValue);
        Property schema = createProperty(type);
        if(schema != null)
          fp.setProperty(schema);
        parameter = fp;
      }
    }
    if(parameter != null) {
      parameters.add(parameter);
    }

    return parameters;
  }

  @Override
  protected boolean shouldIgnoreClass(Class<?> cls) {
    return cls.getName().startsWith("javax.ws.rs.");
  }

  private Property createProperty(Type type) {
    return enforcePrimitive(ModelConverters.getInstance().readAsProperty(type));
  }

  private Property enforcePrimitive(Property in) {
    if (in instanceof RefProperty) {
      return new StringProperty();
    }
    if (in instanceof ArrayProperty) {
      final ArrayProperty array = (ArrayProperty) in;
      array.setItems(enforcePrimitive(array.getItems()));
    }
    return in;
  }
}
