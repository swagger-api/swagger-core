package com.wordnik.swagger.sample.resource.issue606;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.sample.model.issue606.Employee;
import com.wordnik.swagger.sample.model.issue606.Link;

@Path("/hiddenemployees")
@Api(value = "hiddenemployees", description = "")
public class EmployeeResourceHidden extends EmployeeResourceWithLink
{
    @Override
    @GET
    @Path("/{empId}/manager")
    @ApiOperation(value = "Ask to talk to the employee's manager", notes = "You're a whiner!", response = Link.class, hidden = true)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid ID supplied"), @ApiResponse(code = 404, message = "Employee not found")})
    public Response getEmployeeManager( @ApiParam(value = "ID of employee", required = true) @PathParam("empId") String petId )
    {
        Link<Employee> le = new Link<Employee>();
        le.setHref("http://localhost/employees/1");
        le.setRel("manager");

        return Response.ok().entity(le).build();
    }
}
