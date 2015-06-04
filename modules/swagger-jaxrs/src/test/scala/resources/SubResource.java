package resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import models.Employee;

import javax.ws.rs.*;

@Api(hidden = true)
public class SubResource {
  @ApiOperation(value="gets an object by ID", tags = "Employees", response = Employee.class, responseContainer = "list")
  @GET
  public void getAllEmployees() {
    return;
  }

  @ApiOperation(value="gets an object by ID", tags = "Employees", response = Employee.class)
  @GET
  @Path("{id}")
  public Employee getSubresourceOperation(@ApiParam(value = "test") @PathParam("id") Long userId) {
    return null;
  }
}