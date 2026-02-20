package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@Path("/test")
public class Ticket4859Resource {

    @PUT
    @Path("/minlength")
    public void minlength(Minlength minlength) {}

    public static class Minlength {
        @Schema(example = "4242424242424242", minLength = 12, maxLength = 19, required = true)
        public String name;
    }
}
