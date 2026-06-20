package io.swagger.v3.java17.resources;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("test")
public class SchemaResolutionWithRecordsResource {

    @GET
    @Path("/inlineSchemaSecond")
    public InlineSchemaRecordSecond inlineSchemaSecond(@Schema(description = "InlineSchemaSecond API") InlineSchemaRecordSecond inlineSchemaSecond) {
        return null;
    }

    @GET
    @Path("/inlineSchemaFirst")
    public InlineSchemaRecordFirst inlineSchemaFirst() {
        return null;
    }


    public record InlineSchemaRecordFirst(
            InlineSchemaPropertyFirst property1,
            InlineSchemaPropertyFirst property2
    ){
    }

    public record InlineSchemaRecordSecond (
            String foo,
            @Schema(description = "InlineSchemaSecond property 1", nullable = true)
            InlineSchemaPropertySecond propertySecond1,
            @Schema(description = "InlineSchemaSecond property 2", nullable = true)
            InlineSchemaPropertyFirst property2
    ){
    }

    @Schema(description = "property", example = "example")
    static class InlineSchemaPropertyFirst {
        public String bar;
    }

    @Schema(description = "propertySecond", example = "exampleSecond")
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
