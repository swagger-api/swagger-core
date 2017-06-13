package org.myles.project.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.ArrayList;

@Api("/packageC")
@Path("/packageC")
public class ResourceInPackageC {
    @ApiOperation(value = "test.")
    @GET
    public void getTest(@ApiParam(value = "test") ArrayList<String> tenantId) {
        return;
    }
}