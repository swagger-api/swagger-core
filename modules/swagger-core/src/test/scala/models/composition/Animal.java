package models.composition;

import com.wordnik.swagger.annotations.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
  property = "type")
@JsonSubTypes({  
  @Type(value = Human.class, name = "human"),
  @Type(value = Pet.class, name = "pet")
})
public interface Animal {
  String getName();
  void setName(String name);

  String getType();
  void setType(String type);
}
