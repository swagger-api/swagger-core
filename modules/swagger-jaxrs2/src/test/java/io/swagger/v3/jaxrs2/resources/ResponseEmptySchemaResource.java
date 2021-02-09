package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.Empty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class ResponseEmptySchemaResource {

    @Operation(
            summary = "Get a list of users",
            description = "Get a list of users registered in the system",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "The response for the user request")
            }
    )
    @GET
    @Path("/user")
    public User getUser() {
        return null;
    }
    
    @Operation(
            summary = "Get a list of users",
            description = "Get a list of users registered in the system"
    )
    @GET
    @Path("/user/defaultApiResponse")
    public User getUserDefaultApiResponse() {
        return null;
    }
    
    @Operation(
            summary = "Get a list of users",
            description = "Get a list of users registered in the system",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "The response for the user request",
                    content = @Content(schema = @Schema(implementation = Empty.class)))
            }
    )
    @GET
    @Path("/user/noContent")
    public User getUserNoContent() {
        return null;
    }
    
    @Operation(
            summary = "Get a list of users",
            description = "Get a list of users registered in the system",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "The response for the user request",
                    content = @Content(schema=@Schema(implementation = CustomUser.class)))
            }
    )
    @GET
    @Path("/user/customSchemaImpl")
    public User getUserCustomSchemaImpl() {
        return null;
    }
    
    @Operation(
            summary = "Get a list of users",
            description = "Get a list of users registered in the system",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "The response for the user request",
                    content = @Content)
            }
    )
    @GET
    @Path("/user/defaultContentAnnotation")
    public User getUserDefaultContentAnnotation() {
        return null;
    }
    
    @Operation(
            summary = "Get a list of users",
            description = "Get a list of users registered in the system",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "The response for the user request",
                    content = @Content(schema = @Schema))
            }
    )
    @GET
    @Path("/user/defaultSchemaAnnotation")
    public User getUserDefaultSchemaAnnotation() {
        return null;
    }
    
    @Operation(
            summary = "Get a list of users",
            description = "Get a list of users registered in the system",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "The response for the user request",
                    content = @Content(schema = @Schema, examples = {@ExampleObject(value = "Example content")}))
            }
    )
    @GET
    @Path("/user/defaultSchemaAnnotationWithExamples")
    public User getUserDefaultSchemaAnnotationWithExamples() {
        return null;
    }
    
    
    class User {
        public String foo;
    }
    
    class CustomUser extends User {
        public String foo2;
    }
}
