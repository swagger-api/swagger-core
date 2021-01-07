package io.swagger.v3.jaxrs2.it.resources;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.glassfish.jersey.media.multipart.FormDataParam;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;

@Path("/files")
@Produces("application/json")
public class MultiPartFileResource {
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("fileIdRenamed") final String fileId,
                               @Parameter(schema = @Schema(type = "string", format = "binary")) @FormDataParam("fileRenamed") final InputStream file) {
        return Response.status(200).entity("File  " + fileId + " has been uploaded").build();
    }
}
