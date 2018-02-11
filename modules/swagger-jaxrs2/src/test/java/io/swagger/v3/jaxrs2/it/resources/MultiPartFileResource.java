package io.swagger.v3.jaxrs2.it.resources;

import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/files")
public class MultiPartFileResource {
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("fileId") final String fileId,
                               @FormDataParam("file") final InputStream file) {
        return Response.status(200).entity("File  " + fileId + " has been uploaded").build();
    }
}
