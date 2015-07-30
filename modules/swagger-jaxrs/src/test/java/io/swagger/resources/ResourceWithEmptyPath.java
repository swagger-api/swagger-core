package io.swagger.resources;

import io.swagger.annotations.Api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("")
@Api
public class ResourceWithEmptyPath {

    @GET
    public void getTest() {
    }
}
