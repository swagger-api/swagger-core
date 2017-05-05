package io.swagger.models;

import io.swagger.annotations.media.OASSchema;
import io.swagger.models.composition.Pet;

@OASSchema//(parent = Pet.class)
public interface Cat extends Pet {
    Integer getClawCount();

    void setClawCount(Integer name);
}