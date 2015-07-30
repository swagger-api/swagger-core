package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.Employee;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Api("/test")
@Path("/test")
public class Resource942 {
    @ApiOperation(value = "test")
    @POST
    public void addTest(@ApiParam(required = false) Employee employee) {
        return;
    }

    @GET
    public Resource942SubResource getSub() {
        return new Resource942SubResource();
    }
}