package io.swagger.oas.models;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.oas.annotations.media.Schema;

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

    // TODO
    @Schema//(hidden = true)
    public JsonNode getModel() {
        return model;
    }

    public void setModel(JsonNode model) {
        this.model = model;
    }

    @Schema(type = "Object")
    public OpenAPI getSwagger() {
        return swagger;
    }

    public void setSwagger(OpenAPI swagger) {
        this.swagger = swagger;
    }
}