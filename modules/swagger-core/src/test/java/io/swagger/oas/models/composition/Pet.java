package io.swagger.oas.models.composition;

import io.swagger.annotations.media.OASSchema;

public interface Pet extends Animal {
    @OASSchema(
//            position = 1,
            required = true, description = "The pet type")
    String getType();

    void setType(String type);

    @OASSchema(
//            position = 2,
            required = true, description = "The name of the pet")
    String getName();

    void setName(String name);

    @OASSchema(
//            position = 3,
            required = true)
    Boolean getIsDomestic();

    void setIsDomestic(Boolean isDomestic);
}
