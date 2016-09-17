package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/pet")
@Api(tags = "someTag")
public interface ResourceWithAnnotationsOnlyInInterface {

    @GET
    @Path("/randomPet")
    @ApiOperation(value = "getRandomPet")
    @ApiImplicitParams({ @ApiImplicitParam(name = "petImplicitIdParam", paramType = "query", dataType = "string") })
    String getRandomPet();
}
