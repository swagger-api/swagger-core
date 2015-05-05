package com.wordnik.swagger.servlet;

import java.lang.annotation.Annotation;
import java.util.EnumMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.models.ArrayModel;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.ModelImpl;
import com.wordnik.swagger.models.RefModel;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.models.parameters.AbstractSerializableParameter;
import com.wordnik.swagger.models.parameters.BodyParameter;
import com.wordnik.swagger.models.parameters.Parameter;
import com.wordnik.swagger.models.properties.ArrayProperty;
import com.wordnik.swagger.models.properties.Property;
import com.wordnik.swagger.models.properties.PropertyBuilder;
import com.wordnik.swagger.models.properties.RefProperty;

public class ParameterProcessor {
  static Logger LOGGER = LoggerFactory.getLogger(ParameterProcessor.class);
  private static final Table<Class<? extends AllowableValues>, Boolean, AbstractAllowableValuesProcessor> allowableValuesProcessors = HashBasedTable
    .create();

  static {
    allowableValuesProcessors.put(AllowableRangeValues.class, true, new ArgumentsRangeProcessor());
    allowableValuesProcessors.put(AllowableRangeValues.class, false, new ParameterRangeProcessor());
    allowableValuesProcessors.put(AllowableEnumValues.class, true, new ArgumentsEnumProcessor());
    allowableValuesProcessors.put(AllowableEnumValues.class, false, new ParameterEnumProcessor());
  }

  public static Parameter parseApiImplicit(Swagger swagger, Parameter parameter, Class<?> cls,
    ApiImplicitParam param, Property property) {
    if (parameter instanceof AbstractSerializableParameter) {
      final AbstractSerializableParameter p = (AbstractSerializableParameter) parameter;

      if (property != null) {
        p.setType(property.getType());
        p.setFormat(property.getFormat());
      }
      if (param.required()) {
        p.setRequired(param.required());
      }
      final String name = param.name();
      if (StringUtils.isNotEmpty(name)) {
        p.setName(name);
      }
      p.setDescription(param.value());
      p.setAccess(param.access());

      AllowableValues allowableValues = null;
      final String allowableValuesString = param.allowableValues();
      if (allowableValuesString != null) {
        allowableValues = AllowableRangeValues.create(allowableValuesString);
        if (allowableValues == null) {
          allowableValues = AllowableEnumValues.create(allowableValuesString);
        }
      }

      final String defaultValue = param.defaultValue();
      if (param.allowMultiple() /* || isArray */) {
        final Map<PropertyBuilder.PropertyId, Object> args = new EnumMap<PropertyBuilder.PropertyId, Object>(
          PropertyBuilder.PropertyId.class);
        if (!defaultValue.isEmpty()) {
          args.put(PropertyBuilder.PropertyId.DEFAULT, defaultValue);
        }
        if (allowableValues != null) {
          allowableValuesProcessors.get(allowableValues.getClass(), true).process(args, allowableValues);
        }

        p.items(PropertyBuilder.build(p.getType(), p.getFormat(), args))
          .type(ArrayProperty.TYPE)
          .format(null)
          .collectionFormat("multi");
      } else {
        if (!defaultValue.isEmpty()) {
          p.setDefaultValue(defaultValue);
        }
        if (allowableValues != null) {
          allowableValuesProcessors.get(allowableValues.getClass(), false).process(p, allowableValues);
        }
      }
    } else {
      // must be a body param
      BodyParameter bp = new BodyParameter();
      bp.setRequired(param.required());
      if (param.name() != null && !"".equals(param.name()))
        bp.setName(param.name());
      else bp.setName("body");
      bp.setDescription(param.value());

      //          if (cls.isArray() || isArray) {
      //            Class<?> innerType;
      //            if (isArray) {// array has already been detected
      //              innerType = cls;
      //            }
      //            else innerType = cls.getComponentType();
      //            LOGGER.debug("inner type: " + innerType + " from " + cls);
      //            Property innerProperty = ModelConverters.getInstance().readAsProperty(innerType);
      //            if (innerProperty == null) {
      //              Map<String, Model> models = ModelConverters.getInstance().read(innerType);
      //              if (models.size() > 0) {
      //                for (String name : models.keySet()) {
      //                  if (name.indexOf("java.util") == -1) {
      //                    bp.setSchema(
      //                      new ArrayModel().items(new RefProperty().asDefault(name)));
      //                    if (swagger != null)
      //                      swagger.addDefinition(name, models.get(name));
      //                  }
      //                }
      //              }
      //              models = ModelConverters.getInstance().readAll(innerType);
      //              if (swagger != null) {
      //                for (String key : models.keySet()) {
      //                  swagger.model(key, models.get(key));
      //                }
      //              }
      //            }
      //            else {
      //              LOGGER.debug("found inner property " + innerProperty);
      //              bp.setSchema(new ArrayModel().items(innerProperty));
      //
      //              // creation of ref property doesn't add model to definitions - do it now instead
      //              if (innerProperty instanceof RefProperty && swagger != null) {
      //                Map<String, Model> models = ModelConverters.getInstance().read(innerType);
      //                String name = ((RefProperty) innerProperty).getSimpleRef();
      //                swagger.addDefinition(name, models.get(name));
      //
      //                LOGGER.debug("added model definition for RefProperty " + name);
      //              }
      //            }
      //          }
      //          else
      {
        Map<String, Model> models = ModelConverters.getInstance().read(cls);
        if (models.size() > 0) {
          for (String name : models.keySet()) {
            if (name.indexOf("java.util") == -1) {
              //                  if (isArray)
              //                    bp.setSchema(new ArrayModel().items(new RefProperty().asDefault(name)));
              //                  else 
              bp.setSchema(new RefModel().asDefault(name));
              if (swagger != null)
                swagger.addDefinition(name, models.get(name));
            }
          }
          models = ModelConverters.getInstance().readAll(cls);
          if (swagger != null) {
            for (String key : models.keySet()) {
              swagger.model(key, models.get(key));
            }
          }
        }
        else {
          Property prop = ModelConverters.getInstance().readAsProperty(cls);
          if (prop != null) {
            ModelImpl model = new ModelImpl();
            model.setType(prop.getType());
            bp.setSchema(model);
          }
        }
      }
      parameter = bp;
    }
    return parameter;
  }
}
