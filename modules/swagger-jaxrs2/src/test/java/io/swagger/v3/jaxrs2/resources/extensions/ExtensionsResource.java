package io.swagger.v3.jaxrs2.resources.extensions;

import io.swagger.v3.jaxrs2.resources.model.ExtensionUser;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.callbacks.Callback;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@OpenAPIDefinition(
        extensions = {
                @Extension(name = "x-openapi", properties = {
                        @ExtensionProperty(name = "name", value = "Josh")}),
                @Extension(name = "openapi-extensions", properties = {
                        @ExtensionProperty(name = "lastName", value = "Hart"),
                        @ExtensionProperty(name = "address", value = "House")})
        },
        info = @Info(
                extensions = {
                        @Extension(name = "x-info", properties = {
                                @ExtensionProperty(name = "name", value = "Josh")}),
                        @Extension(name = "info-extensions", properties = {
                                @ExtensionProperty(name = "lastName", value = "Hart"),
                                @ExtensionProperty(name = "address", value = "House")})
                },
                contact = @Contact(
                        extensions = {
                                @Extension(name = "x-contact", properties = {
                                        @ExtensionProperty(name = "name", value = "Josh")}),
                                @Extension(name = "contact-extensions", properties = {
                                        @ExtensionProperty(name = "lastName", value = "Hart"),
                                        @ExtensionProperty(name = "address", value = "House")})
                        }
                ),
                license = @License(
                        extensions = {
                                @Extension(name = "x-license", properties = {
                                        @ExtensionProperty(name = "name", value = "Josh")}),
                                @Extension(name = "license-extensions", properties = {
                                        @ExtensionProperty(name = "lastName", value = "Hart"),
                                        @ExtensionProperty(name = "address", value = "House")})
                        }
                )
        ),
        servers = @Server(
                extensions = {
                        @Extension(name = "x-server", properties = {
                                @ExtensionProperty(name = "name", value = "Josh")}),
                        @Extension(name = "server-extensions", properties = {
                                @ExtensionProperty(name = "lastName", value = "Hart"),
                                @ExtensionProperty(name = "address", value = "House")})
                },
                variables = @ServerVariable(
                        name = "aa",
                        defaultValue = "aa",
                        extensions = {
                                @Extension(name = "x-servervar", properties = {
                                        @ExtensionProperty(name = "name", value = "Josh")}),
                                @Extension(name = "servervar-extensions", properties = {
                                        @ExtensionProperty(name = "lastName", value = "Hart"),
                                        @ExtensionProperty(name = "address", value = "House")})
                        }
                )
        ),
        externalDocs = @ExternalDocumentation(
                extensions = {
                        @Extension(name = "x-externalDocs", properties = {
                                @ExtensionProperty(name = "name", value = "Josh")}),
                        @Extension(name = "externalDocs-extensions", properties = {
                                @ExtensionProperty(name = "lastName", value = "Hart"),
                                @ExtensionProperty(name = "address", value = "House")})
                }

        )
)
@SecurityScheme(name = "myOauth2Security",
        type = SecuritySchemeType.OAUTH2,
        in = SecuritySchemeIn.HEADER,
        description = "myOauthSecurity Description",
        flows = @OAuthFlows(
                implicit = @OAuthFlow(
                        authorizationUrl = "http://x.com",
                        scopes = @OAuthScope(
                                name = "write:pets",
                                description = "modify pets in your account"
                        ),
                        extensions = {
                                @Extension(name = "x-oauthflow", properties = {
                                        @ExtensionProperty(name = "name", value = "Josh")}),
                                @Extension(name = "oauthflow-extensions", properties = {
                                        @ExtensionProperty(name = "lastName", value = "Hart"),
                                        @ExtensionProperty(name = "address", value = "House")})
                        }
                ),
                extensions = {
                        @Extension(name = "x-oauthflows", properties = {
                                @ExtensionProperty(name = "name", value = "Josh")}),
                        @Extension(name = "oauthflows-extensions", properties = {
                                @ExtensionProperty(name = "lastName", value = "Hart"),
                                @ExtensionProperty(name = "address", value = "House")})
                }
        ),
        extensions = {
                @Extension(name = "x-security", properties = {
                        @ExtensionProperty(name = "name", value = "Josh")}),
                @Extension(name = "security-extensions", properties = {
                        @ExtensionProperty(name = "lastName", value = "Hart"),
                        @ExtensionProperty(name = "address", value = "House")})
        }
)
@SecurityRequirement(name = "security_key",
        scopes = {"write:pets", "read:pets"}
)
@SecurityRequirement(name = "myOauth2Security",
        scopes = {"write:pets"}
)
public class ExtensionsResource {

