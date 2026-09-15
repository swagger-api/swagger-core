package io.swagger.v3.jaxrs2.resources.generics.ticket2144;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public  abstract class BaseResource<T extends BaseDTO> {

    @GET
    @Path("/{id}")
    public T getById(@PathParam("id") String id) { return null; }


    @GET
    @Path("/nogeneric/{id}")
    public BaseDTO getByIdNoGeneric(@PathParam("id") String id) { return null; }

    @GET
    @Path("/nogenericsamereturn/{id}")
    public BaseDTO getByIdNoGenericSameReturn(@PathParam("id") String id) { return null; }


    @POST
    @Path("/genericparam")
    public BaseDTO genericParam(T id) { return null; }

}

