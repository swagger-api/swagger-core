package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Employee;

import javax.ws.rs.Path;

@Api("/employees")
@Path("/employees")
public class ResourceWithSubResources {
    @ApiOperation(value = "gets all employees",
            response = Employee.class,
            responseContainer = "list",
            tags = "Employees")
    @Path("{id}")
    public SubResource getTest() {
        return new SubResource();
    }

    @Path("noPath")
    @ApiOperation(value = "Returns sub-resource without @Path")
    public NoPathSubResource getNoPathTest() {
        return new NoPathSubResource();
    }
}
