package io.swagger.v3.jaxrs2.annotations.requests;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.jaxrs2.resources.model.Pet;
import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.IOException;


public class RequestBodyTest extends AbstractAnnotationTest {

    @Test(description = "Returns a request with one RequestBody and multiple parameters")
    public void oneRequestBodyMultipleParameters() throws IOException {

        String expectedYAML = "openapi: 3.0.0\n" +
                "paths:\n" +
                "  /user:\n" +
                "    put:\n" +
                "      summary: Modify user\n" +
                "      description: Modifying user.\n" +
                "      operationId: methodWithRequestBodyWithoutAnnotation\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "    post:\n" +
                "      summary: Create user\n" +
                "      description: This can only be done by the logged in user.\n" +
                "      operationId: methodWithRequestBodyAndTwoParameters\n" +
                "      parameters:\n" +
                "      - name: name\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      - name: code\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      requestBody:\n" +
                "        description: Created user object\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "    delete:\n" +
                "      summary: Delete user\n" +
                "      description: This can only be done by the logged in user.\n" +
                "      operationId: methodWithoutRequestBodyAndTwoParameters\n" +
                "      parameters:\n" +
                "      - name: name\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      - name: code\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "  /pet:\n" +
                "    put:\n" +
                "      summary: Modify pet\n" +
                "      description: Modifying pet.\n" +
                "      operationId: methodWithRequestBodyWithoutAnnotationAndTwoConsumes\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "    post:\n" +
                "      summary: Create pet\n" +
                "      description: Creating pet.\n" +
                "      operationId: methodWithTwoRequestBodyWithoutAnnotationAndTwoConsumes\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/Pet'\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/Pet'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "    delete:\n" +
                "        summary: Delete pet\n" +
                "        description: Deleting pet.\n" +
                "        operationId: methodWithOneSimpleRequestBody\n" +
                "        requestBody:\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: integer\n" +
                "                format: int32\n" +
                "            application/xml:\n" +
                "              schema:\n" +
                "                type: integer\n" +
                "                format: int32\n" +
                "        responses:\n" +
                "          default:\n" +
                "            description: default response\n" +
                "components:\n" +
                "  schemas:\n" +
                "    User:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        username:\n" +
                "          type: string\n" +
                "        firstName:\n" +
                "          type: string\n" +
                "        lastName:\n" +
                "          type: string\n" +
                "        email:\n" +
                "          type: string\n" +
                "        password:\n" +
                "          type: string\n" +
                "        phone:\n" +
                "          type: string\n" +
                "        userStatus:\n" +
                "          type: integer\n" +
                "          description: User Status\n" +
                "          format: int32\n" +
                "      xml:\n" +
                "        name: User\n" +
                "    Category:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        name:\n" +
                "          type: string\n" +
                "      xml:\n" +
                "        name: Category\n" +
                "    Tag:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        name:\n" +
                "          type: string\n" +
                "      xml:\n" +
                "        name: Tag\n" +
                "    Pet:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        category:\n" +
                "          $ref: '#/components/schemas/Category'\n" +
                "        name:\n" +
                "          type: string\n" +
                "        photoUrls:\n" +
                "          type: array\n" +
                "          xml:\n" +
                "            wrapped: true\n" +
                "          items:\n" +
                "            type: string\n" +
                "            xml:\n" +
                "              name: photoUrl\n" +
                "        tags:\n" +
                "          type: array\n" +
                "          xml:\n" +
                "            wrapped: true\n" +
                "          items:\n" +
                "            $ref: '#/components/schemas/Tag'\n" +
                "        status:\n" +
                "          type: string\n" +
                "          description: pet status in the store\n" +
                "          enum:\n" +
                "          - available,pending,sold\n" +
                "      xml:\n" +
                "        name: Pet";

        compareAsYaml(RequestBodyTest.UserResource.class, expectedYAML);
    }

    static class UserResource {
        @POST
        @Path("/user")
        @Operation(summary = "Create user",
                description = "This can only be done by the logged in user.")
        public Response methodWithRequestBodyAndTwoParameters(
                @RequestBody(description = "Created user object", required = true,
                        content = @Content(
                                schema = @Schema(implementation = User.class))) User user,
                @QueryParam("name") String name, @QueryParam("code") String code) {
            return Response.ok().entity("").build();
        }

        @PUT
        @Path("/user")
        @Operation(summary = "Modify user",
                description = "Modifying user.")
        public Response methodWithRequestBodyWithoutAnnotation(
                User user) {
            return Response.ok().entity("").build();
        }

        @DELETE
        @Path("/user")
        @Operation(summary = "Delete user",
                description = "This can only be done by the logged in user.")
        public Response methodWithoutRequestBodyAndTwoParameters(
                @QueryParam("name") String name, @QueryParam("code") String code) {
            return Response.ok().entity("").build();
        }

        @PUT
        @Path("/pet")
        @Operation(summary = "Modify pet",
                description = "Modifying pet.")
        @Consumes({"application/json", "application/xml"})
        public Response methodWithRequestBodyWithoutAnnotationAndTwoConsumes(
                User user) {
            return Response.ok().entity("").build();
        }

        @POST
        @Path("/pet")
        @Operation(summary = "Create pet",
                description = "Creating pet.")
        @Consumes({"application/json", "application/xml"})
        public Response methodWithTwoRequestBodyWithoutAnnotationAndTwoConsumes(
                Pet pet, User user) {
            return Response.ok().entity("").build();
        }

        @DELETE
        @Path("/pet")
        @Operation(summary = "Delete pet",
                description = "Deleting pet.")
        @Consumes({"application/json", "application/xml"})
        public Response methodWithOneSimpleRequestBody(int id) {
            return Response.ok().entity("").build();
        }
    }
}
