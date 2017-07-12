package io.swagger.oas.models.composition;

public class PetClass extends AnimalClass {
    private String name;
    private String type;
    private Boolean isDomestic;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Boolean getIsDomestic() {
        return isDomestic;
    }

    void setIsDomestic(Boolean isDomestic) {
        this.isDomestic = isDomestic;
    }
}
