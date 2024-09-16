package io.swagger.v3.jaxrs2.schemaResolution;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("test")
public class SchemaResolutionResourceSimple {

    @GET
    @Path("/inlineSchemaFirst")
    public InlineSchemaFirst inlineSchemaFirst() {
        return null;
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
