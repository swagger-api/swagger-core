package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import io.swagger.models.NotFoundModel;
import io.swagger.models.Sample;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Api
@Path("/")
public class ResourceWithApiResponseResponseContainer {
    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get object by ID",
            notes = "No details provided",
            response = Sample.class,
            responseContainer = "map",
            code = 200,
            position = 0,
            responseHeaders = {
                    @ResponseHeader(name = "foo", description = "description", response = String.class, responseContainer = "list")
            })
    @ApiResponses({
            @ApiResponse(code = 400, message = "Invalid ID",
                    response = NotFoundModel.class,
                    responseContainer = "list",
                    responseHeaders = @ResponseHeader(name = "X-Rack-Cache", description = "Explains whether or not a cache was used", response = Boolean.class)),
            @ApiResponse(code = 404, message = "object not found")})
    public Response getTest() {
        return Response.ok().entity("out").build();
    }

    @PUT
    @Path("/{id}")
    @ApiOperation(value = "Get object by ID",
            notes = "No details provided",
            response = Sample.class,
            position = 0,
            code = 201,
            responseHeaders = {
                    @ResponseHeader(name = "foo", description = "description", response = String.class, responseContainer = "list")
            })
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized",
                    response = NotFoundModel.class,
                    responseContainer = "list",
                    responseHeaders = @ResponseHeader(name = "X-Rack-Cache", description = "Explains whether or not a cache was used", response = Boolean.class)),
            @ApiResponse(code = 405, message = "Method Not Allowed")})
    public Response putTest() {
        return Response.ok().entity("out").build();
    }

    @POST
    @Path("/{id}")
    @ApiOperation(value = "Get object by ID",
            notes = "No details provided",
            response = Sample.class,
            position = 0,
            code = 202,
            responseHeaders = {
                    @ResponseHeader(name = "foo", description = "description", response = String.class, responseContainer = "list")
            })
    @ApiResponses({
            @ApiResponse(code = 402, message = "Invalid ID",
                    response = NotFoundModel.class,
                    responseHeaders = @ResponseHeader(name = "X-Rack-Cache", description = "Explains whether or not a cache was used", response = Boolean.class)),
            @ApiResponse(code = 406, message = "Method Not Allowed")})
    public Response postTest() {
        return Response.ok().entity("out").build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Get object by ID",
            notes = "No details provided",
            response = Sample.class,
            responseContainer = "other",
            position = 0,
            code = 203,
            responseHeaders = {
                    @ResponseHeader(name = "foo", description = "description", response = String.class, responseContainer = "list")
            })
    @ApiResponses({
            @ApiResponse(code = 403, message = "Forbidden",
                    response = NotFoundModel.class,
                    responseContainer = "wrongValue",
                    responseHeaders = @ResponseHeader(name = "X-Rack-Cache", description = "Explains whether or not a cache was used", response = Boolean.class)),
            @ApiResponse(code = 407, message = "Proxy Authentication Required")})
    public Response deleteTest() {
        return Response.ok().entity("out").build();
    }

    @GET
    @Path("/{id}/name")
    @ApiOperation(value = "Get object by ID",
            notes = "No details provided",
            response = Sample.class,
            responseContainer = "array",
            position = 0,
            code = 203,
            responseHeaders = {
                    @ResponseHeader(name = "foo", description = "description", response = String.class, responseContainer = "list")
            })
    @ApiResponses({
            @ApiResponse(code = 403, message = "Forbidden",
                    response = NotFoundModel.class,
                    responseContainer = "set",
                    responseHeaders = @ResponseHeader(name = "X-Rack-Cache", description = "Explains whether or not a cache was used", response = Boolean.class))})
    public Response getNameTest() {
        return Response.ok().entity("out").build();
    }

    @PUT
    @Path("/{id}/name")
    @ApiOperation(value = "Get object by ID",
            notes = "No details provided",
            response = Sample.class,
            responseContainer = "set",
            position = 0,
            code = 203,
            responseHeaders = {
                    @ResponseHeader(name = "foo", description = "description", response = String.class, responseContainer = "set")
            })
    @ApiResponses({
            @ApiResponse(code = 403, message = "Forbidden",
                    response = NotFoundModel.class,
                    responseContainer = "array",
                    responseHeaders = @ResponseHeader(name = "X-Rack-Cache", description = "Explains whether or not a cache was used", response = Boolean.class))})
    public Response putNameTest() {
        return Response.ok().entity("out").build();
    }
}
