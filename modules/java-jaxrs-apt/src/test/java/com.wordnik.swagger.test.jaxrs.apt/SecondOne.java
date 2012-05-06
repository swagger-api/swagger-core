package com.wordnik.swagger.test.jaxrs.apt;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.ApiError;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Just an example class
 * @author Heiko W. Rupp
 */
@Path("bla")
public interface SecondOne {

    @Path("bla")
    @PUT
    @ApiError(code=404,reason = "Resource with the passed key not found")
    public Response putSomeData(
            @ApiParam("The primary key") @PathParam("id")int id,
            @ApiParam("The data to put") FooBean bean);

}
