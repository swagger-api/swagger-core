package com.my.sorted.resources;

import io.swagger.v3.jaxrs2.resources.model.Pet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.ArrayList;

@Path("/sorted")
public class SortedThing {

    @Operation(operationId = "foo")
    @GET
    @Path("/pet")
    public Pet foo() {
        return null;
    }

    @Operation(operationId = "bar")
    @GET
    @Path("/pet")
    public Pet bar() {
        return null;
    }

}
