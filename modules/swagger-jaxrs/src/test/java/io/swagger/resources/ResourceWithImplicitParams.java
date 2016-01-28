package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Api
@Path("/")
public class ResourceWithImplicitParams {

    @POST
    @Path("/testString")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", paramType = "query", dataType = "string", required = false, value = "Comma-delimited list of fields to sort by."),
            @ApiImplicitParam(name = "type", paramType = "path", dataType = "string", allowableValues = "one,two,three"),
            @ApiImplicitParam(name = "size", paramType = "header", dataType = "int", allowableValues = "range[1,infinity]"),
            @ApiImplicitParam(name = "width", paramType = "form", dataType = "int", allowableValues = "range[infinity,1]"),
            @ApiImplicitParam(name = "width", paramType = "formData", dataType = "int", allowableValues = "range[infinity,1]"),
            @ApiImplicitParam(name = "height", paramType = "query", dataType = "int", allowableValues = "range[3,4]"),
            @ApiImplicitParam(name = "body", paramType = "body", dataType = "string", required = true),
            @ApiImplicitParam(name = "width", paramType = "unknown")
    })
    @ApiOperation("Test operation with implicit parameters")
    public void testString() {
    }
}
