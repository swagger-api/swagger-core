package io.swagger.models.composition;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Class that has subtypes extending it and referencing it as parent = , without specifying subtypes =",
            parent = AbstractModelInheritanceOnRoot.class)
public class BaseModelWithNoSubTypesSpecified extends AbstractModelInheritanceOnRoot {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
