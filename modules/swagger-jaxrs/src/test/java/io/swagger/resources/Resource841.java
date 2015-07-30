package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Api(value = "/external/info/")
@Path("fun")
public class Resource841 {
    @ApiOperation(value = "this", tags = "tag1")
    @GET
    @Path("/this")
    public Response getThis(@ApiParam(value = "test") ArrayList<String> tenantId) {
        return Response.ok().build();
    }

    @ApiOperation(value = "that", tags = "tag2")
    @GET
    @Path("/that")
    public Response getThat(@ApiParam(value = "test") ArrayList<String> tenantId) {
        return Response.ok().build();
    }

    @ApiOperation(value = "everything", tags = {"tag1", "tag2"})
    @GET
    public Response getEverything(@ApiParam(value = "test") ArrayList<String> tenantId) {
        return Response.ok().build();
    }
}