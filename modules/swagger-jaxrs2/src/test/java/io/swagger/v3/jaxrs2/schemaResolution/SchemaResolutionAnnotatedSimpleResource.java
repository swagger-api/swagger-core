package io.swagger.v3.jaxrs2.schemaResolution;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("test")
public class SchemaResolutionAnnotatedSimpleResource {

    @GET
    @Path("/inlineSchemaFirst")
    public InlineSchemaFirst inlineSchemaFirst() {
        return null;
    }


    static class InlineSchemaFirst {

        private InlineSchemaPropertyFirst property2;

        @Schema(description = " InlineSchemaFirst property 2", example = "example 2", schemaResolution = Schema.SchemaResolution.INLINE)
        public InlineSchemaPropertyFirst getProperty2() {
            return null;
        }
    }

    @Schema(description = "property", example = "example")
    static class InlineSchemaPropertyFirst {
        // public String bar;
    }
}
