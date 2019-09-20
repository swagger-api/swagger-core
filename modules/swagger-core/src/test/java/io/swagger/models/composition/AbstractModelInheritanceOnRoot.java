package io.swagger.models.composition;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Abstract class to be position at the root, should be added to definitions")
public abstract class AbstractModelInheritanceOnRoot {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
