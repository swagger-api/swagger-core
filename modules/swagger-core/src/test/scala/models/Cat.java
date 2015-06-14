package models;

import io.swagger.annotations.ApiModel;
import models.composition.Pet;

@ApiModel(parent = Pet.class)
public interface Cat extends Pet {
    Integer getClawCount();

    void setClawCount(Integer name);
}