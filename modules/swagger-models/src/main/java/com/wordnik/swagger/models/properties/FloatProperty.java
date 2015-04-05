package com.wordnik.swagger.models.properties;

import com.wordnik.swagger.models.Xml;

public class FloatProperty extends AbstractNumericProperty implements Property {
  protected Float _default;

  public FloatProperty() {
    super.type = "number";
    super.format = "float";
  }
  public FloatProperty xml(Xml xml) {
    this.setXml(xml);
    return this;
  }

  public FloatProperty example(Float example) {
    this.setExample(String.valueOf(example));
    return this;
  }

  public FloatProperty _default(String _default) {
    if(_default != null) {
      try {
        this._default = Float.parseFloat(_default);
      }
      catch (NumberFormatException e) {
        // continue;
      }
    }
    return this;
  }
  public FloatProperty _default(Float _default) {
    this.setDefault(_default);
    return this;
  }

  public static boolean isType(String type, String format) {
    if("number".equals(type) && "float".equals(format))
      return true;
    else return false;
  }

  public Float getDefault() {
    return _default;
  }
  public void setDefault(Float _default) {
    this._default = _default;
  }
  public void setDefault(String _default) {
    this._default(_default);
  }
}