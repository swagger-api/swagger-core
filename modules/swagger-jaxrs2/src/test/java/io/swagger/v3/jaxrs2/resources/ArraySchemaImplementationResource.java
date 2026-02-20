package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

public class ArraySchemaImplementationResource {

    static class Pet {
        @ArraySchema(schema = @Schema(implementation = Integer.class, description = "A house in a street"))
        public List<Number> cars;
    }

    @GET
    @Path("/test")
    public Pet test() {
        return null;
    }

}
