package resources;

import models.Employee;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

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
}