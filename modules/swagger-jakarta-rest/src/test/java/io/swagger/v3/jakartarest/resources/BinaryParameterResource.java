package io.swagger.v3.jakartarest.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import io.swagger.v3.jakartarest.resources.model.Item;
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
