package models;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class ModelContainingModelWithReference {
    public ModelWithReference getModel(){
        return null;
    }

    @ApiModelProperty( reference = "http://swagger.io/schemas.json#/Models/AnotherModel")
    public ModelWithReference getAnotherModel(){
        return null;
    }
}
