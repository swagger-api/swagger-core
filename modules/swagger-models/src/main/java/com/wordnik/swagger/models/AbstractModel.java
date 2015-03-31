package com.wordnik.swagger.models;

public abstract class AbstractModel implements Model {
  private ExternalDocs externalDocs;

  @Override
  public ExternalDocs getExternalDocs() {
    return externalDocs;
  }

  public void setExternalDocs(ExternalDocs value) {
    externalDocs = value;
  }

  public void cloneTo(Object clone) {
    AbstractModel cloned = (AbstractModel) clone;
    cloned.externalDocs = this.externalDocs;
  }

  public Object clone() {
    return null;
  }
}
