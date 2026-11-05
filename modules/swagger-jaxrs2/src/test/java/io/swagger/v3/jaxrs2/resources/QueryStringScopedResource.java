package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/ctorsearch")
public class QueryStringScopedResource {

    @QueryParam("fq")
    @Parameter(in = ParameterIn.QUERYSTRING, name = "fq",
            content = @Content(schema = @Schema(implementation = String.class)))
    private String fieldQuery;

    public QueryStringScopedResource(
            @QueryParam("cq")
            @Parameter(in = ParameterIn.QUERYSTRING, name = "cq",
                    content = @Content(schema = @Schema(implementation = String.class)))
            String ctorQuery) {
    }

    @GET
    @Produces("application/json")
    @Operation(operationId = "search", summary = "Scoped query string search")
    public Response search() {
        return Response.ok().build();
    }
}
