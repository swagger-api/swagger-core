package com.wordnik.swagger.jaxrs;

import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.annotations.*;
import com.wordnik.swagger.jackson.ModelResolver;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.parameters.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.util.Json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.annotation.Annotation;
import java.util.*;

public class ParameterProcessor {
  static Logger LOGGER = LoggerFactory.getLogger(ParameterProcessor.class);

  public static Parameter applyAnnotations(Swagger swagger, Parameter parameter, Class<?> cls, Annotation[] annotations, boolean isArray) {
    String defaultValue = null;
    boolean shouldIgnore = false;
    boolean allowMultiple;
    String allowableValues;

    for(Annotation annotation : annotations) {
      if(annotation instanceof ApiParam) {
        ApiParam param = (ApiParam) annotation;
        if(parameter != null) {
          if(!param.defaultValue().isEmpty()){
            defaultValue = param.defaultValue();
          }

          if(param.required() == true) {
            parameter.setRequired(param.required());
          }

          if(param.name() != null && !"".equals(param.name()))
            parameter.setName(param.name());
          parameter.setDescription(param.value());
          parameter.setAccess(param.access());
          allowMultiple = param.allowMultiple() || isArray;
          if(parameter instanceof PathParameter) {
            PathParameter p = (PathParameter) parameter;
            if(defaultValue != null)
              p.setDefaultValue(defaultValue);
            if(allowMultiple == true) {
              Property items = PropertyBuilder.build(p.getType(), p.getFormat(), null);
              p.items(items)
                .array(true)
                .collectionFormat("multi");
            }
          }
          else if(parameter instanceof QueryParameter) {
            QueryParameter p = (QueryParameter) parameter;
            if(defaultValue != null)
              p.setDefaultValue(defaultValue);
            if(allowMultiple == true) {
              Property items = PropertyBuilder.build(p.getType(), p.getFormat(), null);
              p.items(items)
                .array(true)
                .collectionFormat("multi");
            }
          }
          else if(parameter instanceof HeaderParameter) {
            HeaderParameter p = (HeaderParameter) parameter;
            if(defaultValue != null)
              p.setDefaultValue(defaultValue);
            if(allowMultiple == true) {
              Property items = PropertyBuilder.build(p.getType(), p.getFormat(), null);
              p.items(items)
                .array(true)
                .collectionFormat("multi");
            }
          }
          else if(parameter instanceof CookieParameter) {
            CookieParameter p = (CookieParameter) parameter;
            if(defaultValue != null)
              p.setDefaultValue(defaultValue);
            if(allowMultiple == true) {
              Property items = PropertyBuilder.build(p.getType(), p.getFormat(), null);
              p.items(items)
                .array(true)
                .collectionFormat("multi");
            }
          }
          allowableValues = param.allowableValues();
          if(allowableValues != null) {
            if (allowableValues.startsWith("range")) {
              // TODO handle range
            }
            else {
              String[] values = allowableValues.split(",");
              List<String> _enum = new ArrayList<String>();
              for(String value : values) {
                String trimmed = value.trim();
                if(!trimmed.equals(""))
                  _enum.add(trimmed);
              }
              if(parameter instanceof SerializableParameter) {
                SerializableParameter p = (SerializableParameter) parameter;
                if(_enum.size() > 0)
                  p.setEnum(_enum);
              }
            }
          }
        }
        else if(shouldIgnore == false) {
          // must be a body param
          BodyParameter bp = new BodyParameter();
          if(param.name() != null && !"".equals(param.name()))
            bp.setName(param.name());
          else
            bp.setName("body");
          bp.setDescription(param.value());

          if(cls.isArray() || isArray) {
            Class<?> innerType;
            if(isArray) {// array has already been detected
              innerType = cls;
            }
            else
              innerType = cls.getComponentType();
            LOGGER.debug("inner type: " + innerType + " from " + cls);
            Property innerProperty = ModelConverters.getInstance().readAsProperty(innerType);
            if(innerProperty == null) {
              Map<String, Model> models = ModelConverters.getInstance().read(innerType);
              if(models.size() > 0) {
                for(String name: models.keySet()) {
                  if(name.indexOf("java.util") == -1) {
                    bp.setSchema(
                      new ArrayModel().items(new RefProperty().asDefault(name)));
                    if(swagger != null)
                      swagger.addDefinition(name, models.get(name));
                  }
                }
              }
              models = ModelConverters.getInstance().readAll(innerType);
              if(swagger != null) {
                for(String key : models.keySet()) {
                  swagger.model(key, models.get(key));
                }
              }
            }
            else {
              LOGGER.debug("found inner property " + innerProperty);
              bp.setSchema(new ArrayModel().items(innerProperty));

              // creation of ref property doesn't add model to definitions - do it now instead
              if( innerProperty instanceof RefProperty && swagger != null) {
                  Map<String, Model> models = ModelConverters.getInstance().read(innerType);
                  String name = ((RefProperty)innerProperty).getSimpleRef();
                  swagger.addDefinition(name, models.get(name));

                  LOGGER.debug( "added model definition for RefProperty " + name );
              }
            }
          }
          else {
            Map<String, Model> models = ModelConverters.getInstance().read(cls);
            if(models.size() > 0) {
              for(String name: models.keySet()) {
                if(name.indexOf("java.util") == -1) {
                  if(isArray)
                    bp.setSchema(new ArrayModel().items(new RefProperty().asDefault(name)));
                  else
                    bp.setSchema(new RefModel().asDefault(name));
                  if(swagger != null)
                    swagger.addDefinition(name, models.get(name));
                }
              }
              models = ModelConverters.getInstance().readAll(cls);
              if(swagger != null) {
                for(String key : models.keySet()) {
                  swagger.model(key, models.get(key));
                }
              }
            }
            else {
              Property prop = ModelConverters.getInstance().readAsProperty(cls);
              if(prop != null) {
                ModelImpl model = new ModelImpl();
                model.setType(prop.getType());
                bp.setSchema(model);
              }
            }
          }
          parameter = bp;
        }
      }
    }
    return parameter;
  } 
}
