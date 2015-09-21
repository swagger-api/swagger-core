package io.swagger.resources;

import io.swagger.annotations.ApiParam;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

public class BaseResource {
    @ApiParam("The Identifier of entity")
    @PathParam("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ApiParam("Base description")
    @PathParam("description")
    private String description = "base";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ApiParam(value = "Test Query Param")
    @QueryParam("test")
    protected boolean test;
}
