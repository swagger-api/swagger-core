package io.swagger.v3.jaxrs2.petstore.operation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

/**
 * Interface resource
 */
public interface InterfaceResource {
    @GET
    @Path("/interfaceoperation/{petId}")
    @Operation(summary = "Find pet by ID Operation in Parent",
            description = "Returns a pet in Parent"
    )
    Response getPetById(@Parameter(description = "ID of pet that needs to be fetched", required = true)
                        @PathParam("petId")final Long petId);
}
