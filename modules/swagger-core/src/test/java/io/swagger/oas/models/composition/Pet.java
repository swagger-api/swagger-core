package io.swagger.oas.models.composition;

import io.swagger.oas.annotations.media.Schema;

public interface Pet extends Animal {
    @Schema(
//            position = 1,
            required = true, description = "The pet type")
    String getType();

    void setType(String type);

    @Schema(
//            position = 2,
            required = true, description = "The name of the pet")
    String getName();

    void setName(String name);

    @Schema(
//            position = 3,
            required = true)
    Boolean getIsDomestic();

    void setIsDomestic(Boolean isDomestic);
}
