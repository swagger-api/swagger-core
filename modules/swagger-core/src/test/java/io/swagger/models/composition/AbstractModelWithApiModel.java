package io.swagger.models.composition;

import io.swagger.annotations.ApiModel;

import com.fasterxml.jackson.annotation.JsonSubTypes;

@ApiModel("MyProperty")
@JsonSubTypes({@JsonSubTypes.Type(value = ModelWithUrlProperty.class), @JsonSubTypes.Type(value = ModelWithValueProperty.class)})
public abstract class AbstractModelWithApiModel {

    private final String type;

    protected AbstractModelWithApiModel(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
