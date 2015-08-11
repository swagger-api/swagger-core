package io.swagger.models;

import io.swagger.annotations.ApiModel;
import io.swagger.models.composition.Pet;

@ApiModel(parent = Pet.class)
public interface Cat extends Pet {
    Integer getClawCount();

    void setClawCount(Integer name);
}