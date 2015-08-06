package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Employee;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

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