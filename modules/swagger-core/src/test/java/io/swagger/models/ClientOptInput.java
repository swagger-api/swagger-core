package io.swagger.models;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.media.OASSchema;

public class ClientOptInput {
    private String opts;
    private JsonNode model;
    private OpenAPI swagger;

    public String getOpts() {
        return opts;
    }

    public void setOpts(String opts) {
        this.opts = opts;
    }

    @OASSchema//(hidden = true)
    public JsonNode getModel() {
        return model;
    }

    public void setModel(JsonNode model) {
        this.model = model;
    }

    @OASSchema(type = "Object")
    public OpenAPI getSwagger() {
        return swagger;
    }

    public void setSwagger(OpenAPI swagger) {
        this.swagger = swagger;
    }
}