package com.wordnik.swagger.models.parameters;

public class QueryParameter extends AbstractSerializableParameter<QueryParameter> {

  public QueryParameter() {
    super.setIn("query");
  }
}
