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

  @Override
  public int hashCode() {
  	final int prime = 31;
  	int result = 1;
  	result = prime * result
  			+ ((externalDocs == null) ? 0 : externalDocs.hashCode());
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
  	AbstractModel other = (AbstractModel) obj;
  	if (externalDocs == null) {
  		if (other.externalDocs != null)
  			return false;
  	} else if (!externalDocs.equals(other.externalDocs))
  		return false;
  	return true;
  }
}
