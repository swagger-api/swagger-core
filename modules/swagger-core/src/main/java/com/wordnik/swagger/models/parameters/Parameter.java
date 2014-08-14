package com.wordnik.swagger.models.parameters;

import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(  
    use = JsonTypeInfo.Id.NAME,  
    include = JsonTypeInfo.As.PROPERTY,  
    property = "in")  
@JsonSubTypes({  
    @Type(value = BodyParameter.class, name = "body"),  
    @Type(value = HeaderParameter.class, name = "header"),  
    @Type(value = PathParameter.class, name = "path"),  
    @Type(value = QueryParameter.class, name = "query"),  
    @Type(value = CookieParameter.class, name = "cookie") }) 
public interface Parameter {
  void setName(String name);
  String getIn();
  String getName();
  String getDescription();
  boolean getRequired();
  void setDescription(String description);
}