package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("test")
@Produces(MediaType.APPLICATION_JSON)
@Api("test")
public class TestResource {
    @Path("/status")
    @GET
    @ApiOperation("Get status")
    public String getStatus() {
        return "{\"status\":\"OK!\"}";
    }

    @Path("/more")
    @ApiOperation("Get more")
    @Produces({MediaType.APPLICATION_XML})
    public TestSubResource getSubResource(
            @ApiParam("a query param") @QueryParam("qp") Integer qp) {
        return new TestSubResource();
    }
}