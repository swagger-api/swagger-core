package io.swagger.models;

import io.swagger.models.properties.Property;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class RefModel implements Model {
  // internally, the ref value is never fully qualified
  private String ref;
  private String description;
  private ExternalDocs externalDocs;
  private Map<String, Property> properties;
  private Object example;

  public RefModel(){}
  public RefModel(String ref){
    set$ref(ref);
  }

  public RefModel asDefault(String ref) {
    this.set$ref("#/definitions/" + ref);
    return this;
  }

  // not allowed in a $ref
  @JsonIgnore
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonIgnore
  public Map<String, Property> getProperties() {
    return properties;
  }
  public void setProperties(Map<String, Property> properties) {
    this.properties = properties;
  }

  @JsonIgnore
  public String getSimpleRef() {
    if(ref.indexOf("#/definitions/") == 0)
      return ref.substring("#/definitions/".length());
    else
      return ref;
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
  public Object getExample() {
    return example;
  }
  public void setExample(Object example) {
    this.example = example;
  }

  @JsonIgnore
  public ExternalDocs getExternalDocs() {
    return externalDocs;
  }

  public void setExternalDocs(ExternalDocs value) {
    externalDocs = value;
  }

  public Object clone() {
    RefModel cloned = new RefModel();
    cloned.ref = this.ref;
    cloned.description = this.description;
    cloned.properties = this.properties;
    cloned.example = this.example;

    return cloned;
  }

  @Override
  public int hashCode() {
  	final int prime = 31;
  	int result = 1;
  	result = prime * result
  			+ ((description == null) ? 0 : description.hashCode());
  	result = prime * result + ((example == null) ? 0 : example.hashCode());
  	result = prime * result
  			+ ((externalDocs == null) ? 0 : externalDocs.hashCode());
  	result = prime * result
  			+ ((properties == null) ? 0 : properties.hashCode());
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
  	RefModel other = (RefModel) obj;
  	if (description == null) {
  		if (other.description != null)
  			return false;
  	} else if (!description.equals(other.description))
  		return false;
  	if (example == null) {
  		if (other.example != null)
  			return false;
  	} else if (!example.equals(other.example))
  		return false;
  	if (externalDocs == null) {
  		if (other.externalDocs != null)
  			return false;
  	} else if (!externalDocs.equals(other.externalDocs))
  		return false;
  	if (properties == null) {
  		if (other.properties != null)
  			return false;
  	} else if (!properties.equals(other.properties))
  		return false;
  	if (ref == null) {
  		if (other.ref != null)
  			return false;
  	} else if (!ref.equals(other.ref))
  		return false;
  	return true;
  }

  @Override
  @JsonIgnore
  public String getReference() {
    return ref;
  }

  @Override
  public void setReference(String reference) {
    this.ref = reference;
  }
}