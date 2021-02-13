package io.swagger.v3.jaxrs2.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.Empty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/noContentResource")
public class ResponseEmptySchemaResource {

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static class ResponseEmptySchemaJsonResource extends ResponseEmptySchemaResource {
    }
    
    @GET
    @Path("/default")
    @Operation(summary = "Summary", description = "Desc", operationId = "default")
    public User getDefault() {
        return null;
    }
    
    @GET
    @Path("/schema")
    @Operation(summary = "Summary", description = "Desc", operationId = "schema")
    @ApiResponse(responseCode = "200", description = "Ok") // CLASS MEDIA TYPE (DEFAULT SCHEMA)
    @ApiResponse(responseCode = "201", description = "Ok", content = @Content()) // CLASS MEDIA TYPE (DEFAULT SCHEMA)
    @ApiResponse(responseCode = "202", description = "Ok", content = @Content(schema = @Schema()))  // CLASS MEDIA TYPE (DEFAULT SCHEMA)
    @ApiResponse(responseCode = "203", description = "Ok", content = @Content(mediaType = "application/xml")) // XML MEDIA TYPE (DEFAULT SCHEMA)
    @ApiResponse(responseCode = "204", description = "Ok", content = @Content(schema = @Schema(implementation = Empty.class))) // NO CONTENT
    @ApiResponse(responseCode = "205", description = "Ok", content = @Content(mediaType = "application/xml", schema = @Schema(implementation = Empty.class))) // NO CONTENT
    @ApiResponse(responseCode = "206", description = "Ok", content = @Content(schema = @Schema(implementation = CustomUser.class))) // CLASS MEDIA TYPE (CUSTOM USER SCHEMA)
    @ApiResponse(responseCode = "207", description = "Ok", content = @Content(mediaType = "application/xml", schema = @Schema(implementation = CustomUser.class))) // XML MEDIA TYPE (CUSTOM USER SCHEMA)
    @ApiResponse(responseCode = "208", description = "Ok", content = @Content(examples = @ExampleObject(name = "example1", value="test"))) // CLASS MEDIA TYPE (EXAMPLE AND DEFAULT SCHEMA)
    @ApiResponse(responseCode = "209", description = "Ok", content = @Content(examples = @ExampleObject(name = "example1", value="test"), schema = @Schema(implementation = Empty.class))) // CLASS MEDIA TYPE (EXAMPLE AND NO SCHEMA)
    @ApiResponse(responseCode = "210", description = "Ok", content = @Content(examples = @ExampleObject(name = "example1", value="test"), schema = @Schema(implementation = CustomUser.class))) // CLASS MEDIA TYPE (EXAMPLE AND CUSTOM USER SCHEMA)
    @ApiResponse(responseCode = "211", description = "Ok", content = @Content(mediaType = "application/xml", examples = @ExampleObject(name = "example1", value="test"))) // XML MEDIA TYPE (EXAMPLE AND DEFAULT SCHEMA)
    @ApiResponse(responseCode = "212", description = "Ok", content = @Content(examples = @ExampleObject(name = "example1", value="test"), schema = @Schema())) // CLASS MEDIA TYPE (EXAMPLE AND DEFAULT SCHEMA)
    public User getUser() {
        return null;
    }

