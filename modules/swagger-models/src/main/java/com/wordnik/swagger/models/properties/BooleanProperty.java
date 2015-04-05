package com.wordnik.swagger.models.properties;

import com.wordnik.swagger.models.Xml;

public class BooleanProperty extends AbstractProperty implements Property {
  protected Boolean _default;
  public BooleanProperty() {
    super.type = "boolean";
  }
  public BooleanProperty xml(Xml xml) {
    this.setXml(xml);
    return this;
  }

  public BooleanProperty example(Boolean example) {
    this.setExample(String.valueOf(example));
    return this;
  }
  public BooleanProperty _default(String _default) {
    try {
      this.setDefault(Boolean.parseBoolean(_default));
    }
    catch (Exception e) {
      //cotinue
    }
    return this;
  }
  public BooleanProperty _default(boolean _default) {
    this.setDefault(_default);
    return this;
  }

  public static boolean isType(String type, String format) {
    if("boolean".equals(type))
      return true;
    else return false;
  }

  public Boolean getDefault() {
    return _default;
  }
  public void setDefault(Boolean _default) {
    this._default = _default;
  }
  public void setDefault(String _default) {
    this._default(_default);
  }
}