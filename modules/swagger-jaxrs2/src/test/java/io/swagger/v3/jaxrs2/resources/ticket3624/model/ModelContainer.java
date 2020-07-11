package io.swagger.v3.jaxrs2.resources.ticket3624.model;


import java.util.Optional;


public abstract class ModelContainer {
    public abstract Optional<Model> getModel();
    public abstract int getId();
    public abstract String getText();
}
