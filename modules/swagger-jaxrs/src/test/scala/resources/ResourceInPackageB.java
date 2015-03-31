package org.my.project.resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

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