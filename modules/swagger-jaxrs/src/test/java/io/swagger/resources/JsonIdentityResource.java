package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import io.swagger.models.ModelWithJsonIdentity;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/pet")
@Api(value = "/pet", description = "Operations about pets")
@Produces({"application/json", "application/xml"})
public class JsonIdentityResource {

    @POST
    @ApiOperation(value = "Add a single object")
    @ApiResponses(value = {@ApiResponse(code = 405, message = "Invalid input")})
    public Response test(
            @ApiParam(value = "Test", required = true) ModelWithJsonIdentity model) {
        return Response.ok().entity("SUCCESS").build();
    }
}
