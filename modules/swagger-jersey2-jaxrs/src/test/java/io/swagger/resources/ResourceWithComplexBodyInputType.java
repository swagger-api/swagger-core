package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Path("myapi")
@Api(value = "myapi", description = "myapi routes", produces = "application/json")
public class ResourceWithComplexBodyInputType {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("testPostWithBody")
    @ApiOperation(response = String.class, value = "Returns string with provided parameter")
    public String testPostWithBody(@ApiParam(name = "body", value = "input parameters in json form")
                                   List<ClassWithString> items) {

        return String.format("Given parameters are %s", Arrays.toString(new String[0]));
    }

}