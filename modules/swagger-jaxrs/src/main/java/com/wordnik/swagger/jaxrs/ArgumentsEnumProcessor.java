package com.wordnik.swagger.jaxrs;

import com.wordnik.swagger.models.properties.PropertyBuilder;

import java.util.List;
import java.util.Map;

class ArgumentsEnumProcessor extends AbstractEnumProcessor<Map<PropertyBuilder.PropertyId, Object>> {

  @Override
  protected void setEnum(Map<PropertyBuilder.PropertyId, Object> container, List<String> items) {
    container.put(PropertyBuilder.PropertyId.ENUM, items);
  }
}
