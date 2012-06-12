package com.wordnik.swagger.test.jaxrs.apt;

import javax.ejb.Local;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiError;
import com.wordnik.swagger.annotations.ApiErrors;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;

/**
 * Another example of @Api* usage
 * @author Heiko W. Rupp
 */
@Api(value = "My important business EJB", basePath = "http://localhost:9999/other_path")
@Local
@Produces({"application/json","application/xml","text/html"})
@Path("/biz-ejb")
public interface SomeEjbInterface {

    @ApiOperation(value="Gives the current status", responseClass = "com.acme.MyResponse")
    @ApiErrors({
            @ApiError(code = 404,reason = "If there is no resource or group with the passed id "),
            @ApiError(code = 409,reason =" Resource type does not match the group one")
    })
    @GET
    @Path("/")
    Response getStatus(@Context HttpHeaders httpHeaders);
}
