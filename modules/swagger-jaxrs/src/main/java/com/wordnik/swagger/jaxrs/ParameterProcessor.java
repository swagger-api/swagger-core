package com.wordnik.swagger.jaxrs;

import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.annotations.*;
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
          if(!"".equals(param.defaultValue())){
            defaultValue = param.defaultValue();
          }

          // parameter.required(param.required());
          if(param.name() != null && !"".equals(param.name()))
            parameter.setName(param.name());
          parameter.setDescription(param.value());
          parameter.setAccess(param.access());
          allowMultiple = param.allowMultiple() || isArray;
          if(allowMultiple == true) {
            if(parameter instanceof PathParameter) {
              PathParameter p = (PathParameter) parameter;
              Property items = PropertyBuilder.build(p.getType(), p.getFormat(), null);
              p.items(items)
                .array(true)
                .collectionFormat("multi");
              p.setDefaultValue(defaultValue);
            }
            else if(parameter instanceof QueryParameter) {
              QueryParameter p = (QueryParameter) parameter;
              Property items = PropertyBuilder.build(p.getType(), p.getFormat(), null);
              p.items(items)
                .array(true)
                .collectionFormat("multi");
              p.setDefaultValue(defaultValue);
            }
            else if(parameter instanceof HeaderParameter) {
              HeaderParameter p = (HeaderParameter) parameter;
              Property items = PropertyBuilder.build(p.getType(), p.getFormat(), null);
              p.items(items)
                .array(true)
                .collectionFormat("multi");
              p.setDefaultValue(defaultValue);
            }
            else if(parameter instanceof CookieParameter) {
              CookieParameter p = (CookieParameter) parameter;
              Property items = PropertyBuilder.build(p.getType(), p.getFormat(), null);
              p.items(items)
                .array(true)
                .collectionFormat("multi");
              p.setDefaultValue(defaultValue);
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
                    swagger.addDefinition(name, models.get(name));
                  }
                }
              }
              models = ModelConverters.getInstance().readAll(innerType);
              for(String key : models.keySet()) {
                swagger.model(key, models.get(key));
              }
            }
            else {
              LOGGER.debug("found inner property " + innerProperty);
              bp.setSchema(new ArrayModel().items(innerProperty));
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
                  swagger.addDefinition(name, models.get(name));
                }
              }
              models = ModelConverters.getInstance().readAll(cls);
              for(String key : models.keySet()) {
                swagger.model(key, models.get(key));
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