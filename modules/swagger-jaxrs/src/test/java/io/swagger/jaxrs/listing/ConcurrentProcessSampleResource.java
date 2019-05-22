package io.swagger.jaxrs.listing;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "foo")
@Path("foo")
public class ConcurrentProcessSampleResource {
    @GET
    @ApiOperation(value = "test of sample operation")
    @Path("/bar")
    public Response operationWithReadOnly(@ApiParam(value = "test") @QueryParam("sampleParam") String sampleParam) {
        return Response.ok().build();
    }

}
