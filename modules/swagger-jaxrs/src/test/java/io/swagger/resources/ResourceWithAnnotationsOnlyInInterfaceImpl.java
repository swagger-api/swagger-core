package io.swagger.resources;

public class ResourceWithAnnotationsOnlyInInterfaceImpl implements ResourceWithAnnotationsOnlyInInterface {

    @Override
    public String getRandomPet() {
        return "No pet today..";
    }
}
