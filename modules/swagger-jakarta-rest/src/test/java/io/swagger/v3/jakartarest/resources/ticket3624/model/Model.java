package io.swagger.v3.jakartarest.resources.ticket3624.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;


public abstract class Model {
    // this is the ID of the review
    public abstract int getId();

    public abstract String getText();

    public abstract String getTitle();

    public abstract boolean isActive();

    public abstract Optional<String> getOptionalString();

    public abstract Optional<Model> getParent();

    @Schema
    public abstract Optional<Model> getSchemaParent();

}
