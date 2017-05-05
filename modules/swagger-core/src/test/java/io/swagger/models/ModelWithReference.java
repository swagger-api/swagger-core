package io.swagger.models;

import io.swagger.annotations.media.OASSchema;

@OASSchema(description = "http://swagger.io/schemas.json#/Models")
public class ModelWithReference {

    @OASSchema(description = "http://swagger.io/schemas.json#/Models/Description")
    public String getDescription() {
        return "Swagger";
    }
}
