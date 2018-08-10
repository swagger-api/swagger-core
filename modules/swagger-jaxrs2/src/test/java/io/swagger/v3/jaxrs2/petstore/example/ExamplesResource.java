package io.swagger.v3.jaxrs2.petstore.example;

import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Examples Resource Scenario
 */
public class ExamplesResource {
    @Path("/example")
    @POST
    @Operation(
            operationId = "subscribe",
            description = "subscribes a client to updates relevant to the requestor's account",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "subscriptionId", required = true,
                            schema = @Schema(name = "Schema", description = "Schema", example = "Subscription example"),
                            style = ParameterStyle.SIMPLE, example = "example",
                            examples = {
                                    @ExampleObject(name = "subscriptionId_1", value = "12345",
                                            summary = "Subscription number 12345", externalValue = "Subscription external value 1"),
                                    @ExampleObject(name = "subscriptionId_2", value = "54321",
                                            summary = "Subscription number 54321", externalValue = "Subscription external value 2")
                            })
            },
            responses = {
                    @ApiResponse(
                            description = "test description",
                            content = @Content(
                                    mediaType = "*/*",
                                    schema = @Schema(
                                            type = "string",
                                            format = "uuid",
                                            description = "the generated UUID",
                                            accessMode = Schema.AccessMode.READ_ONLY,
                                            example = "Schema example"
                                    ),
                                    examples = {
                                            @ExampleObject(name = "Default Response", value = "SubscriptionResponse",
                                                    summary = "Subscription Response Example", externalValue = "Subscription Response value 1")
                                    }
                            ))
            })
    public SubscriptionResponse subscribe(@RequestBody(description = "Created user object", required = true,
            content = @Content(
                    schema = @Schema(
                            type = "string",
                            format = "uuid",
                            description = "the generated UUID",
                            accessMode = Schema.AccessMode.READ_ONLY,
                            example = "Schema example"),
                    examples = {
                            @ExampleObject(name = "Default Request", value = "SubscriptionRequest",
                                    summary = "Subscription Request Example", externalValue = "Subscription Request Value")
                    })) User user) {
        return null;
    }
}