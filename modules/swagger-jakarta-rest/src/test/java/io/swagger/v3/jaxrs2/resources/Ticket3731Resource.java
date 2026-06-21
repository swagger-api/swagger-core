package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.util.List;

@Path("/test")
public class Ticket3731Resource {
    @GET
    @Path("/cart")
    @Operation(
            summary = "Get cart items",
            description = "Paging follows RFC 5005.")
    public List<String> getCart(
            @QueryParam("pageSize")
            @DefaultValue("50")
            @Min(1) @Max(200)
            @Parameter(description = "Number of items per page. Range[1, 200]") //
            final int pageSize) {
        return null;
    }
}
