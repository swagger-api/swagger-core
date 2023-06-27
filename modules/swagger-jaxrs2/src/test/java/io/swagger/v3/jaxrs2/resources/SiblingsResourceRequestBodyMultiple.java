package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.siblings.PetSimple;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/test")
public class SiblingsResourceRequestBodyMultiple {
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
                                                    accessMode = Schema.AccessMode.WRITE_ONLY)),
                                    @Content(
                                            mediaType = "application/xml",
                                            schema = @Schema(
                                                    description = "resource pet xml",
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
                            mediaType = "application/json",
                            schema = @Schema(
                                    description = "resource pet",
                                    implementation = PetSimple.class,
                                    accessMode = Schema.AccessMode.WRITE_ONLY)),
                    @Content(
                            mediaType = "application/xml",
                            schema = @Schema(
                                    description = "resource pet xml",
                                    implementation = PetSimple.class,
                                    accessMode = Schema.AccessMode.WRITE_ONLY))
            }) Object body) {
        return null;
    }

    @Path("/bodyparam")
    @GET
    public Response getBodyParam(
            @RequestBody(
                    description = "test",
                    useParameterTypeSchema = true,
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            description = "resource pet",
                                            accessMode = Schema.AccessMode.WRITE_ONLY)),
                            @Content(
                                    mediaType = "application/xml",
                                    schema = @Schema(
                                            description = "resource pet xml",
                                            accessMode = Schema.AccessMode.WRITE_ONLY))
                    }
            ) PetSimple pet) {
        return null;
    }
}
