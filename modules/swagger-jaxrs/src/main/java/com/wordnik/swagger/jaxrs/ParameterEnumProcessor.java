package com.wordnik.swagger.jaxrs;

import com.wordnik.swagger.models.parameters.AbstractSerializableParameter;

import java.util.List;

class ParameterEnumProcessor extends AbstractEnumProcessor<AbstractSerializableParameter> {

  @Override
  protected void setEnum(AbstractSerializableParameter container, List<String> items) {
    container.setEnum(items);
  }
}
