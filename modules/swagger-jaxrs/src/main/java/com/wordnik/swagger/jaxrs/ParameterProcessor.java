package com.wordnik.swagger.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
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
  private static final Table<Class<? extends AllowableValues>, Boolean, AbstractAllowableValuesProcessor<?, ?>> ALLOWED_VALUES_PROCESSORS =
      HashBasedTable.create();

  static {
    ALLOWED_VALUES_PROCESSORS.put(AllowableRangeValues.class, true, new ArgumentsRangeProcessor());
    ALLOWED_VALUES_PROCESSORS.put(AllowableRangeValues.class, false, new ParameterRangeProcessor());
    ALLOWED_VALUES_PROCESSORS.put(AllowableEnumValues.class, true, new ArgumentsEnumProcessor());
    ALLOWED_VALUES_PROCESSORS.put(AllowableEnumValues.class, false, new ParameterEnumProcessor());
  }

  public static Parameter applyAnnotations(Swagger swagger, Parameter parameter, Type type, List<Annotation> annotations) {
    final AnnotationsHelper helper = new AnnotationsHelper(annotations);
    if (helper.isContext()) {
      return null;
    }
    final JavaType javaType = TypeFactory.defaultInstance().constructType(type);
    final ApiParam param = helper.getApiParam();
    if (parameter instanceof AbstractSerializableParameter) {
      final AbstractSerializableParameter<?> p = (AbstractSerializableParameter<?>) parameter;

      if (param.required()) {
        p.setRequired(true);
      }
      if (StringUtils.isNotEmpty(param.name())) {
        p.setName(param.name());
      }
      if (StringUtils.isNotEmpty(param.value())) {
        p.setDescription(param.value());
      }
      if (StringUtils.isNotEmpty(param.access())) {
        p.setAccess(param.access());
      }

      AllowableValues allowableValues = null;
      if (StringUtils.isNotEmpty(param.allowableValues())) {
        allowableValues = AllowableRangeValues.create(param.allowableValues());
        if (allowableValues == null) {
          allowableValues = AllowableEnumValues.create(param.allowableValues());
        }
      }

      final String defaultValue = param.defaultValue();
      if (p.getItems() != null || param.allowMultiple()) {
        if (p.getItems() == null) {
          // Convert to array
          final Map<PropertyBuilder.PropertyId, Object> args = new EnumMap<PropertyBuilder.PropertyId, Object>(PropertyBuilder.PropertyId.class);
          args.put(PropertyBuilder.PropertyId.DEFAULT, p.getDefaultValue());
          p.setDefaultValue(null);
          args.put(PropertyBuilder.PropertyId.ENUM, p.getEnum());
          p.setEnum(null);
          args.put(PropertyBuilder.PropertyId.MINIMUM, p.getMinimum());
          p.setMinimum(null);
          args.put(PropertyBuilder.PropertyId.EXCLUSIVE_MINIMUM, p.isExclusiveMinimum());
          p.setExclusiveMinimum(null);
          args.put(PropertyBuilder.PropertyId.MAXIMUM, p.getMaximum());
          p.setMaximum(null);
          args.put(PropertyBuilder.PropertyId.EXCLUSIVE_MAXIMUM, p.isExclusiveMaximum());
          p.setExclusiveMaximum(null);
          Property items = PropertyBuilder.build(p.getType(), p.getFormat(), args);
          p.type(ArrayProperty.TYPE).format(null).items(items);
        }
        final Map<PropertyBuilder.PropertyId, Object> args = new EnumMap<PropertyBuilder.PropertyId, Object>(PropertyBuilder.PropertyId.class);
        if (!defaultValue.isEmpty()) {
          args.put(PropertyBuilder.PropertyId.DEFAULT, defaultValue);
        }
        processAllowedValues(allowableValues, true, args);
        PropertyBuilder.merge(p.getItems(), args);
        p.collectionFormat("csv");
      } else {
        if (!defaultValue.isEmpty()) {
          p.setDefaultValue(defaultValue);
        }
        processAllowedValues(allowableValues, false, p);
      }
    } else {
      // must be a body param
      BodyParameter bp = new BodyParameter();
      bp.setRequired(param.required());
      bp.setName(StringUtils.isNotEmpty(param.name()) ? param.name() : "body");
      if (StringUtils.isNotEmpty(param.value())) {
        bp.setDescription(param.value());
      }

      if(javaType.isContainerType()) {
        final Type innerType = javaType.getContentType();
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

            LOGGER.debug("added model definition for RefProperty " + name);
          }
        }
      } else {
        Map<String, Model> models = ModelConverters.getInstance().read(type);
        if(models.size() > 0) {
          for(String name: models.keySet()) {
            if(name.indexOf("java.util") == -1) {
              bp.setSchema(new RefModel().asDefault(name));
              if(swagger != null) {
                swagger.addDefinition(name, models.get(name));
              }
            }
          }
          if(swagger != null) {
            for(Map.Entry<String, Model> entry : ModelConverters.getInstance().readAll(type).entrySet()) {
              swagger.model(entry.getKey(), entry.getValue());
            }
          }
        }
        else {
          Property prop = ModelConverters.getInstance().readAsProperty(type);
          if(prop != null) {
            ModelImpl model = new ModelImpl();
            model.setType(prop.getType());
            model.setFormat(prop.getFormat());
            model.setDescription(prop.getDescription());
            bp.setSchema(model);
          }
        }
      }
      parameter = bp;
    }
    return parameter;
  }

  private static <C> void processAllowedValues(AllowableValues values, boolean key, C container) {
    if (values == null) {
      return;
    }
    @SuppressWarnings("unchecked")
    final AbstractAllowableValuesProcessor<C, AllowableValues> processor =
        (AbstractAllowableValuesProcessor<C, AllowableValues>) ALLOWED_VALUES_PROCESSORS.get(values.getClass(), key);
    processor.process(container, values);
  }

  /**
   * The <code>AnnotationsHelper</code> class defines helper methods for
   * accessing supported parameter annotations.
   */
  private static class AnnotationsHelper {
    private static final ApiParam DEFAULT_API_PARAM = getDefaultApiParam(null);
    private boolean context;
    private ApiParam apiParam = DEFAULT_API_PARAM;

    /**
     * Constructs an instance.
     * @param annotations array or parameter annotations
     */
    public AnnotationsHelper(List<Annotation> annotations) {
      for (Annotation item : annotations) {
        if (item instanceof Context) {
          context = true;
        } else if (item instanceof ApiParam) {
          apiParam = (ApiParam) item;
        }
      }
    }

    /**
     * Checks whether the @{@link Context} annotation is present.
     * @return <code>true<code> if parameter is defined with the @{@link Context} annotation
     */
    public boolean isContext() {
      return context;
    }

    /**
     * Returns @{@link ApiParam} annotation. If no @{@link ApiParam} is present
     * a default one will be returned.
     * @return @{@link ApiParam} annotation
     */
    public ApiParam getApiParam() {
      return apiParam;
    }

    /**
     * Returns a default @{@link ApiParam} annotation for parameters without it.
     * @param annotationHolder a placeholder for default @{@link ApiParam}
     *          annotation
     * @return @{@link ApiParam} annotation
     */
    private static ApiParam getDefaultApiParam(@ApiParam String annotationHolder) {
      for (Method method : AnnotationsHelper.class.getDeclaredMethods()) {
        if ("getDefaultApiParam".equals(method.getName())) {
          return (ApiParam) method.getParameterAnnotations()[0][0];
        }
      }
      throw new IllegalStateException("Failed to locate default @ApiParam");
    }
  }
}
