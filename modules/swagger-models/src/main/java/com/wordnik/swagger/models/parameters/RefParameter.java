package com.wordnik.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RefParameter extends AbstractParameter implements Parameter {
  String ref;

  public RefParameter(String ref) {
    set$ref(ref);
  }

  public RefParameter asDefault(String ref) {
    this.set$ref("#/parameters/" + ref);
    return this;
  }
  public RefParameter description(String description) {
    this.setDescription(description);
    return this;
  }

  public String get$ref() {
    if(ref.startsWith("http"))
      return ref;
    else
      return "#/parameters/" + ref;
  }
  public void set$ref(String ref) {
    if(ref.indexOf("#/parameters/") == 0)
      this.ref = ref.substring("#/parameters/".length());
    else
      this.ref = ref;
  }

  @Override
  @JsonIgnore
  public boolean getRequired() {
    return required;
  }

  @JsonIgnore
  public String getSimpleRef() {
    if(ref.indexOf("#/parameters/") == 0)
      return ref.substring("#/parameters/".length());
    else
      return ref;
  }

  public static boolean isType(String type, String format) {
    if("$ref".equals(type))
      return true;
    else return false;
  }

  @Override
  public int hashCode() {
  	final int prime = 31;
  	int result = super.hashCode();
  	result = prime * result + ((ref == null) ? 0 : ref.hashCode());
  	return result;
  }

  @Override
  public boolean equals(Object obj) {
  	if (this == obj)
  		return true;
  	if (!super.equals(obj))
  		return false;
  	if (getClass() != obj.getClass())
  		return false;
  	RefParameter other = (RefParameter) obj;
  	if (ref == null) {
  		if (other.ref != null)
  			return false;
  	} else if (!ref.equals(other.ref))
  		return false;
  	return true;
  }
}