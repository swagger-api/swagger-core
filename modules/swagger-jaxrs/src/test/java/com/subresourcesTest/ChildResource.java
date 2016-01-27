package com.subresourcesTest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

/**
 * Created by rbolles on 1/20/16.
 */
@Api(tags = "children", description = "operations about children")
public class ChildResource {

    @GET
    @ApiOperation(value = "Get Child by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public Response getChildren(
    ) {
        return Response.ok().entity("Hello World").build();
    }
}
