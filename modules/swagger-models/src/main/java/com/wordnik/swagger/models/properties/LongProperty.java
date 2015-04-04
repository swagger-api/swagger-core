package com.wordnik.swagger.models.properties;

import com.wordnik.swagger.models.Xml;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class LongProperty extends AbstractNumericProperty implements Property {
  protected Long _default;

  public LongProperty() {
    super.type = "integer";
    super.format = "int64";
  }
  public LongProperty xml(Xml xml) {
    this.setXml(xml);
    return this;
  }

  public LongProperty example (Long example) {
    this.setExample(String.valueOf(example));
    return this;
  }

  public LongProperty _default(String _default) {
    if(_default != null) {
      try {
        this._default = Long.parseLong(_default);
      }
      catch (NumberFormatException e) {
        // continue;
      }
    }
    return this;
  }

  public static boolean isType(String type, String format) {
    if("integer".equals(type) && "int64".equals(format))
      return true;
    else return false;
  }

  public Long getDefault() {
    return _default;
  }
  public void setDefault(Long _default) {
    this._default = _default;
  }
  @JsonIgnore
  public void setDefault(String _default) {
    this._default(_default);
  }
}