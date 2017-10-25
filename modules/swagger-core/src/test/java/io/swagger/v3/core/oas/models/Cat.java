package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.core.oas.models.composition.Pet;

@Schema//(parent = Pet.class)
public interface Cat extends Pet {
    Integer getClawCount();

    void setClawCount(Integer name);
}