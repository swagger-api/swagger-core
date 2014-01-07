package com.wordnik.swagger.sample.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
@Api(value="/it", description = "This doesn't work as the call path is generated from this value. " +
        "It's not an info only field")
//@Api(value="/") //This won't work as Swagger UI can't see nor call it
public class SimpleResource {
    @GET
    @ApiOperation(value = "Can't touch this")
    public String it() {
        return "it";
    }
}
