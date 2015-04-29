package com.wordnik.swagger.jaxrs;

import com.wordnik.swagger.models.properties.PropertyBuilder;

import java.util.Map;

class ArgumentsRangeProcessor extends AbstractRangeProcessor<Map<PropertyBuilder.PropertyId, Object>> {

  @Override
  protected void setMinimum(Map<PropertyBuilder.PropertyId, Object> container, Double value) {
    container.put(PropertyBuilder.PropertyId.MINIMUM, value);
  }

  @Override
  protected void setExclusiveMinimum(Map<PropertyBuilder.PropertyId, Object> container, Boolean value) {
    container.put(PropertyBuilder.PropertyId.EXCLUSIVE_MINIMUM, value);
  }

  @Override
  protected void setMaximum(Map<PropertyBuilder.PropertyId, Object> container, Double value) {
    container.put(PropertyBuilder.PropertyId.MAXIMUM, value);
  }

  @Override
  protected void setExclusiveMaximum(Map<PropertyBuilder.PropertyId, Object> container, Boolean value) {
    container.put(PropertyBuilder.PropertyId.EXCLUSIVE_MAXIMUM, value);
  }
}
