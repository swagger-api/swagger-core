package resources;

import models.Employee;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

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