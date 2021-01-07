package io.swagger.v3.jaxrs2.it.resources;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;

@Path("/files")
@Produces("application/json")
public class OctetStreamResource {
    @PUT
    @Path("/attach")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response putFile(@RequestBody(content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM, schema = @Schema(type = "string", format = "binary"))) InputStream fileInputStream) {
        return Response.status(200).entity("File has been attached").build();
    }
}
