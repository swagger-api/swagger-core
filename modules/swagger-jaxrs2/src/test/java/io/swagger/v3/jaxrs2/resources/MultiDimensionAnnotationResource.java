package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class MultiDimensionAnnotationResource {

    @GET
    @Path("/test")
    @ApiResponse(
            description = "test action",
            content = @Content(

                    mediaType = "application/json",
                    array = @ArraySchema(
                            dimension = 5,
                            schema = @Schema(
                                    implementation = Number.class
                            )
                    )
            ))
    public Response getter() {
        return null;
    }


}
