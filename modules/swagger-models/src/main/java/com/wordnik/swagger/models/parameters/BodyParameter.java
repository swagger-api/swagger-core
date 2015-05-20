package com.wordnik.swagger.models.parameters;

import com.wordnik.swagger.models.Model;

public class BodyParameter extends AbstractParameter implements Parameter {
  Model schema;

  public BodyParameter() {
    super.setIn("body");
  }

  public BodyParameter schema(Model schema) {
    this.setSchema(schema);
    return this;
  }
  public BodyParameter description(String description) {
    this.setDescription(description);
    return this;
  }
  public BodyParameter name(String name) {
    this.setName(name);
    return this;
  }

  public void setSchema(Model schema) {
    this.schema = schema;
  }
  public Model getSchema() {
    return schema;
  }

  @Override
  public int hashCode() {
  	final int prime = 31;
  	int result = super.hashCode();
  	result = prime * result + ((schema == null) ? 0 : schema.hashCode());
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
  	BodyParameter other = (BodyParameter) obj;
  	if (schema == null) {
  		if (other.schema != null)
  			return false;
  	} else if (!schema.equals(other.schema))
  		return false;
  	return true;
  }
}