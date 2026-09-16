package io.swagger.v3.jaxrs2.resources.generics.ticket2144;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/item")
public class ItemResource extends BaseResource<Item> {

    @GET
    @Path("/{id}")
    @Override
    public ItemWithChildren getById(@PathParam("id") String id) { return null; }

    @GET
    @Path("/nogeneric/{id}")
    @Override
    public ItemWithChildren getByIdNoGeneric(@PathParam("id") String id) { return null; }

    @GET
    @Path("/nogenericsamereturn/{id}")
    @Override
    public BaseDTO getByIdNoGenericSameReturn(@PathParam("id") String id) { return null; }



    @POST
    @Path("/genericparam")
    public BaseDTO genericParam(ItemWithChildren id) { return null; }

}
