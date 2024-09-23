package io.swagger.v3.jaxrs2.schemaResolution;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("test")
public class SchemaResolutionResource {

    @GET
    @Path("/inlineSchemaSecond")
    public InlineSchemaSecond inlineSchemaSecond(@Schema(description = "InlineSchemaSecond API") InlineSchemaSecond inlineSchemaSecond) {
        return null;
    }
    @GET
    @Path("/inlineSchemaFirst")
    public InlineSchemaFirst inlineSchemaFirst() {
        return null;
    }


    static class InlineSchemaFirst {

        // public String foo;

        @Schema(description = "InlineSchemaFirst property 1", nullable = true)
        public InlineSchemaPropertyFirst property1;


        private InlineSchemaPropertyFirst property2;

        @Schema(description = " InlineSchemaFirst property 2", example = "example 2")
        public InlineSchemaPropertyFirst getProperty2() {
            return null;
        }
    }

    static class InlineSchemaSecond {

        public String foo;

        @Schema(description = "InlineSchemaSecond property 1", nullable = true)
        public InlineSchemaPropertySecond propertySecond1;


        private InlineSchemaPropertyFirst property2;

        @Schema(description = "InlineSchemaSecond property 2", example = "InlineSchemaSecond example 2")
        public InlineSchemaPropertyFirst getProperty2() {
            return null;
        }
    }

    @Schema(description = "property", example = "example")
    static class InlineSchemaPropertyFirst {
        public String bar;
    }

    @Schema(description = "propertysecond", example = "examplesecond")
    static class InlineSchemaPropertySecond {
        public InlineSchemaSimple bar;
    }

    static class InlineSchemaSimple {

        @Schema(description = "property 1")
        public InlineSchemaPropertySimple property1;


        private InlineSchemaPropertySimple property2;

        @Schema(description = "property 2", example = "example")
        public InlineSchemaPropertySimple getProperty2() {
            return null;
        }
    }

    @Schema(description = "property")
    static class InlineSchemaPropertySimple {
        public String bar;
    }
}
