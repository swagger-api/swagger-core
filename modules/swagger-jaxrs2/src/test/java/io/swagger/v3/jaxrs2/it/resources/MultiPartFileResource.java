package io.swagger.v3.jaxrs2.it.resources;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
