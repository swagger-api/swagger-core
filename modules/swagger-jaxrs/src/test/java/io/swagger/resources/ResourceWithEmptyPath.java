package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("")
@Api
public class ResourceWithEmptyPath {

    @GET
    @ApiOperation(value = "get operation")
    public void getTest() {
    }
}
