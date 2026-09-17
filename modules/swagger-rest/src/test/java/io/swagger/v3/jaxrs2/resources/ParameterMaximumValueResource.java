package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/test")
public class ParameterMaximumValueResource {

    @GET
    @Path("/{petId}")
    public Response getPetById(
            @Parameter(
                    description = "ID of pet that needs to be fetched",
                    schema = @Schema(
                            type = "integer",
                            format = "int64",
                            exclusiveMinimumValue = 1,
                            exclusiveMaximumValue = 10
                    ),
                    required = true)
            @PathParam("petId") Long petId) {
        return null;
    }
}
