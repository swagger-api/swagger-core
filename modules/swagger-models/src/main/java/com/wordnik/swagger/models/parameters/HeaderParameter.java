package com.wordnik.swagger.models.parameters;

public class HeaderParameter extends AbstractSerializableParameter<HeaderParameter> {

  public HeaderParameter() {
    super.setIn("header");
  }
}
