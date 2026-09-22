package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/search")
public class QueryStringParamResource {

    @GET
    @Produces("application/json")
    @Operation(operationId = "search", summary = "Query string search")
    public Response search(
            @Parameter(in = ParameterIn.QUERYSTRING, name = "q",
                    content = @Content(schema = @Schema(implementation = String.class)))
            String query) {
        return Response.ok().build();
    }
}
