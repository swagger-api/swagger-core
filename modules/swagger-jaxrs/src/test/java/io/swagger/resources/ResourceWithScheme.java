package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("test")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "test", protocols = "http")
public class ResourceWithScheme {
    @Path("/status")
    @GET
    @ApiOperation(value = "Get status", protocols = "https")
    public String getStatus() {
        return "{\"status\":\"OK!\"}";
    }

    @Path("/value")
    @GET
    @ApiOperation(value = "Get value", protocols = "ws, wss")
    public String getValue() {
        return "{\"value\":\"OK!\"}";
    }

    @Path("/notes")
    @GET
    @ApiOperation(value = "Get notes", protocols = "ftp")
    public String getNotes() {
        return "{\"notes\":\"OK!\"}";
    }

    @Path("/description")
    @GET
    @ApiOperation(value = "Get description")
    public String getDescription() {
        return "{\"description\":\"OK!\"}";
    }
}