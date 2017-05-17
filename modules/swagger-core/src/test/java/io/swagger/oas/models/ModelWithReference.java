package io.swagger.oas.models;

import io.swagger.oas.annotations.media.Schema;

@Schema(description = "http://swagger.io/schemas.json#/Models")
public class ModelWithReference {

    @Schema(description = "http://swagger.io/schemas.json#/Models/Description")
    public String getDescription() {
        return "Swagger";
    }
}
