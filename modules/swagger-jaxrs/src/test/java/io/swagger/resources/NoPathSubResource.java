package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;

/**
 * The {@code NoPathSubResource} class defines test sub-resource without
 * {@link javax.ws.rs.Path} annotations.
 */
@Api()
public class NoPathSubResource {

    @ApiOperation(value = "Returns greeting")
    @GET
    public String getGreeting() {
        return "Hello!";
    }
}
