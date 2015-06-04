package resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import models.Employee;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("/employees")
@Path("/employees")
public class ResourceWithSubResources {
  @ApiOperation(value = "gets all employees",
    response = Employee.class,
    responseContainer = "list",
    tags = "Employees")
  @GET
  public SubResource getTest() {
    return new SubResource();
  }

  @Path("noPath")
  @ApiOperation(value="Returns sub-resource without @Path")
  public NoPathSubResource getNoPathTest() {
    return new NoPathSubResource();
  }
}
