package com.wordnik.swagger.models;

import com.wordnik.swagger.models.properties.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlType(propOrder = { "enum", "properties"})
@JsonPropertyOrder({ "enum", "properties"})
public class ModelImpl implements Model {
  private String name;
  private List<String> _enum;
  private Map<String, Property> properties;
  private boolean isSimple = false;
  private String description;

  public ModelImpl name(String name) {
    this.setName(name);
    return this;
  }
  public ModelImpl description(String description) {
    this.setDescription(description);
    return this;
  }
  public ModelImpl property(String key, Property property) {
    this.addProperty(key, property);
    return this;
  }

  @JsonIgnore
  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonIgnore
  public boolean isSimple() {
    return isSimple;
  }
  public void setSimple(boolean isSimple) {
    this.isSimple = isSimple;
  }

  public List<String> getEnum() {
    List<String> output = new ArrayList<String>();
    if(properties != null) {
      for(String key : properties.keySet()) {
        Property prop = properties.get(key);
        if(prop != null && prop.getRequired())
          output.add(key);
      }
    }
    if(output.size() > 0)
      return output;
    else
      return null;
  }
  public void setEnum(List<String> _enum) {
    this._enum = _enum;
  }

  public void addProperty(String key, Property property) {
    if(properties == null)
      properties = new LinkedHashMap<String, Property>();
    properties.put(key, property);    
  }

  public Map<String, Property> getProperties() {
    return properties;
  }
  public void setProperties(Map<String, Property> properties) {
    this.properties = properties;
  }
}
