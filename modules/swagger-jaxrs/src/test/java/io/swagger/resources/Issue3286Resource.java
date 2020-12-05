package io.swagger.resources;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

@Api(value = "test")
@Path("fun")
public class Issue3286Resource {

    @POST
    @Path("/{id}")
    @ApiOperation(value = "test")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = BookResponse.class),
            @ApiResponse(code = 400, message = "Ko", response = Error.class),
    })
    public Response test(@PathParam(value = "id") String bookId, BookRequest book) {
        return Response.ok().build();
    }


    @ApiModel(value = "BookResponse", description = "descResponse")
    @XmlRootElement(name = "book")
    @JsonRootName("book")
    public static class BookResponse {
        public String foo;
    }

    @ApiModel(value = "BookRequest", description = "desc")
    @XmlRootElement(name = "book")
    @JsonRootName("book")
    public static class BookRequest {
        public String bar;
    }

    public static class Error {
        public String err;
    }
}
