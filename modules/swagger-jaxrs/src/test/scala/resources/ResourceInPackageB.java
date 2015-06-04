package org.my.project.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.*;

import java.util.*;

@Api("/packageB")
@Path("/packageB")
public class ResourceInPackageB {
  @ApiOperation(value="test.")
  @GET
  public void getTest(@ApiParam(value = "test") ArrayList<String> tenantId) {
    return;
  }
}