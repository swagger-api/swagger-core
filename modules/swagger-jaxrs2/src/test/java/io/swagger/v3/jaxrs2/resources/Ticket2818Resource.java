package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/bookstore")
public class Ticket2818Resource {

    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/{id}")
    @GET
    public Book getBook(
            @Parameter(
                    in = ParameterIn.PATH,
                    schema = @Schema (
                            type = "integer",
                            format = "int32"
                    )
            )
            @PathParam("id") int id) {
        return new Book();
    }


    public static class Book {
        public String foo;
    }
}