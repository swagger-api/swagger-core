package com.splitresourcesTestImpl;

import javax.ws.rs.core.Response;

import com.splitresourcesTest.SplitResource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Created by KangoV on 2016-05-22
 * #1800
 */
@Api(tags = "children")
public class SplitResourceImpl implements SplitResource {

    @ApiOperation(value = "Get Child by id")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    public Response getChildren() {
        return Response.ok().entity("Hello World").build();
    }
    
}
