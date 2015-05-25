package com.wordnik.swagger.models.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RefProperty extends AbstractProperty implements Property {
  private static final String TYPE = "ref";
  String ref;

  public RefProperty() {
    setType(TYPE);
  }

  public RefProperty(String ref) {
    this();
    set$ref(ref);
  }

  public RefProperty asDefault(String ref) {
    this.set$ref("#/definitions/" + ref);
    return this;
  }
  public RefProperty description(String description) {
    this.setDescription(description);
    return this;
  }

  @Override
  @JsonIgnore
  public String getType() {
    return this.type;
  }
  @Override
  @JsonIgnore
  public void setType(String type) {
    this.type = type;
  }

  public String get$ref() {
    if(ref.startsWith("http"))
      return ref;
    else
      return "#/definitions/" + ref;
  }
  public void set$ref(String ref) {
    if(ref.indexOf("#/definitions/") == 0)
      this.ref = ref.substring("#/definitions/".length());
    else
      this.ref = ref;
  }

  @JsonIgnore
  public String getSimpleRef() {
    if(ref.indexOf("#/definitions/") == 0)
      return ref.substring("#/definitions/".length());
    else
      return ref;
  }

  public static boolean isType(String type, String format) {
    if (TYPE.equals(type))
      return true;
    else return false;
  }

  @Override
  public int hashCode() {
  	final int prime = 31;
  	int result = 1;
  	result = prime * result + ((ref == null) ? 0 : ref.hashCode());
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
  	RefProperty other = (RefProperty) obj;
  	if (ref == null) {
  		if (other.ref != null)
  			return false;
  	} else if (!ref.equals(other.ref))
  		return false;
  	return true;
  }
}