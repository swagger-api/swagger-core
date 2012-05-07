package com.wordnik.swagger.test.jaxrs.apt;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * // TODO: Document this
 * @author Heiko W. Rupp
 */
@Api(value="My Foobar api", description = "This api does foo and bar and baz and is uber cool")
@Path("/foo")
public interface MyBean {

    @GET
    @Path("/one")
    @ApiOperation("This is the first method")
    public String methodOne();

    @PUT
    @Path("/two/{pp}")
    @ApiOperation("This is the second method")
    public void methodTwo(
            @ApiParam(value="The customer id") @PathParam("pp") @DefaultValue("42") int pp,
            @ApiParam(value="Hulla",required = false) @QueryParam("qp") String qp);
}
