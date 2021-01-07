package io.swagger.v3.jaxrs2.resources;


import java.util.Arrays;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/bookstore")
public class BookStoreTicket2646 {
    @Produces({ MediaType.APPLICATION_JSON })
    @GET
    public Response getBooks(
            @QueryParam("page") @DefaultValue("1") int page) {
        return Response.ok(
                Arrays.asList(
                        new Book(),
                        new Book()
                )
        ).build();
    }

    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/{id}")
    @GET
    public Book getBook(@PathParam("id") Long id) {
        return new Book();
    }

    @Path("/{id}")
    @DELETE
    public Response delete(@PathParam("id") String id) {
        return Response.ok().build();
    }

    public static class Book {
        public String foo;
    }
}