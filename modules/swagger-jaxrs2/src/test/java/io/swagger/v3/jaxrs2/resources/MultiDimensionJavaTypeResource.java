package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

public class MultiDimensionJavaTypeResource {

    @GET
    @Path("/test")
    @ApiResponse(
            description = "test action"
    )
    @Produces("application/json")
    public Number[][][][][] getter() {
        return null;
    }

}
