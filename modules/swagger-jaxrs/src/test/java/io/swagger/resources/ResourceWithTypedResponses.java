package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.duplicated.Tag;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Api(value = "/root")
@Path("/")
public class ResourceWithTypedResponses {

    @GET
    @Path("testPrimitiveResponses")
    @ApiResponses({@ApiResponse(code = 400, message = "Message for URI", response = URI.class),
            @ApiResponse(code = 401, message = "Message for URL", response = URL.class),
            @ApiResponse(code = 402, message = "Message for UUID", response = UUID.class),
            @ApiResponse(code = 403, message = "Message for Long", response = Long.class),
            @ApiResponse(code = 404, message = "Message for String", response = String.class)})
    public Response testPrimitiveResponses() {
        return null;
    }

    @GET
    @Path("testObjectResponse")
    public Tag testObjectResponse(Tag body) {
        return body;
    }

    @GET
    @Path("testObjectsResponse")
    public List<Tag> testObjectsResponse(List<Tag> body) {
        return body;
    }

    @GET
    @Path("testStringResponse")
    public String testStringResponse(String body) {
        return body;
    }

    @GET
    @Path("testStringsResponse")
    public List<String> testStringsResponse(List<String> body) {
        return body;
    }

    @GET
    @Path("testMapResponse")
    public Map<Integer, Tag> testMapResponse(Map<Integer, Tag> body) {
        return body;
    }
}
