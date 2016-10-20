package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/v1")
@Api(value = "root")
public class Resource1970 {
    @GET
    @Path("/{dbkey}")
    @ApiOperation(value = "Retrieve a database resource")
    @ApiImplicitParams({@ApiImplicitParam(name = "param1", dataType = "java.math.BigDecimal", paramType = "path", required = true)})
    public void numberInput() throws Exception {
        return;
    }
}
