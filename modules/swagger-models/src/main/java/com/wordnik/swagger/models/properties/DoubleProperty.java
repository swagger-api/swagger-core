package com.wordnik.swagger.models.properties;

import com.wordnik.swagger.models.Xml;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class DoubleProperty extends AbstractNumericProperty implements Property {
  protected Double _default;

  public DoubleProperty() {
    super.type = "number";
    super.format = "double";
  }
  public DoubleProperty xml(Xml xml) {
    this.setXml(xml);
    return this;
  }

  public DoubleProperty example(Double example) {
    this.setExample(String.valueOf(example));
    return this;
  }

  public DoubleProperty _default(String _default) {
    if(_default != null) {
      try {
        this._default = Double.parseDouble(_default);
      }
      catch (NumberFormatException e) {
        // continue;
      }
    }
    return this;
  }

  public static boolean isType(String type, String format) {
    if("number".equals(type) && "double".equals(format))
      return true;
    else return false;
  }

  public Double getDefault() {
    return _default;
  }
  public void setDefault(Double _default) {
    this._default = _default;
  }
  @JsonIgnore
  public void setDefault(String _default) {
    this._default(_default);
  }
}