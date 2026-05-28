package io.swagger.v3.jaxrs2.schemaResolution;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("test")
public class SchemaResolutionResourceSimple {


    @GET
    @Path("/inlineSchemaFirst")
    @ApiResponse(description = "InlineSchemaFirst Response API", content = @Content(schema = @Schema(implementation = InlineSchemaFirst.class)))
    public Response inlineSchemaFirst() {
        return null;
    }


    @GET
    @Path("/inlineSchemaSecond")
    public void inlineSchemaFirst(@Schema(description = "InlineSchemaSecond API") InlineSchemaFirst inlineSchemaFirst) {
    }



    static class InlineSchemaFirst {

        // public String foo;

        @Schema(description = "InlineSchemaFirst property 1", nullable = true)
        public InlineSchemaPropertyFirst property1;
    }

    @Schema(description = "property", example = "example")
    static class InlineSchemaPropertyFirst {
        public String bar;
    }
}
