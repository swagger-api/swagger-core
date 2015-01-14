package models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "is_persistent", "gettersAndHaters" })
public class ModelPropertyName {
  public boolean is_persistent() { return true; }
  public String gettersAndHaters() { return null; }
}