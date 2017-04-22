package io.swagger.annotations.test.pathItems;

import io.swagger.annotations.Operation;
import io.swagger.annotations.links.Link;
import io.swagger.annotations.links.LinkParameters;
import io.swagger.annotations.test.AbstractAnnotationTest;
import org.testng.annotations.Test;

import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static org.testng.Assert.assertEquals;

public class OperationsWithLinks extends AbstractAnnotationTest {
    @Test(enabled = false, description = "Shows creating simple links")
    public void createOperationWithLinks() {
        String yaml = readIntoYaml(ClassWithOperationAndLinks.class);

        assertEquals(yaml,
            "/users:\n" +
            "  get:\n" +
            "    operationId: getUser\n" +
            "    parameters:\n" +
            "      - in: query\n" +
            "        name: userId\n" +
            "        schema:\n" +
            "          type: string\n" +
            "    responses:\n" +
            "      default:\n" +
            "        description: no description\n" +
            "        schema:\n" +
            "          $ref: '#/components/schemas/User'\n" +
            "        links:\n" +
            "          address:\n" +
            "            operationId: getAddress\n" +
            "            parameters:\n" +
            "              userId: '$request.query.userId'\n" +
            "/addresses:\n" +
            "  get:\n" +
            "    operationId: getAddress\n" +
            "    parameters:\n" +
            "      - in: query\n" +
            "        name: userId\n" +
            "        schema:\n" +
            "          type: string\n" +
            "    responses:\n" +
            "      default:\n" +
            "        description: no description\n" +
            "        schema:\n" +
            "          $ref: '#/components/schemas/Address'");
    }

    static class ClassWithOperationAndLinks {
        @Path("/users")
        @Operation(links = {
            @Link(
                name = "address",
                operationId = "getAddress",
                parameters = @LinkParameters(
                        name = "userId",
                        expression = "$request.query.userId"))
        })
        public User getUser(@QueryParam("userId") String userId) {
            return null;
        }

        @Path("/addresses")
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
}
