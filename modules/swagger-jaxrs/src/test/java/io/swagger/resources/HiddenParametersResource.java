package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Api(value = "/basic", description = "Basic resource")
@Produces({"application/xml"})
@Path("/")
public class HiddenParametersResource {
    @GET
    @Path("/all-hidden/{id}")
    public Response allHiddenParamsMethod(
            @ApiParam(hidden = true) String body,
            @ApiParam(hidden = true) @CookieParam("X-your-cookie") String cookieId,
            @ApiParam(hidden = true) @FormParam("field") String formParam,
            @ApiParam(hidden = true) @HeaderParam("Authorization") String headerParam,
            @ApiParam(hidden = true) @PathParam("id") String pathParam,
            @ApiParam(hidden = true) @QueryParam("dateUpdated") java.util.Date dateUpdated) {
        return Response.ok().build();
    }

    @GET
    @Path("/some-hidden/{id}")
    public Response someHiddenParamsMethod(
            @ApiParam(hidden = true) String body,
            @ApiParam(hidden = false) @CookieParam("X-your-cookie") String cookieId,
            @ApiParam(hidden = true) @FormParam("field") String formParam,
            @ApiParam(hidden = false) @HeaderParam("Authorization") String headerParam,
            @ApiParam(hidden = true) @PathParam("id") String pathParam,
            @ApiParam(hidden = false) @QueryParam("dateUpdated") java.util.Date dateUpdated) {
        return Response.ok().build();
    }

    @GET
    @Path("/others-hidden/{id}")
    public Response othersHiddenParamsMethod(
            @ApiParam(hidden = false) String body,
            @ApiParam(hidden = true) @CookieParam("X-your-cookie") String cookieId,
            @ApiParam(hidden = false) @FormParam("field") String formParam,
            @ApiParam(hidden = true) @HeaderParam("Authorization") String headerParam,
            @ApiParam(hidden = false) @PathParam("id") String pathParam,
            @ApiParam(hidden = true) @QueryParam("dateUpdated") java.util.Date dateUpdated) {
        return Response.ok().build();
    }
}
