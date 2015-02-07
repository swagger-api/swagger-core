package models;

import com.wordnik.swagger.annotations.*;
import com.wordnik.swagger.models.Swagger;

import com.fasterxml.jackson.databind.JsonNode;

public class ClientOptInput {
  private String opts;
  private JsonNode model;
  private Swagger swagger;


  public void setOpts(String opts) {
    this.opts = opts;
  }

  public String getOpts() {
    return opts;
  }

  public void setModel(JsonNode model) {
    this.model = model;
  }

  @ApiModelProperty(hidden = true)
  public JsonNode getModel() {
    return model;
  }
  public void setSwagger(Swagger swagger) {
    this.swagger = swagger;
  }

  @ApiModelProperty(dataType="Object")
  public Swagger getSwagger() {
    return swagger;
  }
}