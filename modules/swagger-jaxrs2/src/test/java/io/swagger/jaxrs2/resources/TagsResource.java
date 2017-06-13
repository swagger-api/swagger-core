package io.swagger.jaxrs2.resources;

import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.servers.Server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by RafaelLopez on 5/19/17.
 */
public class TagsResource {

    @GET
    @Path("/")
    @Operation(tags = {"Example Tag", "Second Tag"})
    public Response getTags() {
        return Response.ok().entity("ok").build();
    }
}
