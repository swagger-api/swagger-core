package io.swagger.v3.jaxrs2.annotations.pathItems;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class OperationsWithLinksTest extends AbstractAnnotationTest {

    @Test(description = "Shows creating simple links")
    public void createOperationWithLinks() throws IOException{

        String expectedYAML = "openapi: 3.0.0\n" +
                "paths:\n" +
                "  /users:\n" +
                "    get:\n" +
                "      operationId: getUser\n" +
                "      parameters:\n" +
                "      - name: userId\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: test description\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/User'\n" +
                "          links:\n" +
                "            address:\n" +
                "              operationId: getAddress\n" +
                "              parameters:\n" +
                "                userId: $request.query.userId\n" +
                "  /addresses:\n" +
                "    get:\n" +
                "      operationId: getAddress\n" +
                "      parameters:\n" +
                "      - name: userId\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: test description\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/Address'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    User:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: string\n" +
                "        username:\n" +
                "          type: string\n" +
                "    Address:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        street:\n" +
                "          type: string\n" +
                "        zip:\n" +
                "          type: string";

        compareAsYaml(ClassWithOperationAndLinks.class, expectedYAML);
    }

    @Test(description = "Shows creating operation response without annotation")
    public void createOperationWithResponseNoAnnotation() throws IOException{

        String expectedYAML = "openapi: 3.0.0\n" +
                "paths:\n" +
                "  /users:\n" +
                "    get:\n" +
                "      operationId: getUser\n" +
                "      parameters:\n" +
                "      - name: userId\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/User'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    User:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: string\n" +
                "        username:\n" +
                "          type: string";

        compareAsYaml(ClassWithResponseNoAnnotation.class, expectedYAML);
    }

    static class ClassWithOperationAndLinks {
        @Path("/users")
        @Operation(operationId = "getUser",
                responses = {
                        @ApiResponse(description = "test description",
                                content = @Content(mediaType = "*/*", schema = @Schema(ref = "#/components/schemas/User")),
                                links = {
                                        @Link(
                                                name = "address",
                                                operationId = "getAddress",
                                                parameters = @LinkParameter(
                                                        name = "userId",
                                                        expression = "$request.query.userId"))
                                })}
        )
        @GET
        public User getUser(@QueryParam("userId") String userId) {
            return null;
        }

        @Path("/addresses")
        @Operation(operationId = "getAddress",

                responses = {
                        @ApiResponse(content = @Content(mediaType = "*/*",
                                schema = @Schema(ref = "#/components/schemas/Address")),
                                description = "test description")
                })
        @GET
        public Address getAddress(@QueryParam("userId") String userId) {
            return null;
        }
    }

    static class ClassWithResponseNoAnnotation {
        @Path("/users")
        @Operation(operationId = "getUser")
        @GET
        public User getUser(@QueryParam("userId") String userId) {
            return null;
        }
    }

    static class User {
        private String id;
        private String username;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    static class Address {
        private String street;
        private String zip;

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }
    }

    @Test(description = "Shows creating simple links")
    public void createOperationWithLinkReferences() {
        String openApiYAML = readIntoYaml(ClassWithOperationAndLinkReferences.class);
        int start = openApiYAML.indexOf("/users:");
        int end = openApiYAML.length() - 1;

        String expectedYaml = "/users:\n" +
                "    get:\n" +
                "      operationId: getUser\n" +
                "      parameters:\n" +
                "      - name: userId\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: test description\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/User'\n" +
                "          links:\n" +
                "            user:\n" +
                "              operationId: getUser\n" +
                "              parameters:\n" +
                "                userId: $request.query.userId\n" +
                "components:\n" +
                "  schemas:\n" +
                "    User:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: string\n" +
                "        username:\n" +
                "          type: string";
        String extractedYAML = openApiYAML.substring(start, end);
        assertEquals(extractedYAML, expectedYaml);
    }

    static class ClassWithOperationAndLinkReferences {
        @Path("/users")
        @Operation(operationId = "getUser",
                responses = {
                        @ApiResponse(description = "test description",
                                links = {
                                        @Link(
                                                name = "user",
                                                operationId = "getUser",
                                                operationRef = "#/components/links/MyLink",
                                                parameters = @LinkParameter(
                                                        name = "userId",
                                                        expression = "$request.query.userId"))
                                })}
        )
        @GET
        public User getUser(@QueryParam("userId") String userId) {
            return null;
        }
    }
}
