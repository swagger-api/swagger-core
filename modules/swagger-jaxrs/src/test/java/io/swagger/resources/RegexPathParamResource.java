package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Api(value = "/external/info/")
@Path("/")
public class RegexPathParamResource {
    @GET
    @ApiOperation(value = "this", tags = "tag1")
    @Path("/{report_type:[aA-zZ]+}")
    public Response getThis(@ApiParam(value = "test") @PathParam("report_type") String param) {
        return Response.ok().build();
    }
}
