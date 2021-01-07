package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.model.ModelWithJsonIdentityCyclic;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/pet")
@Produces({"application/json", "application/xml"})
public class JsonIdentityCyclicResource {

    @POST
    @Operation(description = "Add a single object")
    public Response test(
            @Parameter(required = true) ModelWithJsonIdentityCyclic model) {
        return Response.ok().entity("SUCCESS").build();
    }
}
