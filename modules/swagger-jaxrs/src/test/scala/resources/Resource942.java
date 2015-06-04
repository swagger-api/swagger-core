package resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import models.Employee;

import javax.ws.rs.*;

@Api("/test")
@Path("/test")
public class Resource942 {
  @ApiOperation(value="test")
  @POST
  public void addTest(@ApiParam(required=false) Employee employee) {
    return;
  }

  @GET
  public Resource942SubResource getSub() {
    return new Resource942SubResource();
  }
}