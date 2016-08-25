package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.Sub1Bean;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Api
@Path("/childType")
public interface ResourceWithChildType {

    @GET
    @Path("/testChildTypeResponse")
    @ApiOperation("Tests child type response schema ref is correctly set up")
    @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Sub1Bean.class)})
    Response testChildTypeResponse();

    @GET
    @Path("/testChildTypeResponseOnOperation")
    @ApiOperation(value = "Tests child type response schema ref is correctly set up when specified on the operation", response = Sub1Bean.class)
    Response testChildTypeResponseOnOperation();

    @POST
    @Path("/testChildTypeParameter")
    @ApiOperation("Tests schema ref is correctly set up for child type parameter")
    void testChildTypeParameter(@ApiParam(value = "A child type parameter", required = true) Sub1Bean testParam);

}
