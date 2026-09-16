package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.model.JacksonBean;
import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

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