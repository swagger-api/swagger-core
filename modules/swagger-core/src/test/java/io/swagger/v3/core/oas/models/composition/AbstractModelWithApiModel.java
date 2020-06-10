package io.swagger.v3.core.oas.models.composition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MyProperty")
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
