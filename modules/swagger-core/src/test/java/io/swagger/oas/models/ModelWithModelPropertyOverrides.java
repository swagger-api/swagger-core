package io.swagger.oas.models;

import io.swagger.oas.annotations.media.ArraySchema;
import io.swagger.oas.annotations.media.Schema;

public class ModelWithModelPropertyOverrides {
    @ArraySchema(schema = @Schema(implementation = Children.class))
    private String children;

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }
}
