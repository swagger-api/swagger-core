package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.NotFoundModel;
import io.swagger.models.Pagination;
import io.swagger.models.TestBeanParam;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import java.util.HashMap;
import java.util.Map;

@Api(value = "/basic", description = "Basic resource")
@Path("/")
public class ResourceWithBeanParams {
    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get object by ID",
            notes = "No details provided"/*,
    response = String.class,
    responseContainer = "Map"*/)
    @ApiResponses({
            @ApiResponse(code = 400, message = "Invalid ID", response = NotFoundModel.class),
            @ApiResponse(code = 404, message = "object not found")})
    public Map<String, Integer> getTest(
            @BeanParam Pagination pagination
    ) throws WebApplicationException {
        return new HashMap<String, Integer>();
    }

    @GET
    @Path("/bean/{id}")
    @ApiOperation(value = "Get test object by ID",
            notes = "No details provided")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Invalid ID", response = NotFoundModel.class),
            @ApiResponse(code = 404, message = "object not found")})
    public Map<String, Integer> getTestBeanParams(@BeanParam TestBeanParam params, @DefaultValue("bodyParam") String
            testBody) throws
            WebApplicationException {
        return new HashMap<String, Integer>();
    }
}
