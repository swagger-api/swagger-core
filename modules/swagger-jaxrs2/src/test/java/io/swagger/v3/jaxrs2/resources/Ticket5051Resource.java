package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/ticket5051")
@Produces(MediaType.APPLICATION_JSON)
public class Ticket5051Resource {

    @GET
    @Path("/items")
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    array = @ArraySchema(
                            arraySchema = @Schema(description = "Array description"),
                            schema = @Schema(description = "Item description", implementation = Item.class)
                    )
            )
    )
    public List<Item> items() {
        return null;
    }

    public static class Item {
        public String value;
    }
}
