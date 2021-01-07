package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;

@Path("/notnullparameter")
public class Ticket2794Resource
{
    @Produces({ MediaType.APPLICATION_JSON })
    @GET
    public Response getBooks(
            @QueryParam("page") @NotNull int page) {
        return Response.ok(
                Arrays.asList(
                        new Book(),
                        new Book()
                )
        ).build();
    }

    @Path("/new_reqBody_required")
    @POST
    public Response insert(@RequestBody(required = true) Book book) {
        return Response.ok().build();
    }

    @Path("/newnotnull")
    @POST
    public Response insertnotnull(@NotNull Book book) {
        return Response.ok().build();
    }

    public static class Book {
        public String foo;
    }
}