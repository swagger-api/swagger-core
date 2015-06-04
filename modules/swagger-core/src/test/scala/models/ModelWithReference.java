package models;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel( reference = "http://swagger.io/schemas.json#/Models")
public class ModelWithReference {

    @ApiModelProperty( reference = "http://swagger.io/schemas.json#/Models/Description")
    public String getDescription() {
        return "Swagger";
    }
}
