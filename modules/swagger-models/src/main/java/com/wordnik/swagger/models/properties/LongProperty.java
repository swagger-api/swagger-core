package com.wordnik.swagger.models.properties;

import com.wordnik.swagger.models.Xml;

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
  public LongProperty _default(Long _default) {
    this.setDefault(_default);
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
  public void setDefault(String _default) {
    this._default(_default);
  }

  @Override
  public int hashCode() {
  	final int prime = 31;
  	int result = 1;
  	result = prime * result + ((_default == null) ? 0 : _default.hashCode());
  	return result;
  }

  @Override
  public boolean equals(Object obj) {
  	if (this == obj)
  		return true;
  	if (obj == null)
  		return false;
  	if (getClass() != obj.getClass())
  		return false;
  	LongProperty other = (LongProperty) obj;
  	if (_default == null) {
  		if (other._default != null)
  			return false;
  	} else if (!_default.equals(other._default))
  		return false;
  	return true;
  }
}