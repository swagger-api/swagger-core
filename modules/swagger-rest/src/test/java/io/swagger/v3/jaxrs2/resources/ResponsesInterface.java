package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@ApiResponse(
        responseCode = "200",
        description = "voila!",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResponsesInterface.SampleResponseSchema.class)
        )
)
public interface ResponsesInterface {

    class SampleResponseSchema {
        @Schema(description = "the user id")
        private String id;
    }

    class GenericError {
        private int code;
        private String message;
    }

}
