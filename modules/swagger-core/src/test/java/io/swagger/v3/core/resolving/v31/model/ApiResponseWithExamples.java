package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

/**
 * Demonstrates WORKAROUND 2: Using @ExampleObject at operation/media-type level
 * instead of schema-level examples.
 * 
 * This provides multiple named examples that appear in Swagger UI dropdown,
 * with proper JSON structure for each example.
 */
public class ApiResponseWithExamples {

    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    /**
     * Example operation showing workaround: Use @Content.examples with @ExampleObject
     * This gives you multiple examples with proper JSON structure at operation level.
     */
    @Operation(
            summary = "Get data with multiple examples",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response with multiple example arrays",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWithExamples.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example 1: Primary colors",
                                                    description = "An example with primary colors",
                                                    value = "{\"data\": [\"red\", \"blue\", \"yellow\"]}"
                                            ),
                                            @ExampleObject(
                                                    name = "Example 2: Secondary colors",
                                                    description = "An example with secondary colors",
                                                    value = "{\"data\": [\"green\", \"orange\", \"purple\"]}"
                                            ),
                                            @ExampleObject(
                                                    name = "Example 3: Numbers",
                                                    description = "An example with number strings",
                                                    value = "{\"data\": [\"one\", \"two\", \"three\"]}"
                                            )
                                    }
                            )
                    )
            }
    )
    public ApiResponseWithExamples getDataOperation() {
        return this;
    }
}
