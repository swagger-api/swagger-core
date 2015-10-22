package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.NotFoundModel;
import io.swagger.models.Sample;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("/")
@Api(value = "/basic", description = "Basic resource")
@Produces({ "application/xml" })
public class SimpleResourceWithVendorAnnotation {

    @VendorFunnyAnnotation
    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get object by ID", notes = "No details provided", response = Sample.class, position = 0)
    @ApiResponses({ @ApiResponse(code = 400, message = "Invalid ID", response = NotFoundModel.class), @ApiResponse(code = 404, message = "object not found") })
    public Response getTest(
            @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]") @DefaultValue("5") @PathParam("id") final String id,
            @QueryParam("limit") final Integer limit) throws WebApplicationException {
        final Sample out = new Sample();
        out.setName("foo");
        out.setValue("bar");
        return Response.ok().entity(out).build();
    }

    @GET
    @Path("/{id}/value")
    @Produces({ "text/plain" })
    @ApiOperation(value = "Get simple string value", notes = "No details provided", response = String.class, position = 0)
    @ApiResponses({ @ApiResponse(code = 400, message = "Invalid ID", response = NotFoundModel.class), @ApiResponse(code = 404, message = "object not found") })
    public Response getStringValue() throws WebApplicationException {
        return Response.ok().entity("ok").build();
    }

    /**
     * Annotation processed by some vendor libraries. It could be used by swagger because the result of that processing
     * could return with rest error response.
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface VendorFunnyAnnotation {

    }
}
