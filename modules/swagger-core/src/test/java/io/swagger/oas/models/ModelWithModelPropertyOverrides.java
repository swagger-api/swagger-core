package io.swagger.oas.models;

import io.swagger.annotations.media.OASSchema;

public class ModelWithModelPropertyOverrides {
    @OASSchema(type = "List[Children]")
    private String children;

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }
}
