package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class RefParameter3074Resource {

    public static final String EXPECTED_YAML_WITHOUT_WRAPPER =
            "openapi: 3.0.1\n" +
            "paths:\n" +
            "  /employee:\n" +
            "      get:\n" +
            "        summary: Get an employee\n" +
            "        operationId: getEmployee\n" +
            "        responses:\n" +
            "          \"200\":\n" +
            "            content:\n" +
            "              application/json:\n" +
            "                schema:\n" +
            "                   $ref: '#/components/schemas/Employee'\n" +
            "          \"500\":\n" +
            "            content:\n" +
            "              application/json:\n" +
            "                schema:\n" +
            "                  $ref: '#/components/schemas/Error'\n" +
            "components:\n" +
            "  schemas:\n" +
            "    Employee:\n" +
            "      type: object\n" +
            "    Error:\n" +
            "      type: object";

    public static final String EXPECTED_YAML_WITH_WRAPPER = EXPECTED_YAML_WITHOUT_WRAPPER +
            "\n    Wrapper:\n     type: object";

    class Wrapper<A> {
        final A a;

        Wrapper(A a) {
            this.a = a;
        }
    }

    class Employee {
        int number;
        String name;

        Employee(int number, String name) {
            this.number = number;
            this.name = name;
        }
    }

    class Error {
        String message;
    }

    @GET
    @Path("/employee")
    @Operation(summary = "Get an employee")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = Employee.class), mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "500",
            content = @Content(schema = @Schema(implementation = Error.class), mediaType = "application/json")
        ),
    })
    public Wrapper getEmployee() {
        return new Wrapper<Employee>(new Employee(1, "Michael Suyama"));
    }
}
