package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Api()
public class SubResourceTail {

    @ApiOperation(value = "Returns greeting")
    @GET
    @Path("/hello")
    public String getGreeting() {
        return "Hello!";
    }

    @ApiOperation(value = "Echoes passed string")
    @GET
    @Path("{string}")
    public String getEcho(@PathParam("string") String string) {
        return string;
    }
}
