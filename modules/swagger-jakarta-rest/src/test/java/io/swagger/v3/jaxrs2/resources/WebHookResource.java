package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.siblings.Pet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Webhook;
import io.swagger.v3.oas.annotations.Webhooks;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Webhooks({
        @Webhook(
                name = "newPet",
                operation = @Operation(
                        requestBody = @RequestBody(
                                description = "Information about a new pet in the system",
                                content = {
                                        @Content(
                                                mediaType = "application/json",
                                                schema = @Schema(
                                                        description = "Webhook Pet",
                                                        implementation = Pet.class
                                                )
                                        )
                                }
                        ),
                        method = "post",
                        responses = @ApiResponse(
                                responseCode = "200",
                                description = "Return a 200 status to indicate that the data was received successfully"
                        )
                )
        )
})
public class WebHookResource {
}
