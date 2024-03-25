package io.swagger.v3.jaxrs2.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import io.swagger.v3.jaxrs2.resources.model.Item;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/")
public class BinaryParameterResource {
    @Consumes({ MediaType.APPLICATION_JSON })
    @Path("/binary")
    @POST
    @Operation(
        summary = "Create new item",
        description = "Post operation with entity in a body",
        responses = {
            @ApiResponse(
                content = @Content(
                    schema = @Schema(implementation = Item.class), 
                    mediaType = MediaType.APPLICATION_JSON
                ),
                headers = @Header(name = "Location"),
                responseCode = "201"
            )
        }
    )
    public Response createItem(@Context final UriInfo uriInfo, @Parameter(required = true) final Item item) {
        return Response
            .created(uriInfo.getBaseUriBuilder().path(item.getName()).build())
            .entity(item).build();
    }

}
