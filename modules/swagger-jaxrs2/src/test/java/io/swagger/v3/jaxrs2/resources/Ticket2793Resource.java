package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.UUID;

@OpenAPIDefinition
@Path("distances")
public interface Ticket2793Resource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = DistancesResponse.class)))
    })
    Response getDistances();

    abstract class DistancesResponse implements Map<UUID, Map<RouteMetric, Integer>> { }

    public static class RouteMetric {
        public String foo;
    }
}
