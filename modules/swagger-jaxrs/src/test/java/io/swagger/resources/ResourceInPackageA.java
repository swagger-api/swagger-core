package com.my.project.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.ArrayList;

@Api("/packageA")
@Path("/packageA")
public class ResourceInPackageA {
    @ApiOperation(value = "test.")
    @GET
    public void getTest(@ApiParam(value = "test") ArrayList<String> tenantId) {
        return;
    }
}