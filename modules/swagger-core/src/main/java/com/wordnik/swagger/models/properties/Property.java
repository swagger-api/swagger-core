package com.wordnik.swagger.models.properties;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(  
    use = JsonTypeInfo.Id.NAME,  
    include = JsonTypeInfo.As.PROPERTY,  
    property = "type")
@JsonSubTypes({  
    @Type(value = ArrayProperty.class, name = "array"),
    @Type(value = BooleanProperty.class, name = "boolean"),
    @Type(value = DoubleProperty.class, name = "number"),
    @Type(value = IntegerProperty.class, name = "integer"),
    @Type(value = StringProperty.class, name = "string"),
    @Type(value = RefProperty.class, name = "ref") })
public interface Property {
  String getType();
  String getFormat();
}