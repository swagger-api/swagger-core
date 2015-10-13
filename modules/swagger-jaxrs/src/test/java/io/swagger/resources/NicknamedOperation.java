package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Api(value = "/external/info/")
@Path("external/info/")
public class NicknamedOperation {
    @ApiOperation(value = "test", nickname = "getMyNicknameTest")
    @GET
    public Response.Status getTest(@ApiParam(value = "test") ArrayList<String> tenantId) {
        return Response.Status.OK;
    }
}
