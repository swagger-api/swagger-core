package io.swagger.annotations.test;

import io.swagger.annotations.Operation;
import io.swagger.annotations.Parameter;
import io.swagger.annotations.media.Schema;
import io.swagger.annotations.servers.Server;
import io.swagger.annotations.servers.ServerVariable;

public class AnnotationExample {
    @Operation(
            summary = "ok",
            parameters = {
                    @Parameter(name = "something",
                            in = "query",
                            description = "a query param",
                            schema = @Schema(type = "string", format = "email")
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
}
