package io.swagger.samples.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/items")
@Api("/items")
public class ItemResource {
    @GET
    @Path("/{itemId}")
    @ApiOperation(value = "Returns an item by ID", response = String.class)
    public Response getItemById(@ApiParam("The ID of the item ot be retrieved") @PathParam("itemId") String itemId) {
        return Response.ok().entity("Item: " + itemId).build();
    }

    @POST
    @ApiOperation(value = "Create a new item", notes = "For the sample we just take in an 'id' to 'create' the object.")
    public Response createNewItem(@ApiParam(value = "The id of the parameter", required = true) @QueryParam("itemId") String itemId) {
        return Response.ok().entity("Created item: " + itemId).build();
    }

    @PUT
    @Path("/{itemId}")
    @ApiOperation(value = "Update an existing item", notes = "For this sample, we don't actually provide anything to update with.")
    public Response updateItem(@ApiParam("The id of the item") @PathParam("itemId") String itemId) {
        return Response.ok().entity("Updated item: " + itemId).build();
    }

    @DELETE
    @Path("/{itemId}")
    @ApiOperation(value = "Delete an item")
    public Response deleteItem(@ApiParam("The id of the item") @PathParam("itemId") String itemId) {
        return Response.ok().entity("Deleted item: " + itemId).build();
    }
}
