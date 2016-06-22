package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.JacksonBean;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("test")
@Api(value = "test", description = "test routes", produces = "application/json")
public class ResourceWithJacksonBean {

    @POST
    @Path("/document/{documentName}.json")
    @ApiOperation(value = "uploadAttachAndParseUserDocument", notes = "Uploads, parses, and attaches the document to the user's job application.", position = 509)
    public String uploadDocument(JacksonBean bean) throws Exception {
        return "";
    }
}
