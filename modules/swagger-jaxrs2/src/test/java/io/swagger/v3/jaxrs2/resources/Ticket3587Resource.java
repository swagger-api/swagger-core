package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("test")
public class Ticket3587Resource {

    @GET
    @Path("/test")
    public void parameterExamplesOrderingTest(
        @Parameter(
            in = ParameterIn.QUERY,
            examples = {
                @ExampleObject(
                    name = "Example One"
                ),
                @ExampleObject(
                    name = "Example Two"
                ),
                @ExampleObject(
                    name = "Example Three"
                )
            }
        )
            String parameterWithOrderedExamples,
        @Parameter(
            in = ParameterIn.QUERY,
            examples = {
                @ExampleObject(
                    name = "Example Three"
                ),
                @ExampleObject(
                    name = "Example Two"
                ),
                @ExampleObject(
                    name = "Example One"
                )
            }
        )
            String parameterWithExamplesInDifferentOrder) { }
}