    @GET
    @Path("/arraySchema")
    @Operation(summary = "Summary", description = "Desc", operationId = "array")
    @ApiResponse(responseCode = "200", description = "Ok") // CLASS MEDIA TYPE (DEFAULT ARRAY SCHEMA)
    @ApiResponse(responseCode = "201", description = "Ok", content = @Content()) // CLASS MEDIA TYPE (DEFAULT ARRAY SCHEMA)
    @ApiResponse(responseCode = "202", description = "Ok", content = @Content(schema = @Schema()))  // CLASS MEDIA TYPE (DEFAULT ARRAY SCHEMA)
    @ApiResponse(responseCode = "203", description = "Ok", content = @Content(mediaType = "application/xml")) // XML MEDIA TYPE (DEFAULT ARRAY SCHEMA)
    @ApiResponse(responseCode = "204", description = "Ok", content = @Content(schema = @Schema(implementation = Empty.class))) // NO CONTENT
    @ApiResponse(responseCode = "205", description = "Ok", content = @Content(mediaType = "application/xml", schema = @Schema(implementation = Empty.class))) // NO CONTENT
    @ApiResponse(responseCode = "206", description = "Ok", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomUser.class)))) // CLASS MEDIA TYPE (CUSTOM USER ARRAY SCHEMA)
    @ApiResponse(responseCode = "207", description = "Ok", content = @Content(mediaType = "application/xml", array = @ArraySchema(schema = @Schema(implementation = CustomUser.class)))) // XML MEDIA TYPE (CUSTOM USER ARRAY SCHEMA)
    @ApiResponse(responseCode = "208", description = "Ok", content = @Content(examples = @ExampleObject(name = "example1", value="test"))) // CLASS MEDIA TYPE (EXAMPLE AND DEFAULT ARRAY SCHEMA)
    @ApiResponse(responseCode = "209", description = "Ok", content = @Content(examples = @ExampleObject(name = "example1", value="test"), schema = @Schema(implementation = Empty.class))) // CLASS MEDIA TYPE (EXAMPLE AND NO SCHEMA)
    @ApiResponse(responseCode = "210", description = "Ok", content = @Content(examples = @ExampleObject(name = "example1", value="test"), array = @ArraySchema(schema = @Schema(implementation = CustomUser.class)))) // CLASS MEDIA TYPE (CUSTOM USER ARRAY SCHEMA AND EXAMPLE)
    @ApiResponse(responseCode = "211", description = "Ok", content = @Content(mediaType = "application/xml", examples = @ExampleObject(name = "example1", value="test"))) // XML MEDIA TYPE (EXAMPLE AND DEFAULT ARRAY SCHEMA)
    @ApiResponse(responseCode = "212", description = "Ok", content = @Content(examples = @ExampleObject(name = "example1", value="test"), schema = @Schema())) // CLASS MEDIA TYPE (EXAMPLE AND DEFAULT ARRAY SCHEMA)
    @ApiResponse(responseCode = "213", description = "Ok", content = @Content(array = @ArraySchema())) // CLASS MEDIA TYPE (DEFAULT ARRAY SCHEMA)
    @ApiResponse(responseCode = "214", description = "Ok", content = @Content(examples = @ExampleObject(name = "example1", value="test"), array = @ArraySchema())) // CLASS MEDIA TYPE (EXAMPLE AND DEFAULT ARRAY SCHEMA)
    public List<User> getUsers() {
        return null;
    }
    
    @GET
    @Path("/multipleContents")
    @Operation(summary = "Summary", description = "Desc", operationId = "multiContent")
    @ApiResponse(responseCode = "201", description = "Ok", content = { @Content(), @Content() }) // 1 CLASS MEDIA TYPE AND DEFAULT SCHEMA
    @ApiResponse(responseCode = "202", description = "Ok", content = { @Content(schema = @Schema()), @Content(mediaType="application/xml", schema = @Schema()) })  // CLASS MEDIA TYPE (DEFAULT SCHEMA), XML MEDIA TYPE (DEFAULT SCHEMA)
    @ApiResponse(responseCode = "203", description = "Ok", content = { @Content(mediaType = "application/xml"), @Content(mediaType = "application/json") }) // TWO MEDIA TYPES (DEFAULT SCHEMA)
    @ApiResponse(responseCode = "204", description = "Ok", content = { @Content(schema = @Schema(implementation = Empty.class)), @Content(mediaType = "application/xml") }) // ONE XML MEDIA TYPE (DEFAULT SCHEMA)
    @ApiResponse(responseCode = "205", description = "Ok", content = { @Content(mediaType = "application/json"), @Content(mediaType = "application/xml", schema = @Schema(implementation = Empty.class)) }) // ONE JSON MEDIA TYPE (DEFAULT SCHEMA)
    @ApiResponse(responseCode = "206", description = "Ok", content = { @Content(schema = @Schema(implementation = CustomUser.class)), @Content(mediaType = "application/xml", schema = @Schema(implementation = CustomUser.class))}) // CLASS MEDIA TYPE (CUSTOM USER SCHEMA), XML MEDIA TYPE (CUSTOM USER SCHEMA)
    @ApiResponse(responseCode = "207", description = "Ok", content = { @Content(examples = @ExampleObject(name = "example1", value="test")), @Content(mediaType = "application/xml", examples = @ExampleObject(name = "example2", value="test"))}) // CLASS MEDIA TYPE (DEFAULT SCHEMA AND EXAMPLE), XML MEDIA TYPE (DEFAULT SCHEMA AND EXAMPLE), 
    @ApiResponse(responseCode = "208", description = "Ok", content = { @Content(examples = @ExampleObject(name = "example1", value="test"), schema = @Schema(implementation = Empty.class)), @Content(mediaType = "application/xml")}) // CLASS MEDIA TYPE (EXAMPLE AND NO CONTENT), XML MEDIA TYPE (DEFAULT SCHEMA)
    @ApiResponse(responseCode = "209", description = "Ok", content = { @Content(examples = @ExampleObject(name = "example1", value="test"), schema = @Schema()), @Content(mediaType = "application/xml", examples = @ExampleObject(name = "example1", value="test"), schema = @Schema())}) // CLASS MEDIA TYPE (EXAMPLE AND DEFAULT SCHEMA), XML MEDIA TYPE (EXAMPLE AND DEFAULT SCHEMA)
    public User getUserMultipleContent() {
        return null;
    }

    @Operation(summary = "Summary", responses = {
            @ApiResponse(description = "ok", responseCode = "200", content = @Content(schema = @Schema(oneOf = {
                    User.class, CustomUser.class }))) })
    @GET
    @Path("/oneOf")
    public User oneOf() {
        return null;
    }
    
    @Operation(summary = "Summary", responses = {
            @ApiResponse(description = "ok", responseCode = "200", content = @Content(schema = @Schema(anyOf = {
                    User.class, CustomUser.class }))) })
    @GET
    @Path("/anyOf")
    public User anyOf() {
        return null;
    }

    @Operation(summary = "Summary", responses = {
            @ApiResponse(description = "ok", responseCode = "200", content = @Content(schema = @Schema(allOf = {
                    User.class, CustomUser.class }))) })
    @GET
    @Path("/allOf")
    public User allOf() {
        return null;
    }
    
    class User {
        public String foo;
    }
    
    class CustomUser extends User {
        public String foo2;
    }

}
