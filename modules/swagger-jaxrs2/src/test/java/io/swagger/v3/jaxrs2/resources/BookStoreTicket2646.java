package io.swagger.v3.jaxrs2.resources;


import java.util.Arrays;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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