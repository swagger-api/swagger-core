package com.wordnik.swagger.sample.resource.issue606;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.sample.model.issue606.Department;
import com.wordnik.swagger.sample.model.issue606.Employee;
import com.wordnik.swagger.sample.model.issue606.Link;

@Path("/linkedemployees")
@Api(value = "linkedemployees", description = "")
public class EmployeeResourceWithLink extends EmployeeResource
{
    @GET
    @Path("/{empId}/manager")
    @ApiOperation(value = "Ask to talk to the employee's manager", notes = "You're a whiner!", response = Link.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid ID supplied"), @ApiResponse(code = 404, message = "Employee not found")})
    public Response getEmployeeManager( @ApiParam(value = "ID of employee", required = true) @PathParam("empId") String petId )
    {
        Link<Employee> le = new Link<Employee>();
        le.setHref("http://localhost/employees/1");
        le.setRel("manager");

        return Response.ok().entity(le).build();
    }

    @Override
    @GET
    @Path("/{empId}")
    @ApiOperation(value = "Find employee by ID", notes = "There's only one employee in this company!", response = Employee.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid ID supplied"), @ApiResponse(code = 404, message = "Employee not found")})
    public Response getEmployeeById( @ApiParam(value = "ID of employee that needs to be fetched", required = true) @PathParam("empId") String petId )
    {
        Employee e = new Employee();
        e.setId(10);
        e.setFirstName("Kaiser");
        e.setLastName("Soze");
        Link<Department> dept = new Link<Department>();
        dept.setHref("http://localhost/departments/1");
        dept.setRel("department");
        e.setDept(dept);

        Link<Employee> le = new Link<Employee>();
        le.setHref("http://localhost/employees/1");
        le.setRel("manager");
        e.setManager(le);

        Set<Link<Employee>> subs = new HashSet<Link<Employee>>();

        le = new Link<Employee>();
        le.setHref("http://localhost/employees/11");
        le.setRel("team member");
        subs.add(le);

        le = new Link<Employee>();
        le.setHref("http://localhost/employees/12");
        le.setRel("team member");
        subs.add(le);

        le = new Link<Employee>();
        le.setHref("http://localhost/employees/99");
        le.setRel("team member");
        subs.add(le);

        return Response.ok().entity(e).build();

    }
}
