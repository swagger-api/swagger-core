package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

public class APIResponsesResource {
    @POST
    @Path("postStringOrEmailSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    public void postStringOrEmailSchemaContent() { }

    @POST
    @Path("postBooleanSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Boolean.class))
            )
    })
    public void postBooleanSchemaContent() { }

    @POST
    @Path("postByteOrBinarySchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Byte.class))
            )
    })
    public void postByteOrBinarySchemaContent() { }

    @POST
    @Path("postURISchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = URI.class))
            )
    })
    public void postURISchemaContent() { }

    @POST
    @Path("postURLSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = URL.class))
            )
    })
    public void postURLSchemaContent() { }

    @POST
    @Path("postUUIDSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = UUID.class))
            )
    })
    public void postUUIDSchemaContent() { }

    @POST
    @Path("postIntegerSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Integer.class))
            )
    })
    public void postIntegerSchemaContent() { }

    @POST
    @Path("postLongSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Long.class))
            )
    })
    public void postLongSchemaContent() { }

    @POST
    @Path("postFloatSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Float.class))
            )
    })
    public void postFloatSchemaContent() { }

    @POST
    @Path("postDoubleSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Double.class))
            )
    })
    public void postDoubleSchemaContent() { }

    @POST
    @Path("postBigIntegerSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BigInteger.class))
            )
    })
    public void postBigIntegerSchemaContent() { }

    @POST
    @Path("postBigDecimalSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BigDecimal.class))
            )
    })
    public void postBigDecimalSchemaContent() { }

    @POST
    @Path("postNumberSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Number.class))
            )
    })
    public void postNumberSchemaContent() { }

    @POST
    @Path("postDateStubSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PrimitiveType.DateStub.class))
            )
    })
    public void postDateStubSchemaContent() { }

    @POST
    @Path("postDateSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Date.class))
            )
    })
    public void postDateSchemaContent() { }

    @POST
    @Path("postLocalTimeSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = LocalTime.class))
            )
    })
    public void postLocalTimeSchemaContent() { }

    @POST
    @Path("postFileSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = File.class))
            )
    })
    public void postFileSchemaContent() { }

    @POST
    @Path("postObjectSchemaContent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Object.class))
            )
    })
    public void postObjectSchemaContent() { }
}
