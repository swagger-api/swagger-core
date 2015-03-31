package resources;

import models.Employee;
import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api(hidden = true)
public class Resource942SubResource {
  @ApiOperation(value = "gets all employees",
    response = Employee.class,
    responseContainer = "list",
    tags = "Employees")
  @Path("")
  public Response getEmployeesById() {
    return Response.ok().build();
  }
}