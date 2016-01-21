package com.subresourcesTest;

import io.swagger.annotations.Api;

import javax.ws.rs.Path;

/**
 * Created by rbolles on 1/20/16.
 */

@Api(tags = "root")
@Path("/api/1.0")
public class RootResource {

    @Path("/children")
    public ChildResource getChildResource() {
        return new ChildResource();
    }

}
