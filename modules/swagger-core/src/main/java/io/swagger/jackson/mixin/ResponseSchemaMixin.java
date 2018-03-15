package io.swagger.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;

public abstract class ResponseSchemaMixin {

    @JsonIgnore
    public abstract Property getSchema();

    @JsonIgnore
    public abstract void setSchema(Property schema);

    @JsonGetter("schema")
    public abstract Model getResponseSchema();

    @JsonSetter("schema")
    public abstract void setResponseSchema(Model schema);



}
