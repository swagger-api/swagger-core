package io.swagger.resources;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

public class SimpleMethods {
    @GET
    @Path("/int")
    @ApiOperation(value = "getInt")
    public int getInt() {
        return 0;
    }

    @GET
    @Path("/intArray")
    @ApiOperation(value = "getIntArray")
    public int[] getIntArray() {
        return new int[]{0};
    }

    @GET
    @Path("/string")
    @ApiOperation(value = "getStringArray")
    public String[] getStringArray() {
        return new String[]{};
    }

    @GET
    @Path("/stringArray")
    public void getWithIntArrayInput(@QueryParam("ids") int[] inputs) {
    }
}