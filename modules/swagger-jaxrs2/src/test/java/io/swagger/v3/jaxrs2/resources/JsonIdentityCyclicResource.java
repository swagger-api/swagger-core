package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.model.ModelWithJsonIdentityCyclic;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

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