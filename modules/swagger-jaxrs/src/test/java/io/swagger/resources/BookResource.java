package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Api(value = "Books", description = "Book management")
@Path("/{id}/v1/books/")
public class BookResource extends BaseResource {

    @ApiParam("Overridden description")
    @PathParam("description")
    private String description = "Overridden";

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @GET
    @Path("{name}")
    @Produces("text/plain")
    @ApiOperation(value = "Returns book id and name")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Completed Successfully", response = String.class)})
    public String getIt(@ApiParam("The books name") @PathParam("name") final String name) {
        return "Hi there " + name + ", id " + getId() + ", description" + getDescription();
    }
}
