package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Api()
@Path("/")
public class Resource469 {
    @Path("/test")
    @ApiOperation(value = "test")
    @GET
    public void getTest(
            @ApiParam(value = "A password", format = "password")
            @QueryParam("password") String password) {
        return;
    }

    @Path("/test")
    @ApiOperation(value = "test")
    @POST
    public void postTest(
            @ApiParam(value = "A password", format = "int64")
            @QueryParam("count") Integer count) {
        return;
    }

    @Path("/test")
    @ApiOperation(value = "test")
    @PUT
    public void putTest(
            @ApiParam(value = "A count that accepts strings for parsing safety", type="string", format="blah")
            @QueryParam("count") Integer count) {
        return;
    }

    @Path("/test2")
    @ApiOperation(value = "test2")
    @GET
    @ApiImplicitParams({
        @ApiImplicitParam(name = "password", paramType = "query", value="ok", type = "string", format = "password")
    })
    public void getTest2() {
        return;
    }
}
