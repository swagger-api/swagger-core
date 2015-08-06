package io.swagger.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(reference = "http://swagger.io/schemas.json#/Models")
public class ModelWithReference {

    @ApiModelProperty(reference = "http://swagger.io/schemas.json#/Models/Description")
    public String getDescription() {
        return "Swagger";
    }

    @ApiModelProperty(value = "SubModelWithSelfReference")
    public List<SubModelWithSelfReference> nested;
}
