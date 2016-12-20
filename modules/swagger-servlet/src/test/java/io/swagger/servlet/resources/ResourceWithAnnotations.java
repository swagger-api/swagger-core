package io.swagger.servlet.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiKeyAuthDefinition;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import io.swagger.annotations.BasicAuthDefinition;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import io.swagger.annotations.ExternalDocs;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.OAuth2Definition;
import io.swagger.annotations.ResponseHeader;
import io.swagger.annotations.SecurityDefinition;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import io.swagger.servlet.models.SampleData;

@SwaggerDefinition(
        info = @Info(
                description = "Test description",
                version = "1.0.0",
                title = "Test title",
                termsOfService = "link_to_terms",
                contact = @Contact(name = "Author", email = "author@mail", url = "site"),
                license = @License(name = "License", url = "license_url"),
                extensions = {
                        @Extension(properties = {
                                @ExtensionProperty(name = "ext1_prop1", value = "ext1_val1"),
                                @ExtensionProperty(name = "x-ext1_prop2", value = "x-ext1_val2"),
                                @ExtensionProperty(name = "ext1_prop3", value = "")}),
                        @Extension(name = "ext2", properties = {
                                @ExtensionProperty(name = "ext2_prop1", value = "ext2_val1"),
                                @ExtensionProperty(name = "ext2_prop2", value = "ext2_val2")}),
                        @Extension(name = "x-ext3", properties = {
                                @ExtensionProperty(name = "x-ext3_prop1", value = "x-ext3_val1")})
                }
        ),
        basePath = "/api",
        host = "host",
        consumes = {"application/json", "application/xml", ""},
        produces = {"application/json", "application/xml", ""},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS, SwaggerDefinition.Scheme.DEFAULT},
        securityDefinition = @SecurityDefinition(
                basicAuthDefinitions = {
                        @BasicAuthDefinition(key = "basicAuth")},
                apiKeyAuthDefinitions = {
                        @ApiKeyAuthDefinition(key = "apiKeyAuth", name = "apiKey", in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER)},
                oAuth2Definitions = {
                        @OAuth2Definition(key = "oAuth2AccessCode", flow = OAuth2Definition.Flow.ACCESS_CODE),
                        @OAuth2Definition(key = "oAuth2Password", flow = OAuth2Definition.Flow.PASSWORD)
                }
        ),
        tags = {
                @Tag(name = "tests", description = "tests"),
                @Tag(name = "tests", description = "tests",
                        externalDocs = @ExternalDocs(value = "tag_docs", url = "url_to_tag_docs")),
                @Tag(name = "", description = "")},
        externalDocs = @ExternalDocs(value = "docs", url = "url_to_docs")
)
@Api(
        value = "/resources",
        consumes = "application/json, application/xml",
        produces = "application/json, application/xml",
        protocols = "http",
        tags = {"api_tag1", "api_tag2"},
        authorizations = {
                @Authorization(
                        value = "api_auth",
                        scopes = {
                                @AuthorizationScope(
                                        scope = "api_auth_scope",
                                        description = "api_auth_description")})}
)
public class ResourceWithAnnotations {

    public String testMethod1() {
        return null;
    }

    @ApiOperation(value = "Test summary")
    @Deprecated
    public void testMethod2() {

    }

    @ApiOperation(
            value = "Test summary",
            notes = "Test description",
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
                            response = SampleData.class),
                    @ResponseHeader(
                            name = "operation_response_header2",
                            description = "operation_response_header_description2",
                            response = Void.class),
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
    @ApiResponses({
            @ApiResponse(
                    message = "response_annotation1",
                    code = 0,
                    response = String.class,
                    responseContainer = "array"),
            @ApiResponse(
                    message = "response_annotation2",
                    code = 400,
                    response = String.class,
                    responseContainer = "map"),
            @ApiResponse(
                    message = "response_annotation3",
                    code = 401,
                    response = String.class,
                    responseContainer = "set"),
            @ApiResponse(
                    message = "response_annotation4",
                    code = 402,
                    reference = "#test"),
            @ApiResponse(
                    message = "response_annotation5",
                    code = 403,
                    response = Class.class),
            @ApiResponse(
                    message = "response_annotation6",
                    code = 404,
                    response = Void.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param1", value = "Param 1", required = true, dataType = "integer", paramType = "path"),
            @ApiImplicitParam(name = "param2", value = "Param 2", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "param3", value = "Param 3", dataType = "java.util.Date", paramType = "form"),
            @ApiImplicitParam(name = "param4", value = "Param 4", dataType = "integer", paramType = "formData"),
            @ApiImplicitParam(name = "param5", value = "Param 5", dataType = "string", paramType = "body"),
            @ApiImplicitParam(name = "param6", value = "Param 6", dataType = "fakeType", paramType = "header"),
            @ApiImplicitParam(name = "param7", value = "Param 7", dataType = "string", paramType = "fakeType")
    })
    public void testMethod3() {

    }

    @ApiOperation(
            value = "",
            notes = "",
            consumes = "",
            produces = "",
            nickname = "",
            protocols = "",
            tags = {},
            authorizations = {
                    @Authorization(
                            value = "",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "",
                                            description = "")})},
            code = 0,
            response = Class.class,
            responseContainer = "",
            responseHeaders = {},
            responseReference = ""
    )
    @ApiResponses({})
    @ApiImplicitParams({})
    public void testMethod4() {

    }

    @ApiOperation(value = "empty")
    public void testMethod5(String param) {

    }
}
