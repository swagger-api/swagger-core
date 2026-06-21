package io.swagger.v3.jakartarest.resources;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

public class Ticket2806Resource {

    @GET
    @Path("/test")
    public Test getTest() {
        return null;
    }

    class Test {
        private String[] stringArray;

        @ArraySchema(
                minItems = 2,
                maxItems = 4,
                uniqueItems = true,
                arraySchema = @Schema(
                        description = "Array desc",
                        example = "[\"aaa\", \"bbb\"]"
                ),
                schema = @Schema(
                        description = "Hello, World!",
                        example = "Lorem ipsum dolor set"
                )
        )
        public void setStringArray(String[] value) { stringArray = value; }
        public String[] getStringArray() { return stringArray; }
    }
}
