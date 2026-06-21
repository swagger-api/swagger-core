package io.swagger.v3.java17.resources;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("test")
public class SchemaResolutionWithRecordSimpleResource{


    @GET
    @Path("/inlineSchemaFirst")
    @ApiResponse(description = "InlineSchemaFirst Response API", content = @Content(schema = @Schema(implementation = SchemaRecordFirst.class)))
    public Response inlineSchemaFirst() {
        return null;
    }


    @GET
    @Path("/inlineSchemaSecond")
    public void inlineSchemaFirst(@Schema(description = "InlineSchemaSecond API") SchemaRecordFirst inlineSchemaFirst) {
    }



    public record SchemaRecordFirst (
            @Schema(description = "InlineSchemaFirst property 1", nullable = true) InlineSchemaPropertyFirst property1
    ){
    }

    @Schema(description = "property", example = "example")
    static class InlineSchemaPropertyFirst {
        public String bar;
    }
}
