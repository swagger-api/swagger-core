package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("/test")
public class Ticket3731BisResource {
    @GET
    @Path("/cart")
    @Operation(
            summary = "Get cart items",
            description = "Paging follows RFC 5005.")
    public List<String> getCart(
            @QueryParam("pageSize")
            @DefaultValue("50")
            @Parameter(description = "Number of items per page. Range[1, 200]") //
            @Min(2)
            @Max(202)
            @Schema(minimum = "1", maximum = "200")
            final int pageSize) {
        return null;
    }
}
