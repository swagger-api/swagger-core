package io.swagger.jaxrs2.annotations.pathItems;

import io.swagger.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.links.Link;
import io.swagger.oas.annotations.links.LinkParameters;
import io.swagger.oas.annotations.responses.ApiResponse;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static org.testng.Assert.assertEquals;

public class OperationsWithLinks extends AbstractAnnotationTest {
    @Test(enabled = false, description = "Shows creating simple links")
    public void createOperationWithLinks() {
        String openApiYAML = readIntoYaml(ClassWithOperationAndLinks.class);
        int start = openApiYAML.indexOf("/users:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "/users:\n" +
                "    get:\n" +
                "      operationId: \"getUser\"\n" +
                "      parameters:\n" +
                "      - name: \"userId\"\n" +
                "        in: \"query\"\n" +
                "        schema:\n" +
                "          type: \"string\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: \"no description\"\n" +
                "          schema:\n" +
                "            $ref: \"#/components/schemas/User\"\n" +
                "          links:\n" +
                "            address:\n" +
                "              operationId: \"getAddress\"\n" +
                "              parameters:\n" +
                "                userId: \"$request.query.userId\"\n" +
                "/addresses:\n" +
                "    get:\n" +
                "      operationId: \"getAddress\"\n" +
                "      parameters:\n" +
                "      - in: \"query\"\n" +
                "        name: \"userId\"\n" +
                "        schema:\n" +
                "          type: \"string\"\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: \"no description\"\n" +
                "          schema:\n" +
                "            $ref: \"#/components/schemas/Address\"";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class ClassWithOperationAndLinks {
        @Path("/users")
        @Operation(operationId = "getUser",
                responses = {
                        @ApiResponse(description = "no description")},
                links = {
                        @Link(
                                name = "address",
                                operationId = "getAddress",
                                parameters = @LinkParameters(
                                        name = "userId",
                                        expression = "$request.query.userId"))
                })
        @GET
        public User getUser(@QueryParam("userId") String userId) {
            return null;
        }

        @Path("/addresses")
        @Operation(operationId = "getAddress",
                responses = {
                        @ApiResponse(description = "no description")
                })
        @GET
        public Address getAddress(@QueryParam("userId") String userId) {
            return null;
        }
    }

    static class User {
        private String id;
        private String username;
    }

    static class Address {
        private String street;
        private String zip;
    }

    @Test(enabled = false, description = "Shows creating simple links")
    public void createOperationWithLinkReferences() {
        String yaml = readIntoYaml(ClassWithOperationAndLinkReferences.class);

        assertEquals(yaml,
                "/users:\n" +
                        "  get:\n" +
                        "    operationId: getUser\n" +
                        "    parameters:\n" +
                        "    - in: query\n" +
                        "      name: userId\n" +
                        "      schema:\n" +
                        "        type: string\n" +
                        "    responses:\n" +
                        "      default:\n" +
                        "        description: no description\n" +
                        "        schema:\n" +
                        "          $ref: '#/components/schemas/User'\n" +
                        "        links:\n" +
                        "          $ref: '#/components/links/MyLink'");
    }

    static class ClassWithOperationAndLinkReferences {
        @Path("/users")
        @Operation(links = {
                @Link(
                        operationRef = "#/components/links/MyLink")
        })
        public User getUser(@QueryParam("userId") String userId) {
            return null;
        }
    }
}
