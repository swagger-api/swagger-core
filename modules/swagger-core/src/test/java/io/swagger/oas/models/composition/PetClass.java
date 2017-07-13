package io.swagger.oas.models.composition;

import io.swagger.oas.annotations.media.Schema;

public class PetClass extends AnimalClass {
    private String name;
    private String type;
    private Boolean isDomestic;


    @Schema(
//            position = 1,
            required = true, description = "The pet type")
    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }

    @Schema(
//            position = 2,
            required = true, description = "The name of the pet")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Schema(
//            position = 3,
            required = true)
    public Boolean getIsDomestic() {
        return isDomestic;
    }

    public void setIsDomestic(Boolean isDomestic) {
        this.isDomestic = isDomestic;
    }
}
