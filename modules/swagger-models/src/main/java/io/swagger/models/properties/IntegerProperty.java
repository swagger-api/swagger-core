package io.swagger.models.properties;

import io.swagger.models.Xml;

public class IntegerProperty extends AbstractNumericProperty implements Property {
  public static final String TYPE = "integer";
  protected Integer _default;

  public IntegerProperty() {
    super.type = TYPE;
    super.format = "int32";
  }
  public IntegerProperty xml(Xml xml) {
    this.setXml(xml);
    return this;
  }

  public IntegerProperty example(Integer example) {
    this.setExample(String.valueOf(example));
    return this;
  }

  public IntegerProperty _default(String _default) {
    if(_default != null) {
      try {
        this._default = Integer.parseInt(_default);
      }
      catch (NumberFormatException e) {
        // continue;
      }
    }
    return this;
  }
  public IntegerProperty _default(Integer _default) {
    this.setDefault(_default);
    return this;
  }

  public static boolean isType(String type, String format) {
    if (TYPE.equals(type) && "int32".equals(format))
      return true;
    else return false;
  }

  public Integer getDefault() {
    return _default;
  }
  public void setDefault(Integer _default) {
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
  	IntegerProperty other = (IntegerProperty) obj;
  	if (_default == null) {
  		if (other._default != null)
  			return false;
  	} else if (!_default.equals(other._default))
  		return false;
  	return true;
  }
}