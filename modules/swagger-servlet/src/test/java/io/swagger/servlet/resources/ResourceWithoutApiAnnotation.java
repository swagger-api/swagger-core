package io.swagger.servlet.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@SwaggerDefinition(
        consumes = {"application/json", "application/xml"},
        produces = {"application/json", "application/xml"},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
        tags = {@Tag(name = "tests", description = "tests")}
)
public class ResourceWithoutApiAnnotation {

    public void testMethod1() {

    }

    @ApiOperation(value = "sample")
    public void testMethod2() {

    }

    @ApiOperation(
            value = "sample",
            consumes = "multipart/form-data",
            produces = "multipart/form-data",
            nickname = "/users",
            protocols = "http, https, http",
            tags = {"operation_tag1", "operation_tag2"},
            authorizations = {@Authorization(
                    value = "operation_auth",
                    scopes = {@AuthorizationScope(
                            scope = "",
                            description = "")})}
    )
    public void testMethod3() {

    }

    @ApiOperation(
            value = "sample",
            consumes = "",
            produces = "",
            nickname = "",
            protocols = "",
            tags = {},
            authorizations = {}
    )
    public void testMethod4() {

    }
}
