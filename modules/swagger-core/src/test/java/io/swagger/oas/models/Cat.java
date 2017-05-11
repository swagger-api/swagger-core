package io.swagger.oas.models;

import io.swagger.annotations.media.OASSchema;
import io.swagger.oas.models.composition.Pet;

@OASSchema//(parent = Pet.class)
public interface Cat extends Pet {
    Integer getClawCount();

    void setClawCount(Integer name);
}