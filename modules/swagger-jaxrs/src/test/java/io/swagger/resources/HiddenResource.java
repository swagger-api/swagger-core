package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Api(value = "/external/info/", hidden = true)
@Path("fun")
public class HiddenResource {
    @GET
    @ApiOperation(value = "this", tags = "tag1")
    @Path("/this")
    public Response getThis(@ApiParam(value = "test") ArrayList<String> tenantId) {
        return Response.ok().build();
    }
}