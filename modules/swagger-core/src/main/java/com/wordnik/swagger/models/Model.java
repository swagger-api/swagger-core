package com.wordnik.swagger.models;

import com.wordnik.swagger.models.properties.*;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlType(propOrder = { "enum", "properties"})
@JsonPropertyOrder({ "enum", "properties"})
public class Model {
  List<String> _enum;
  Map<String, Property> properties;

  public List<String> getEnum() {
    return _enum;
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
