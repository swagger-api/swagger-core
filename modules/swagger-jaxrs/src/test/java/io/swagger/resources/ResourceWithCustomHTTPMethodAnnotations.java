package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Api(value = "/custom-http", description = "Resource using custom http methods")
@Path("/")
public class ResourceWithCustomHTTPMethodAnnotations {

    @CUSTOMPATCH
    @ApiOperation(value = "Patch Test")
    public Response patchTest() {
        return Response.ok().build();
    }

    @CUSTOMGET
    @ApiOperation(value = "Get Test")
    public Response getTest() {
        return Response.ok().build();
    }

    @CUSTOMPOST
    @ApiOperation(value = "Post Test")
    public Response postTest() {
        return Response.ok().build();
    }

    @CUSTOMPUT
    @ApiOperation(value = "Put Test")
    public Response putTest() {
        return Response.ok().build();
    }

    @CUSTOMDELETE
    @ApiOperation(value = "Delete Test")
    public Response deleteTest() {
        return Response.ok().build();
    }


    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @HttpMethod("PATCH")
    public @interface CUSTOMPATCH {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @HttpMethod("GET")
    public @interface CUSTOMGET {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @HttpMethod("POST")
    public @interface CUSTOMPOST {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @HttpMethod("PUT")
    public @interface CUSTOMPUT {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @HttpMethod("DELETE")
    public @interface CUSTOMDELETE {
    }
}
