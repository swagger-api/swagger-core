package io.swagger.models.composition;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Class that has a field that is the AbstractBaseModelWithSubTypes",
        parent = BaseModelWithNoSubTypesSpecified.class)
public class ModelWithParentOnly extends BaseModelWithNoSubTypesSpecified {

    private String childProperty;

    private int id;

    public String getChildProperty() {
        return childProperty;
    }

    public void setChildProperty(String childProperty) {
        this.childProperty = childProperty;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
