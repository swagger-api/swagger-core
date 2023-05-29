package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("test")
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource {
    @Path("/status")
    @GET
    @Operation(description = "Get status")
    public String getStatus() {
        return "{\"status\":\"OK!\"}";
    }

    @Path("/more")
    @Operation(description = "Get more")
    @Produces({MediaType.APPLICATION_XML})
    public StatusSubResource getSubResource(
            @QueryParam("qp") Integer qp) {
        return new StatusSubResource();
    }
}
