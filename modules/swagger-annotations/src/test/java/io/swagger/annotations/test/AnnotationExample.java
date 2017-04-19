package io.swagger.annotations.test;

import io.swagger.annotations.Operation;
import io.swagger.annotations.Parameter;
import io.swagger.annotations.media.Content;
import io.swagger.annotations.media.Schema;
import io.swagger.annotations.parameters.RequestBody;
import io.swagger.annotations.servers.Server;
import io.swagger.annotations.servers.ServerVariable;

import java.util.List;

public class AnnotationExample {
    @Operation(
            summary = "ok",
            parameters = {
                    @Parameter(name = "something",
                            in = "query",
                            description = "a query param",
                            schema = @Schema(type = "string", format = "email")
                    ),
                    @Parameter(name = "bodyParameter",
                            in = "body",
                            description = "a complex input type",
                            content = {
                                    @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            /* todo */
                                    ))
                            }
                    )
            },

            servers = @Server(description = "our internal servers",
                    url = "http://{env}.foo.com",
                    variables = {
                            @ServerVariable(
                                    value = "development",
                                    name = "env",
                                    description = "the host",
                                    allowableValues = {"development", "staging", "production"})
                    }))
    public void fake(@Parameter(description = "the skip parameter") /*@QueryParam("skip")*/ String skip) {

    }

    @Operation(description = "get available currencies")
    public void methodWithDecoratedParameters(
            @Parameter(style = "form", description = "a comma-delimited list of currencies")
            /* @QueryParam("currencies") */List<String> currencies){
    }

    @Operation(description = "defines a JSON input")
    public void addUser (
            @RequestBody(description = "the user to add",
            content = {
                @Content(mediaType = "application/json",
                schema = @Schema(implementation = MySampleModel.class))
            })
            Object model){
    }

    @Operation(description = "defines multiple input types with `anyOf`")
    public void addWithMultipleTypes(
            @RequestBody(description = "we accept everything!",
            content = {
                @Content(mediaType = "application/json",
                schema = @Schema(anyOf = {Table.class, Animal.class, Pencil.class}))
            }) Object something) {

    }

    @Operation(description = "accepts anything _except_ an animal")
    public void hateTheAnimals(
            @RequestBody(description = "no animals allowed",
            content = {
                @Content(mediaType = "application/json",
                schema = @Schema(type = "object", not = Animal.class))
            }) Object something) {

    }

    public static class MySampleModel {
        private String username;
    }

    public static class Table {}

    public static class Animal {}

    public static class Pencil {}
}
