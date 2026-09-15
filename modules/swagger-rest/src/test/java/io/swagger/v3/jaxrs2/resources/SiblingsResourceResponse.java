package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.siblings.PetSimple;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class SiblingsResourceResponse {
    @GET
    @Operation(
            responses = {
                    @ApiResponse(
                            responseCode = "300",
                            description = "aaa",
                            useReturnTypeSchema = true,
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    description = "resource pet",
                                                    accessMode = Schema.AccessMode.READ_ONLY)),
                                    @Content(
                                            mediaType = "application/xml",
                                            schema = @Schema(
                                                    description = "resource pet xml",
                                                    accessMode = Schema.AccessMode.READ_ONLY))
                            })})
    public PetSimple getCart() {
        return null;
    }

    @Path("/impl")
    @GET
    @Operation(
            responses = {
                    @ApiResponse(
                            responseCode = "300",
                            description = "aaa",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    description = "resource pet",
                                                    implementation = PetSimple.class,
                                                    accessMode = Schema.AccessMode.READ_ONLY)),
                                    @Content(
                                            mediaType = "application/xml",
                                            schema = @Schema(
                                                    description = "resource pet xml",
                                                    implementation = PetSimple.class,
                                                    accessMode = Schema.AccessMode.READ_ONLY))
                            })})
    public PetSimple getCartImpl() {
        return null;
    }

}
