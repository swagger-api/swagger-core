package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.model.JacksonBean;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("test")
@Produces("application/json")
public class ResourceWithJacksonBean {

    @POST
    @Path("/document/{documentName}.json")
    @Operation(operationId = "uploadAttachAndParseUserDocument", description = "Uploads, parses, and attaches the document to the user's job application.")
    public String uploadDocument(JacksonBean bean) throws Exception {
        return "";
    }
}