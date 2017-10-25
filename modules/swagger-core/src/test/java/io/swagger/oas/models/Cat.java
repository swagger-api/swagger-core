package io.swagger.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.oas.models.composition.Pet;

@Schema//(parent = Pet.class)
public interface Cat extends Pet {
    Integer getClawCount();

    void setClawCount(Integer name);
}