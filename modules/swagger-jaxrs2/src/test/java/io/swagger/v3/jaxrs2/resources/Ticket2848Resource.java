package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/")
public class Ticket2848Resource {
    @GET
    public Town getter() {
        return null;
    }

    public static class Town {
        @ArraySchema( schema = @Schema(required = true), minItems = 1,uniqueItems = true )
        public List<String> streets;
    }



}
