package io.swagger.v3.core.oas.models;

import io.swagger.v3.core.oas.models.composition.Pet;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema//(parent = Pet.class)
public interface Cat extends Pet {
    Integer getClawCount();

    void setClawCount(Integer name);
}