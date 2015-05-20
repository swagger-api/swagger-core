package com.wordnik.swagger.jaxrs;

import com.wordnik.swagger.models.parameters.AbstractSerializableParameter;

class ParameterRangeProcessor extends AbstractRangeProcessor<AbstractSerializableParameter> {

  @Override
  protected void setMinimum(AbstractSerializableParameter container, Double value) {
    container.setMinimum(value);
  }

  @Override
  protected void setExclusiveMinimum(AbstractSerializableParameter container, Boolean value) {
    container.setExclusiveMinimum(value);
  }

  @Override
  protected void setMaximum(AbstractSerializableParameter container, Double value) {
    container.setMaximum(value);
  }

  @Override
  protected void setExclusiveMaximum(AbstractSerializableParameter container, Boolean value) {
    container.setExclusiveMaximum(value);
  }
}
