package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.siblings.Pet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/pet")
@Produces({"application/json", "application/xml"})
public class SiblingPropResource {
  @PUT
  @Operation(summary = "Update an existing pet",
          tags = {"pet"},
          security = {
                @SecurityRequirement(name = "petstore_auth", scopes = {"write:pets", "read:pets"}),
                @SecurityRequirement(name = "mutual_tls", scopes = {})
          },
          responses = {
                  @ApiResponse(
                          responseCode = "200",
                          description = "Successful operation",
                          useReturnTypeSchema = true,
                          content = {
                                  @Content(
                                    mediaType = "application/xml",
                                    schema = @Schema(
                                            accessMode = Schema.AccessMode.READ_ONLY,
                                            description = "A Pet in XML Format"
                                    )
                                  ),
                                  @Content(
                                          mediaType = "application/json",
                                          schema = @Schema(
                                                  accessMode = Schema.AccessMode.READ_ONLY,
                                                  description = "A Pet in JSON Format"
                                          )
                                  )
                          }
                  ),
                  @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                  @ApiResponse(responseCode = "404", description = "Pet not found"),
                  @ApiResponse(responseCode = "405", description = "Validation exception") })
  @Consumes({"application/json", "application/xml"})
  public Pet updatePet(
      @RequestBody(
              description = "Pet object that needs to be updated in the store",
              required = true,
              useParameterTypeSchema = true,
              content = {
                      @Content(
                              mediaType = "application/json",
                              schema = @Schema(
                                      accessMode = Schema.AccessMode.WRITE_ONLY,
                                      description = "A Pet in JSON Format",
                                      requiredProperties = {"id"}
                              )
                      ),
                      @Content(
                              mediaType = "application/xml",
                              schema = @Schema(
                                      accessMode = Schema.AccessMode.WRITE_ONLY,
                                      description = "A Pet in XML Format",
                                      requiredProperties = {"id"}
                              )
                      )
              }
      ) Pet pet) {
    return null;
  }

}
