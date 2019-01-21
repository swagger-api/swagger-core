package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class RefParameter3074Resource {

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
    @Operation(
            operationId = "getEmployee",
            summary = "Get an employee",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(allOf = {Employee.class, Error.class})
                            )
                    )
            }
    )
    public Wrapper getEmployee() {
        return new Wrapper<Employee>(new Employee(1, "Michael Suyama"));
    }
}
