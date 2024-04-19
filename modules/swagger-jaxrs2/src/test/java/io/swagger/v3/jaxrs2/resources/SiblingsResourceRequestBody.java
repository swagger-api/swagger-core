package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.siblings.PetSimple;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/test")
public class SiblingsResourceRequestBody {
    @Path("/bodyimpl")
    @GET
    @Operation(
            requestBody =
                    @RequestBody(
                            description = "aaa",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    description = "resource pet",
                                                    implementation = PetSimple.class,
                                                    accessMode = Schema.AccessMode.WRITE_ONLY))
                            }))
    public Response getBodyImpl(Object body) {
        return null;
    }

    @Path("/bodyimplparam")
    @GET
    public Response getBodyImplParam(@RequestBody(
            content = {
                    @Content(
                            mediaType = "*/*",
                            schema = @Schema(
                                    description = "resource pet",
                                    implementation = PetSimple.class,
                                    accessMode = Schema.AccessMode.WRITE_ONLY))
            }) Object body) {
        return null;
    }
}
