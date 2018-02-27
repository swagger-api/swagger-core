package io.swagger.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import io.swagger.annotations.ResponseHeader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@ApiOperation(
        value = "Meta DataUber Description",
        notes = "Test Uber Description",
        consumes = "multipart/form-data",
        produces = "multipart/form-data",
        httpMethod = "GET",
        nickname = "users",
        protocols = "ws, wss",
        tags = {"operation_tag1", "operation_tag2"},
        authorizations = {
                @Authorization(
                        value = "operation_auth",
                        scopes = {
                                @AuthorizationScope(
                                        scope = "operation_auth_scope",
                                        description = "operation_auth_description")})},
        response = String.class,
        responseContainer = "list",
        responseHeaders = {
                @ResponseHeader(
                        name = "operation_response_header1",
                        description = "operation_response_header_description1",
                        response = Class.class),
                @ResponseHeader(
                        name = "operation_response_header2",
                        description = "operation_response_header_description2",
                        response = Class.class),
                @ResponseHeader(
                        name = "operation_response_header3",
                        description = "operation_response_header_description3",
                        response = Class.class)},
        responseReference = "#test",
        extensions = {
                @Extension(properties = {
                        @ExtensionProperty(name = "name", value = "value")
                })}
)
@Retention(RetentionPolicy.RUNTIME)
public @interface Uber {
    String description() default "";

}
