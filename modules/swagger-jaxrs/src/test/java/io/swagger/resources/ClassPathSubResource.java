package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Api
@Path("/subresource")
public class ClassPathSubResource {
    @GET
    @ApiOperation(value = "getCodeExtended")
    public String getCode(){
        return "400";
    }
}
