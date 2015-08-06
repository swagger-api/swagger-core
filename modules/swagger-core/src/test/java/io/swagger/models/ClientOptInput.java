package io.swagger.models;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModelProperty;

public class ClientOptInput {
    private String opts;
    private JsonNode model;
    private Swagger swagger;

    public String getOpts() {
        return opts;
    }

    public void setOpts(String opts) {
        this.opts = opts;
    }

    @ApiModelProperty(hidden = true)
    public JsonNode getModel() {
        return model;
    }

    public void setModel(JsonNode model) {
        this.model = model;
    }

    @ApiModelProperty(dataType = "Object")
    public Swagger getSwagger() {
        return swagger;
    }

    public void setSwagger(Swagger swagger) {
        this.swagger = swagger;
    }
}