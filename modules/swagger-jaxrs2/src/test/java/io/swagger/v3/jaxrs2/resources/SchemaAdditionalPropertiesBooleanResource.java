package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


public class SchemaAdditionalPropertiesBooleanResource {

    @Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
    static class Pet {
        @Schema(additionalPropertiesSchema = Bar.class)
        public Bar bar;

        @Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
        public Bar vbar;
    }


    static class Bar {
        public String foo;
    }

    @Schema(description = "A car")
    static class Car {
        public String foo;
    }

    @GET
    @Path("/test")
    public Pet test() {
        return null;
    }

}
