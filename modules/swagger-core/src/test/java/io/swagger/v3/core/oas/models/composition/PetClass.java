package io.swagger.v3.core.oas.models.composition;

import io.swagger.v3.oas.annotations.media.Schema;

public class PetClass extends AnimalClass {
    private String name;
    private String type;
    private Boolean isDomestic;

    @Schema(required = true, description = "The pet type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Schema(required = true, description = "The name of the pet")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Schema(required = true)
    public Boolean getIsDomestic() {
        return isDomestic;
    }

    public void setIsDomestic(Boolean isDomestic) {
        this.isDomestic = isDomestic;
    }
}