    @GET
    @Path("/")
    @Tag(
            name = "MyTag",
            extensions = {
                    @Extension(name = "x-tag", properties = {
                            @ExtensionProperty(name = "name", value = "Josh")}),
                    @Extension(name = "tag-extensions", properties = {
                            @ExtensionProperty(name = "lastName", value = "Hart"),
                            @ExtensionProperty(name = "address", value = "House")})
            }
    )
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description",
            extensions = {
                    @Extension(name = "x-operation", properties = {
                            @ExtensionProperty(name = "name", value = "Josh")}),
                    @Extension(name = "x-operation-extensions", properties = {
                            @ExtensionProperty(name = "lastName", value = "Hart"),
                            @ExtensionProperty(name = "address", value = "House")})
            })
    public Response getSummaryAndDescription(
            @Parameter(
                    extensions = @Extension(
                            name = "x-parameter",
                            properties = {
                                    @ExtensionProperty(name = "parameter", value = "value")
                            }
                    ),
                    examples = {
                            @ExampleObject(
                                    name = "example1",
                                    value = "example1",
                                    summary = "Summary example 1",
                                    externalValue = "external value 1",
                                    extensions = {
                                            @Extension(name = "x-examples", properties = {
                                                    @ExtensionProperty(name = "name", value = "Josh")}),
                                            @Extension(name = "examples-extensions", properties = {
                                                    @ExtensionProperty(name = "lastName", value = "Hart"),
                                                    @ExtensionProperty(name = "address", value = "House")})
                                    }
                            )
                    }
            )
            @QueryParam("subscriptionId") String subscriptionId) {

        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/user")
    @Operation(
            operationId = "getUser"
    )
    @Callback(
            callbackUrlExpression = "http://$request.query.url",
            name = "subscription",
            extensions = {
                    @Extension(name = "x-callback", properties = {
                            @ExtensionProperty(name = "name", value = "Josh")}),
                    @Extension(name = "callback-extensions", properties = {
                            @ExtensionProperty(name = "lastName", value = "Hart"),
                            @ExtensionProperty(name = "address", value = "House")})
            },
            operation = {
                    @Operation(
                            method = "post",
                            description = "payload data will be sent",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "subscriptionId", required = true, schema = @Schema(
                                            type = "string",
                                            format = "uuid",
                                            description = "the generated UUID",
                                            readOnly = true
                                    ))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Return this code if the callback was received and processed successfully"
                                    ),
                                    @ApiResponse(
                                            responseCode = "205",
                                            description = "Return this code to unsubscribe from future data updates"
                                    ),
                                    @ApiResponse(
                                            responseCode = "default",
                                            description = "All other response codes will disable this callback subscription"
                                    )
                            }),
                    @Operation(
                            method = "get",
                            description = "payload data will be received"
                    ),
                    @Operation(
                            method = "put",
                            description = "payload data will be sent"
                    )})
    public void getUser(
            //@Parameter(description = "Parameter with no IN", required = true)
            @RequestBody(
                    description = "Request Body in Param",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    extensions = {
                                            @Extension(name = "x-schema", properties = {
                                                    @ExtensionProperty(name = "name", value = "Josh")}),
                                            @Extension(name = "schema-extensions", properties = {
                                                    @ExtensionProperty(name = "lastName", value = "Hart"),
                                                    @ExtensionProperty(name = "address", value = "House")})
                                    }
                            ),
                            extensions = {
                                    @Extension(name = "x-content", properties = {
                                            @ExtensionProperty(name = "name", value = "Josh")}),
                                    @Extension(name = "content-extensions", properties = {
                                            @ExtensionProperty(name = "lastName", value = "Hart"),
                                            @ExtensionProperty(name = "address", value = "House")})
                            },
                            encoding = @Encoding(
                                    name = "application/xml",
                                    extensions = {
                                            @Extension(name = "x-encoding", properties = {
                                                    @ExtensionProperty(name = "name", value = "Josh")}),
                                            @Extension(name = "encoding-extensions", properties = {
                                                    @ExtensionProperty(name = "lastName", value = "Hart"),
                                                    @ExtensionProperty(name = "address", value = "House")})
                                    }
                            )
                    ),
                    extensions = {
                            @Extension(name = "x-extension", properties = {
                                    @ExtensionProperty(name = "name", value = "param")}),
                            @Extension(name = "x-extension2", properties = {
                                    @ExtensionProperty(name = "another", value = "val")})})
                    ExtensionUser user) {
    }

    @POST
    @Path("/user")
    @Operation(operationId = "setUser"
    )
    @ApiResponse(
            description = "200",
            content = @Content(
                    extensions = {
                            @Extension(name = "x-content", properties = {
                                    @ExtensionProperty(name = "name", value = "Josh")}),
                            @Extension(name = "content-extensions", properties = {
                                    @ExtensionProperty(name = "lastName", value = "Hart"),
                                    @ExtensionProperty(name = "address", value = "House")})
                    },
                    encoding = @Encoding(
                            extensions = {
                                    @Extension(name = "x-encoding", properties = {
                                            @ExtensionProperty(name = "name", value = "Josh")}),
                                    @Extension(name = "encoding-extensions", properties = {
                                            @ExtensionProperty(name = "lastName", value = "Hart"),
                                            @ExtensionProperty(name = "address", value = "House")})
                            }
                    )
            ),
            links = @Link(
                    name = "aa",
                    description = "aa",
                    operationId = "getUser",
                    extensions = {
                            @Extension(name = "x-links", properties = {
                                    @ExtensionProperty(name = "name", value = "Josh")}),
                            @Extension(name = "links-extensions", properties = {
                                    @ExtensionProperty(name = "lastName", value = "Hart"),
                                    @ExtensionProperty(name = "address", value = "House")})
                    }

            ),
            extensions = {
                    @Extension(name = "x-response", properties = {
                            @ExtensionProperty(name = "name", value = "Josh")}),
                    @Extension(name = "response-extensions", properties = {
                            @ExtensionProperty(name = "lastName", value = "Hart"),
                            @ExtensionProperty(name = "address", value = "House")})
            }
    )
    public ExtensionUser setUser(
            @Parameter(
                    description = "Parameter with no IN",
                    required = true,
                    schema = @Schema(
                            description = "the user",
                            readOnly = true,
                            extensions = {
                                    @Extension(name = "x-schema", properties = {
                                            @ExtensionProperty(name = "name", value = "Josh")}),
                                    @Extension(name = "schema-extensions", properties = {
                                            @ExtensionProperty(name = "lastName", value = "Hart"),
                                            @ExtensionProperty(name = "address", value = "House")})
                            }
                    )
            )
            @RequestBody(description = "Request Body in Param",
                    extensions = {
                            @Extension(name = "x-extension", properties = {
                                    @ExtensionProperty(name = "name", value = "param")}),
                            @Extension(name = "x-extension2", properties = {
                                    @ExtensionProperty(name = "another", value = "val")})})
                    ExtensionUser user) {
        return null;
    }


    public static final String YAML =
            "openapi: 3.0.1\n" +
                    "info:\n" +
                    "  contact:\n" +
                    "    x-contact:\n" +
                    "      name: Josh\n" +
                    "    x-contact-extensions:\n" +
                    "      lastName: Hart\n" +
                    "      address: House\n" +
                    "  license:\n" +
                    "    x-license:\n" +
                    "      name: Josh\n" +
                    "    x-license-extensions:\n" +
                    "      lastName: Hart\n" +
                    "      address: House\n" +
                    "  x-info:\n" +
                    "    name: Josh\n" +
                    "  x-info-extensions:\n" +
                    "    lastName: Hart\n" +
                    "    address: House\n" +
                    "externalDocs:\n" +
                    "  x-externalDocs:\n" +
                    "    name: Josh\n" +
                    "  x-externalDocs-extensions:\n" +
                    "    lastName: Hart\n" +
                    "    address: House\n" +
                    "servers:\n" +
                    "- variables:\n" +
                    "    aa:\n" +
                    "      enum:\n" +
                    "      - \"\"\n" +
                    "      default: aa\n" +
                    "      x-servervar:\n" +
                    "        name: Josh\n" +
                    "      x-servervar-extensions:\n" +
                    "        lastName: Hart\n" +
                    "        address: House\n" +
                    "  x-server-extensions:\n" +
                    "    lastName: Hart\n" +
                    "    address: House\n" +
                    "  x-server:\n" +
                    "    name: Josh\n" +
                    "paths:\n" +
                    "  /:\n" +
                    "    get:\n" +
                    "      tags:\n" +
                    "      - MyTag\n" +
                    "      summary: Operation Summary\n" +
                    "      description: Operation Description\n" +
                    "      operationId: operationId\n" +
                    "      parameters:\n" +
                    "      - name: subscriptionId\n" +
                    "        in: query\n" +
                    "        schema:\n" +
                    "          type: string\n" +
                    "        examples:\n" +
                    "          example1:\n" +
                    "            summary: Summary example 1\n" +
                    "            description: example1\n" +
                    "            value: example1\n" +
                    "            externalValue: external value 1\n" +
                    "            x-examples-extensions:\n" +
                    "              lastName: Hart\n" +
                    "              address: House\n" +
                    "            x-examples:\n" +
                    "              name: Josh\n" +
                    "        x-parameter:\n" +
                    "          parameter: value\n" +
                    "      responses:\n" +
                    "        default:\n" +
                    "          description: default response\n" +
                    "          content:\n" +
                    "            '*/*': {}\n" +
                    "      security:\n" +
                    "      - security_key:\n" +
                    "        - write:pets\n" +
                    "        - read:pets\n" +
                    "      - myOauth2Security:\n" +
                    "        - write:pets\n" +
                    "      x-operation:\n" +
                    "        name: Josh\n" +
                    "      x-operation-extensions:\n" +
                    "        lastName: Hart\n" +
                    "        address: House\n" +
                    "  /user:\n" +
                    "    get:\n" +
                    "      operationId: getUser\n" +
                    "      requestBody:\n" +
                    "        description: Request Body in Param\n" +
                    "        content:\n" +
                    "          application/json:\n" +
                    "            schema:\n" +
                    "              type: string\n" +
                    "              x-schema:\n" +
                    "                name: Josh\n" +
                    "              x-schema-extensions:\n" +
                    "                lastName: Hart\n" +
                    "                address: House\n" +
                    "            encoding:\n" +
                    "              application/xml:\n" +
                    "                x-encoding-extensions:\n" +
                    "                  lastName: Hart\n" +
                    "                  address: House\n" +
                    "                x-encoding:\n" +
                    "                  name: Josh\n" +
                    "            x-content:\n" +
                    "              name: Josh\n" +
                    "            x-content-extensions:\n" +
                    "              lastName: Hart\n" +
                    "              address: House\n" +
                    "        x-extension:\n" +
                    "          name: param\n" +
                    "        x-extension2:\n" +
                    "          another: val\n" +
                    "      responses:\n" +
                    "        default:\n" +
                    "          description: default response\n" +
                    "          content:\n" +
                    "            '*/*': {}\n" +
                    "      callbacks:\n" +
                    "        subscription:\n" +
                    "          http://$request.query.url:\n" +
                    "            get:\n" +
                    "              description: payload data will be received\n" +
                    "            put:\n" +
                    "              description: payload data will be sent\n" +
                    "            post:\n" +
                    "              description: payload data will be sent\n" +
                    "              parameters:\n" +
                    "              - name: subscriptionId\n" +
                    "                in: path\n" +
                    "                required: true\n" +
                    "                schema:\n" +
                    "                  type: string\n" +
                    "                  description: the generated UUID\n" +
                    "                  format: uuid\n" +
                    "                  readOnly: true\n" +
                    "              responses:\n" +
                    "                200:\n" +
                    "                  description: Return this code if the callback was received and processed\n" +
                    "                    successfully\n" +
                    "                205:\n" +
                    "                  description: Return this code to unsubscribe from future data updates\n" +
                    "                default:\n" +
                    "                  description: All other response codes will disable this callback\n" +
                    "                    subscription\n" +
                    "      security:\n" +
                    "      - security_key:\n" +
                    "        - write:pets\n" +
                    "        - read:pets\n" +
                    "      - myOauth2Security:\n" +
                    "        - write:pets\n" +
                    "    post:\n" +
                    "      operationId: setUser\n" +
                    "      requestBody:\n" +
                    "        description: Request Body in Param\n" +
                    "        content:\n" +
                    "          '*/*':\n" +
                    "            schema:\n" +
                    "              $ref: '#/components/schemas/ExtensionUser'\n" +
                    "        x-extension:\n" +
                    "          name: param\n" +
                    "        x-extension2:\n" +
                    "          another: val\n" +
                    "      responses:\n" +
                    "        default:\n" +
                    "          description: \"200\"\n" +
                    "          content:\n" +
                    "            '*/*':\n" +
                    "              schema:\n" +
                    "                $ref: '#/components/schemas/ExtensionUser'\n" +
                    "          links:\n" +
                    "            aa:\n" +
                    "              operationId: getUser\n" +
                    "              description: aa\n" +
                    "              x-links-extensions:\n" +
                    "                lastName: Hart\n" +
                    "                address: House\n" +
                    "              x-links:\n" +
                    "                name: Josh\n" +
                    "          x-response-extensions:\n" +
                    "            lastName: Hart\n" +
                    "            address: House\n" +
                    "          x-response:\n" +
                    "            name: Josh\n" +
                    "      security:\n" +
                    "      - security_key:\n" +
                    "        - write:pets\n" +
                    "        - read:pets\n" +
                    "      - myOauth2Security:\n" +
                    "        - write:pets\n" +
                    "components:\n" +
                    "  schemas:\n" +
                    "    ExtensionUser:\n" +
                    "      type: object\n" +
                    "      properties:\n" +
                    "        id:\n" +
                    "          type: integer\n" +
                    "          format: int64\n" +
                    "        username:\n" +
                    "          type: string\n" +
                    "        firstName:\n" +
                    "          type: string\n" +
                    "        lastName:\n" +
                    "          type: string\n" +
                    "        email:\n" +
                    "          type: string\n" +
                    "        password:\n" +
                    "          type: string\n" +
                    "        phone:\n" +
                    "          type: string\n" +
                    "        userStatus:\n" +
                    "          type: integer\n" +
                    "          description: User Status\n" +
                    "          format: int32\n" +
                    "          x-userStatus:\n" +
                    "            name: Josh\n" +
                    "          x-userStatus-extensions:\n" +
                    "            lastName: Hart\n" +
                    "            address: House\n" +
                    "      description: User\n" +
                    "      xml:\n" +
                    "        name: User\n" +
                    "      x-user-extensions:\n" +
                    "        lastName: Hart\n" +
                    "        address: House\n" +
                    "      x-user:\n" +
                    "        name: Josh\n" +
                    "  securitySchemes:\n" +
                    "    myOauth2Security:\n" +
                    "      type: oauth2\n" +
                    "      description: myOauthSecurity Description\n" +
                    "      name: myOauth2Security\n" +
                    "      in: header\n" +
                    "      flows:\n" +
                    "        implicit:\n" +
                    "          authorizationUrl: http://x.com\n" +
                    "          scopes:\n" +
                    "            write:pets: modify pets in your account\n" +
                    "          x-oauthflow-extensions:\n" +
                    "            lastName: Hart\n" +
                    "            address: House\n" +
                    "          x-oauthflow:\n" +
                    "            name: Josh\n" +
                    "        x-oauthflows:\n" +
                    "          name: Josh\n" +
                    "        x-oauthflows-extensions:\n" +
                    "          lastName: Hart\n" +
                    "          address: House\n" +
                    "      x-security:\n" +
                    "        name: Josh\n" +
                    "      x-security-extensions:\n" +
                    "        lastName: Hart\n" +
                    "        address: House\n" +
                    "x-openapi:\n" +
                    "  name: Josh\n" +
                    "x-openapi-extensions:\n" +
                    "  lastName: Hart\n" +
                    "  address: House";

}
