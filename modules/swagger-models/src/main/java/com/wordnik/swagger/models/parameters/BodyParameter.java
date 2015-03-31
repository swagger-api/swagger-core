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
}